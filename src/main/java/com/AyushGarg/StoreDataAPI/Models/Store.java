package com.AyushGarg.StoreDataAPI.Models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="stores")
public class Store {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable=false, unique=true)
    private String storeName;

    @Column(nullable=false)
    private String accessToken;
    
    @Column(nullable=false, unique = true)
    private String url;

    private Date lastSynced;

}
