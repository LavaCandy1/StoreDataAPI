package com.AyushGarg.StoreDataAPI.DTO.graphql.customer;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ShopifyCustomerDTO {
    
    private String id;
    private Date createdAt;
    private String firstName;
    private String lastName;
    private String email;
    private boolean verifiedEmail;

    @JsonProperty("numberOfOrders")
    private int numberOfOrders;

    @JsonProperty("amountSpent")
    private MoneyDTO totalSpent;
    private LastOrderDTO lastOrder;
    private AddressDTO defaultAddress;

    public Long getCustomerId() {
        if (id == null || id.isEmpty()) return null;
        String[] parts = id.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    @Data
    public static class MoneyDTO {
        private BigDecimal amount;
    }

    @Data
    public static class LastOrderDTO {
        private String id;

        public Long getOrderId() {
            if (id == null || id.isEmpty()) return null;
            String[] parts = id.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        }
    }

    @Data
    public static class AddressDTO {
        private String country;
    }
}