package com.AyushGarg.StoreDataAPI.DTO.graphql.product;

import java.util.List;

import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;

import lombok.Data;
@Data
public class ProductConnectionDTO {
    private List<ProductEdgeDTO> edges;
    private PageInfoDTO pageInfo;


}
