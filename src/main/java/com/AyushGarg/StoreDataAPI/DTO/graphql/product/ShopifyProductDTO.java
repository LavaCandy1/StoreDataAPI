package com.AyushGarg.StoreDataAPI.DTO.graphql.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ShopifyProductDTO {

    @JsonProperty("id")
    private String shopifyGid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("vendor")
    private String vendor;

    @JsonProperty("productType")
    private String productType;

    @JsonProperty("handle")
    private String handle;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("createdAt")
    private ZonedDateTime createdAt;

    public Long getProductId() {
        if (shopifyGid == null || shopifyGid.isEmpty()) {
            return null;
        }
        try {
            String[] parts = shopifyGid.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }
}