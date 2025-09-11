package com.AyushGarg.StoreDataAPI.Models;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="customers")
public class Customer {
    
    @Id
    private Long customerId;

    private Date createdAt;
    private String firstName;
    private String lastName;

    private String email;
    private boolean verifiedEmail;

    private String ordersCount;
    private String totalSpent;
    
    private Long lastOrderId;
    private String lastOrderNumber;
    // in orders.json it is named as order_number but in customers json it is last_order_name so i went with lastOrderNumber for consistency
    //although in customers it has # at start
    private String country;

    
}
