package com.AyushGarg.StoreDataAPI.Controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreControler {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody Store store){

        Store createdStore = storeService.createStore(store);

        if(createdStore!=null){
            return ResponseEntity.ok(createdStore);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
    
}
