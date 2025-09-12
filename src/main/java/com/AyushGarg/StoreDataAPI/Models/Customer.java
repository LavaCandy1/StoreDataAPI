package com.AyushGarg.StoreDataAPI.Models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    private int ordersCount;
    private BigDecimal totalSpent;
    
    private Long lastOrderId;
    // in orders.json it is named as order_number but in customers json it is last_order_name so i went with lastOrderNumber for consistency
    //although in customers it has # at start
    private String country;

    private Long storeId;

    @OneToMany(
        mappedBy = "customer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @ElementCollection
    @Column(name = "orders", columnDefinition = "text[]")
    private List<Order> orders = new ArrayList<>();
    
}
