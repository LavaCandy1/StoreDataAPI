package com.AyushGarg.StoreDataAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;

@Service
public class StoreService {

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private ShopifyValidationService shopifyValidationService;

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

    
}
