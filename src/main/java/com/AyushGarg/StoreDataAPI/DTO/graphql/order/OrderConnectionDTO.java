package com.AyushGarg.StoreDataAPI.DTO.graphql.order;

import java.util.List;

import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;

import lombok.Data;
@Data
public class OrderConnectionDTO {
    private List<OrderEdgeDTO> edges;
    private PageInfoDTO pageInfo;
}
