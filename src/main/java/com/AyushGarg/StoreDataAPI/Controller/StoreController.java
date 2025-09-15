package com.AyushGarg.StoreDataAPI.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.DTO.StoreRequestDTO;
import com.AyushGarg.StoreDataAPI.DTO.StoreResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.OrdersByDateDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.StoreTotalDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.TopCustomerDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Service.StoreService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.CustomerDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.OrderDataIngestionService;
import com.AyushGarg.StoreDataAPI.Service.IngestionService.ProductDataIngestionService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductDataIngestionService productDataIngector;

    @Autowired
    private CustomerDataIngestionService customerDataIngector;

    @Autowired
    private OrderDataIngestionService orderDataIngector;

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@RequestBody StoreRequestDTO storeRequestDTO){

        Store createdStore = storeService.createStore(storeRequestDTO.getDomain(), storeRequestDTO.getAccessToken());

        if(createdStore!=null){
            StoreResponseDTO responseDTO = new StoreResponseDTO(createdStore);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllStores(){
        List<Store> stores = storeService.getAllStores();
        List<StoreResponseDTO> responseDTOs = stores.stream()
                                                    .map(StoreResponseDTO::new)
                                                    .toList();
        return ResponseEntity.ok(responseDTOs);
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

    @PostMapping("/{storeId}/ingestOrders")
    public ResponseEntity<String> ingestOrders(@PathVariable Long storeId){

        orderDataIngector.ingest(storeId);

        return ResponseEntity.ok("Check Database for Orders.");

    }

    @PostMapping("/{storeId}/sync")
    public ResponseEntity<Object> sync(@PathVariable Long storeId) {
        boolean synced = storeService.sync(storeId);

        if (synced) {
            Map<String, String> responseBody = Map.of("message", "Sync completed successfully.");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, String> errorBody = Map.of("message", "Sync failed.");
            return ResponseEntity.status(500).body(errorBody);
        }
    }

    //for analytical data

    @GetMapping("/{id}/ordersByDate")
    public ResponseEntity<List<OrdersByDateDTO>> ordersByDate(@PathVariable Long id, @RequestParam String startDate, @RequestParam String endDate){

        List<OrdersByDateDTO> analyticsData = storeService.getAnalytics(id, startDate, endDate);

        if(analyticsData!=null){
            return ResponseEntity.ok(analyticsData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/{id}/topCustomers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers(@PathVariable Long id){

        List<TopCustomerDTO> topCustomers = storeService.getTopCustomers(id);

        if(topCustomers!=null){
            return ResponseEntity.ok(topCustomers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}/totalDetails")
    public ResponseEntity<StoreTotalDTO> storeTotalData(@PathVariable Long id){

        StoreTotalDTO topCustomers = storeService.getTotalData(id);

        if(topCustomers!=null){
            return ResponseEntity.ok(topCustomers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
