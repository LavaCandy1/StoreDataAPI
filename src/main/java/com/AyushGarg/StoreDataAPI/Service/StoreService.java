package com.AyushGarg.StoreDataAPI.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.DTO.analytics.AnalyticsRequestDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.AnalyticsResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.AnalyticsTotalsDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.OrdersByDateDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.TopCustomerDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.OrderRepo;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.CustomerDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.OrderDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.ProductDataIngestionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StoreService {

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ShopifyValidationService shopifyValidationService;

    @Autowired
    private CustomerDataIngestionService customerDataIngestionService;
    @Autowired
    private ProductDataIngestionService productDataIngestionService;
    @Autowired
    private OrderDataIngestionService orderDataIngestionService;

    public Store createStore(String domain, String accessToken) {
        
        if(storeRepo.existsByDomain(domain)){
            return null;
        } else {
            //sync stores data and update last synced
            //add url and token validation here too
            Store createdStore = shopifyValidationService
                                                    .gerShopNameIfValid(domain, accessToken)
                                                    .block();

            if(createdStore==null) return null;

            return storeRepo.save(createdStore);
        }
    }

    public List<Store> getAllStores() {
        return storeRepo.findAll();
    }

    public boolean sync(Long storeId) {

        Store fetchStore = storeRepo.findById(storeId)
                                    .orElse(null);
        if (fetchStore==null) return false;
        
        productDataIngestionService.ingest(storeId);
        customerDataIngestionService.ingest(storeId);
        orderDataIngestionService.ingest(storeId);

        fetchStore.setLastSynced(new java.util.Date());

        storeRepo.save(fetchStore);
        return true;

        

    }

    public AnalyticsResponseDTO getAnalytics(Long storeId, String startDateStr, String endDateStr) {

        // 1. Validate the store exists
        if (!storeRepo.existsById(storeId)) {
            throw new EntityNotFoundException("Store with id " + storeId + " not found.");
        }

        // 2. Parse and validate the date strings to java.sql.Date
        Date startDate;
        Date endDate;
        try {
            startDate = Date.valueOf(LocalDate.parse(startDateStr));
            endDate = Date.valueOf(LocalDate.parse(endDateStr));
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }

        // 3. Call the repository methods to fetch the data
        AnalyticsTotalsDTO totals = orderRepo.getAnalyticsTotals(storeId, startDate, endDate);
        List<OrdersByDateDTO> ordersByDate = orderRepo.findOrdersByDate(storeId, startDate, endDate);

        // Create a Pageable object to request only the top 5 results
        Pageable topFive = PageRequest.of(0, 5);
        List<TopCustomerDTO> topCustomers = orderRepo.findTopCustomersBySpend(storeId, startDate, endDate, topFive);

        // 4. Assemble the final response DTO
        AnalyticsResponseDTO response = new AnalyticsResponseDTO();
        response.setTotalRevenue(totals.getTotalRevenue());
        response.setTotalOrders(totals.getTotalOrders());
        response.setTotalCustomers(totals.getTotalCustomers());
        response.setOrdersByDate(ordersByDate);
        response.setTopCustomers(topCustomers);

        return response;
    }

    
}
