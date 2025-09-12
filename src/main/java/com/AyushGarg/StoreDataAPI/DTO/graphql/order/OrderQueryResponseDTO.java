package com.AyushGarg.StoreDataAPI.DTO.graphql.order;

import lombok.Data;

@Data
public class OrderQueryResponseDTO {
    private OrderConnectionDTO orders;
}
