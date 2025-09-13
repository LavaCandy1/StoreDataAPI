package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AnalyticsTotalsDTO {
     private BigDecimal totalRevenue;
    private long totalOrders;
    private long totalCustomers;

    public AnalyticsTotalsDTO(BigDecimal totalRevenue, long totalOrders, long totalCustomers) {
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        this.totalOrders = totalOrders;
        this.totalCustomers = totalCustomers;
    }
}