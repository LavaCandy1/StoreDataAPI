package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StoreTotalDTO {

    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalCustomers;

}
