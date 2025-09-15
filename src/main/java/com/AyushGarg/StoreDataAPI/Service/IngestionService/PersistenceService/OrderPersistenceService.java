package com.AyushGarg.StoreDataAPI.Service.IngestionService.PersistenceService;

import com.AyushGarg.StoreDataAPI.DTO.graphql.order.ShopifyOrderDTO;
import com.AyushGarg.StoreDataAPI.Models.*;
import com.AyushGarg.StoreDataAPI.Repositories.CustomerRepo;
import com.AyushGarg.StoreDataAPI.Repositories.OrderRepo;
import com.AyushGarg.StoreDataAPI.Repositories.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
@Service
public class OrderPersistenceService {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;

    public OrderPersistenceService(OrderRepo orderRepo, CustomerRepo customerRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public void saveOrder(ShopifyOrderDTO node, Long storeId) {
        if (node.getOrderId() == null) return;

        Order order = orderRepo.findById(node.getOrderId())
                .orElse(new Order());

        order.setOrderId(node.getOrderId());
        order.setCreatedAt(new Date(node.getCreatedAt().getTime()));
        order.setFinancialStatus(node.getFinancialStatus());

        if (node.getCurrentTotalPrice() != null && node.getCurrentTotalPrice().getShopMoney() != null) {
            order.setTotalPrice(node.getCurrentTotalPrice().getShopMoney().getAmount());
        }

        if (node.getCustomer() != null && node.getCustomer().getCustomerId() != null) {
            Long customerId = node.getCustomer().getCustomerId();
            Customer customer = customerRepo.findById(customerId)
                    .orElseGet(() -> {
                        Customer c = new Customer();
                        c.setCustomerId(customerId);
                        return customerRepo.save(c);
                    });
            order.setCustomer(customer);
        }

        if (node.getLineItems() != null && node.getLineItems().getEdges() != null) {
            for (ShopifyOrderDTO.LineItemEdgeDTO edge : node.getLineItems().getEdges()) {
                ShopifyOrderDTO.LineItemNodeDTO liNode = edge.getNode();
                if (liNode == null) continue;

                LineItem li = new LineItem();
                li.setQuantity(liNode.getQuantity() != null ? liNode.getQuantity() : 0);
                if (liNode.getUnitPrice() != null) {
                    li.setPrice(liNode.getUnitPrice());
                }

                if (liNode.getProduct() != null && liNode.getProduct().getProductId() != null) {
                    Long productId = liNode.getProduct().getProductId();
                    Product product = productRepo.findById(productId).orElseGet(() -> {
                        Product p = new Product();
                        p.setProductId(productId);
                        p.setStoreId(storeId);
                        return productRepo.save(p);
                    });
                    li.setProduct(product);
                }

                li.setOrder(order);
                order.getItems().add(li);
            }
        }

        order.setStoreId(storeId);
        orderRepo.save(order);
    }
}
