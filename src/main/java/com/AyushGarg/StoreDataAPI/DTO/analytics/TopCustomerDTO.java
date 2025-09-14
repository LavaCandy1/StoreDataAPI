package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.math.BigDecimal;

import com.AyushGarg.StoreDataAPI.Models.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopCustomerDTO {

    private String name;
    private BigDecimal spend;

    public TopCustomerDTO(Customer customer){
        this.name = customer.getFirstName()+" "+customer.getLastName();
        this.spend = customer.getTotalSpent();
    }
}

