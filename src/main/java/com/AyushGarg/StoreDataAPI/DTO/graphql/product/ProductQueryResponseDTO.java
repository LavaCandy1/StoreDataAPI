package com.AyushGarg.StoreDataAPI.DTO.graphql.product;

import lombok.Data;

@Data
public class ProductQueryResponseDTO {
    private ProductConnectionDTO products;
}
