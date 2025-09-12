package com.AyushGarg.StoreDataAPI.Controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.DTO.StoreRequestDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Service.StoreService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.CustomerDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.ProductDataIngestionService;

@RestController
@RequestMapping("/store")
public class StoreControler {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductDataIngestionService productDataIngector;

    @Autowired
    private CustomerDataIngestionService customerDataIngector;

    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody StoreRequestDTO storeRequestDTO){

        Store createdStore = storeService.createStore(storeRequestDTO.getDomain(), storeRequestDTO.getAccessToken());

        if(createdStore!=null){
            return ResponseEntity.ok(createdStore);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores(){
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @PostMapping("/{storeId}/ingestProducts")
    public ResponseEntity<String> ingestProduct(@PathVariable Long storeId){

        productDataIngector.ingest(storeId);

        return ResponseEntity.ok("Check Database for Products.");

    }

    @PostMapping("/{storeId}/ingestCustomers")
    public ResponseEntity<String> ingestCustomer(@PathVariable Long storeId){

        customerDataIngector.ingest(storeId);

        return ResponseEntity.ok("Check Database for Customers.");

    }
    
}
