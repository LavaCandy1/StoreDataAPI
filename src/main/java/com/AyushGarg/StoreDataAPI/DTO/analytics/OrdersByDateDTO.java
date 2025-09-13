package com.AyushGarg.StoreDataAPI.DTO.analytics;

import java.sql.Date;

import lombok.Data;

@Data
public class OrdersByDateDTO {

    private Date date;
    private long orders;
    
    public OrdersByDateDTO(Date date, long orders) {
        this.date = date;
        this.orders = orders;
    }
}
