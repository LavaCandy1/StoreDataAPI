package com.AyushGarg.StoreDataAPI.DTO;

import java.util.Date;

import com.AyushGarg.StoreDataAPI.Models.Store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreResponseDTO {

    public StoreResponseDTO(Store store){
        this.domain = store.getDomain();
        this.email = store.getEmail();
        this.storeName = store.getStoreName();
        this.lastSynced = store.getLastSynced();
    }

    private String storeName;
    private String domain;
    private String email;
    private Date lastSynced;
    
}
