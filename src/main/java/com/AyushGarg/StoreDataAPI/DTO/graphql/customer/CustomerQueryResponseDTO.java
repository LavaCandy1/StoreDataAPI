package com.AyushGarg.StoreDataAPI.DTO.graphql.customer;

import lombok.Data;

@Data
public class CustomerQueryResponseDTO {
    private CustomerConnectionDTO customers;
}
