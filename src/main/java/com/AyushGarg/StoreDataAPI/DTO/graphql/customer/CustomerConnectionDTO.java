package com.AyushGarg.StoreDataAPI.DTO.graphql.customer;

import java.util.List;

import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;

import lombok.Data;
@Data
public class CustomerConnectionDTO {
    private List<CustomerEdgeDTO> edges;
    private PageInfoDTO pageInfo;


}
