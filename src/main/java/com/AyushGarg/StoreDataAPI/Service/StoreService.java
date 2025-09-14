package com.AyushGarg.StoreDataAPI.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.DTO.analytics.OrdersByDateDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.StoreTotalDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.TopCustomerDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.CustomerRepo;
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

    @Autowired CustomerRepo customerRepo;

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

    public List<OrdersByDateDTO> getAnalytics(Long storeId, String startDateStr, String endDateStr) {

        if (!storeRepo.existsById(storeId)) {
            throw new EntityNotFoundException("Store with id " + storeId + " not found.");
        }

        Date startDate;
        Date endDate;
        try {
            startDate = Date.valueOf(LocalDate.parse(startDateStr));
            endDate = Date.valueOf(LocalDate.parse(endDateStr));
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }

        return orderRepo.findOrdersByDate(storeId, startDate, endDate);
    }

    public List<TopCustomerDTO> getTopCustomers(Long id) {
        
        List<TopCustomerDTO> topCustomers = customerRepo.findTop5ByStoreIdOrderByTotalSpentDesc(id)
                                                        .stream()
                                                        .map(TopCustomerDTO::new)
                                                        .toList();

        return topCustomers;
    }

    public StoreTotalDTO getTotalData(Long id) {
        
        StoreTotalDTO totalDTO = new StoreTotalDTO();

        totalDTO.setTotalCustomers(customerRepo.countByStoreId(id));
        totalDTO.setTotalOrders(orderRepo.countByStoreId(id));
        totalDTO.setTotalRevenue(orderRepo.getTotalRevenueByStoreId(id));

        return totalDTO;
    }

    
}
