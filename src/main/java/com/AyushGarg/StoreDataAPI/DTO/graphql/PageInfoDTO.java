package com.AyushGarg.StoreDataAPI.DTO.graphql;

import lombok.Data;

@Data
public class PageInfoDTO {

    private boolean hasNextPage;
    private String endCursor;

}
