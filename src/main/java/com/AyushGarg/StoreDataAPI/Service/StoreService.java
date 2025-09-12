package com.AyushGarg.StoreDataAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.CustomerDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.OrderDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.ProductDataIngestionService;

@Service
public class StoreService {

    @Autowired
    private StoreRepo storeRepo;

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

    public void sync(Long storeId) {
        
        productDataIngestionService.ingest(storeId);
        customerDataIngestionService.ingest(storeId);
        orderDataIngestionService.ingest(storeId);
    }

    
}
