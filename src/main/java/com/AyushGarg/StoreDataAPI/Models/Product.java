package com.AyushGarg.StoreDataAPI.Models;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="products")
public class Product {
    
    @Id
    private Long productId;

    private String title;
    private String vendor;
    private String productType;
    private String handle;
    private boolean status;

    private List<String> tags;

    private Date createdAt;


}
