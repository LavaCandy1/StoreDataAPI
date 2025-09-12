package com.AyushGarg.StoreDataAPI.DTO.graphql;

import lombok.Data;

@Data
public class GraphQLResponseDTO<T> {
    private T data;
}