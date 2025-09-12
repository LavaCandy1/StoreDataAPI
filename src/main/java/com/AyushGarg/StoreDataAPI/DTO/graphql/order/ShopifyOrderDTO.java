package com.AyushGarg.StoreDataAPI.DTO.graphql.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ShopifyOrderDTO {
    
    private String id;
    private String name;
    private Integer orderNumber;
    private Date createdAt;
    private String financialStatus;
    private TotalPriceSetDTO currentTotalPrice;
    private CustomerDTO customer;
    private LineItemsDTO lineItems;

    public Long getOrderId() {
        if (id == null || id.isEmpty()) return null;
        String[] parts = id.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    public BigDecimal getTotalPrice() {
        if (currentTotalPrice != null && currentTotalPrice.getShopMoney() != null) {
            return currentTotalPrice.getShopMoney().getAmount();
        }
        return null;
    }

    @Data
    public static class TotalPriceSetDTO {
        private MoneyDTO shopMoney;
    }

    @Data
    public static class MoneyDTO {
        private BigDecimal amount;
        private String currencyCode;
    }

    @Data
    public static class CustomerDTO {
        private String id;
        private String email;

        public Long getCustomerId() {
            if (id == null || id.isEmpty()) return null;
            String[] parts = id.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        }
    }

    @Data
    public static class LineItemsDTO {
        private List<LineItemEdgeDTO> edges;
    }

    @Data
    public static class LineItemEdgeDTO {
        private LineItemNodeDTO node;
    }

    @Data
    public static class LineItemNodeDTO {
        private String id;
        private String name;
        private Integer quantity;
        private UnitPriceSetDTO originalUnitPriceSet;
        private ProductDTO product;

        public Long getLineItemId() {
            if (id == null || id.isEmpty()) return null;
            String[] parts = id.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        }

        public BigDecimal getUnitPrice() {
            if (originalUnitPriceSet != null && originalUnitPriceSet.getShopMoney() != null) {
                return originalUnitPriceSet.getShopMoney().getAmount();
            }
            return null;
        }
    }

    @Data
    public static class UnitPriceSetDTO {
        private MoneyDTO shopMoney;
    }

    @Data
    public static class ProductDTO {
        private String id;
        private String title;
        private String vendor;
        private String productType;
        private String handle;
        private String status;

        public Long getProductId() {
            if (id == null || id.isEmpty()) return null;
            String[] parts = id.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        }
    }
}
