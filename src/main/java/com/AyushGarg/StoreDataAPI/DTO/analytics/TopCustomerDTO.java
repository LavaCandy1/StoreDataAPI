package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopCustomerDTO {

    private String name;
    private BigDecimal spend;
}
