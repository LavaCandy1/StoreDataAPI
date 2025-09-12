package com.AyushGarg.StoreDataAPI.DTO.graphql;

import java.util.List;

import lombok.Data;

@Data
public class GraphQLResponseDTO<T> {
    private T data;
    private List<ErrorDTO> errors;
}