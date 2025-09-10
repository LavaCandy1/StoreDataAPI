package com.AyushGarg.StoreDataAPI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;

@Service
public class StoreService {

    @Autowired
    private StoreRepo storeRepo;

    public Store createStore(Store store) {
        
        if(storeRepo.existsByUrl(store.getUrl())){
            return null;
        } else {
            //sync stores data and update last synced
            //add url and token validation here too
            return storeRepo.save(store);
        }
    }

    
}
