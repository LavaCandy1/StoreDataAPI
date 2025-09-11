package com.AyushGarg.StoreDataAPI.Models;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class Order {

    @Id
    private Long orderId;

    private String orderNumber;
    private Date createdAt;
    
    private BigDecimal totalPrice;
    private String financialStatus;

    private Long customerId;
    
    private Long storeId;

    
}
