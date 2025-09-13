package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class AnalyticsResponseDTO {

    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalCustomers;
    private List<OrdersByDateDTO> ordersByDate;
    private List<TopCustomerDTO> topCustomers;
    
}
