package com.AyushGarg.StoreDataAPI.Models;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {
    
    @Id
    private Long productId;

    private String title;
    private String vendor;
    private String productType;
    private String handle;
    private String status;

    private String tags;

    private ZonedDateTime createdAt;
    
    private Long storeId;

    @OneToMany(mappedBy = "product")
    private List<LineItem> orderItems = new ArrayList<>();


}
