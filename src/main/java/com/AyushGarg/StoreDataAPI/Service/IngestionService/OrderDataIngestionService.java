package com.AyushGarg.StoreDataAPI.Service.IngestionService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.AyushGarg.StoreDataAPI.DTO.graphql.GraphQLResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.order.ShopifyOrderDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.order.OrderEdgeDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.order.OrderQueryResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.Customer;
import com.AyushGarg.StoreDataAPI.Models.LineItem;
import com.AyushGarg.StoreDataAPI.Models.Order;
import com.AyushGarg.StoreDataAPI.Models.Product;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.CustomerRepo;
import com.AyushGarg.StoreDataAPI.Repositories.OrderRepo;
import com.AyushGarg.StoreDataAPI.Repositories.ProductRepo;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderDataIngestionService {
    
    private final WebClient.Builder webClientBuilder;
    private final OrderRepo orderRepo;
    private final StoreRepo storeRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;

    public OrderDataIngestionService(
                                        WebClient.Builder webClientBuilder,
                                        OrderRepo orderRepo, 
                                        StoreRepo storeRepo, 
                                        CustomerRepo customerRepo, 
                                        ProductRepo productRepo) {

        this.webClientBuilder = webClientBuilder;
        this.orderRepo = orderRepo;
        this.storeRepo = storeRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;

    }

    @Transactional
    public void ingest(Long storeId) {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));

        String domain = store.getDomain();
        String accessToken = store.getAccessToken();
        String graphqlURL = "https://" + domain + "/admin/api/2025-07/graphql.json";

        WebClient webClient = webClientBuilder
                                                .baseUrl(graphqlURL)
                                                .defaultHeader("X-Shopify-Access-Token", accessToken)
                                                .defaultHeader("Content-Type", "application/json")
                                                .build();


        String graphqlQuery = """
            query getOrders($cursor: String) {
                orders(first: 50, after: $cursor) {
                    edges {
                        node {
                            id
                            name
                            createdAt
                            currentTotalPriceSet {
                                shopMoney {
                                    amount
                                    currencyCode
                                }
                            }
                            customer {
                                id
                                email
                            }
                            lineItems(first: 10) {
                                edges {
                                    node {
                                        id
                                        name
                                        quantity
                                        originalUnitPriceSet {
                                            shopMoney {
                                                amount
                                                currencyCode
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    pageInfo {
                        hasNextPage
                        endCursor
                    }
                }
            }

            """;

        boolean hasNextPage = true;
        String cursor = null;

        do {
            Map<String, Object> variables = Collections.singletonMap("cursor", cursor);
            Map<String, Object> body = Map.of("query", graphqlQuery, "variables", variables);

            OrderQueryResponseDTO response = webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<GraphQLResponseDTO<OrderQueryResponseDTO>>() {})
                    .map(GraphQLResponseDTO::getData)
                    // .bodyToMono(String.class)
                    .block();


            if (response != null && response.getOrders() != null) {
                for (OrderEdgeDTO edge : response.getOrders().getEdges()) {
                    saveOrder(edge.getNode(), store.getStoreId());
                }

                PageInfoDTO pageInfo = response.getOrders().getPageInfo();
                hasNextPage = pageInfo.isHasNextPage();
                cursor = pageInfo.getEndCursor();
            } else {
                hasNextPage = false;
            }

        } while (hasNextPage);


    }

    private void saveOrder(ShopifyOrderDTO node, Long storeId) {
        if (node.getOrderId() == null) return;

        Order order = orderRepo.findById(node.getOrderId())
                .orElse(new Order());

        order.setOrderId(node.getOrderId());
        order.setCreatedAt(new Date(node.getCreatedAt().getTime()));

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
            customer.getOrders().add(order);

            customerRepo.save(customer);
        } else {
            return;
        }

    if (node.getLineItems() != null && node.getLineItems().getEdges() != null) {
        order.getItems().clear();

        for (ShopifyOrderDTO.LineItemEdgeDTO edge : node.getLineItems().getEdges()) {
            ShopifyOrderDTO.LineItemNodeDTO liNode = edge.getNode();
            if (liNode == null) continue;

            LineItem li = new LineItem();
            li.setQuantity(liNode.getQuantity() != null ? liNode.getQuantity() : 0);
            if (liNode.getUnitPrice() != null) li.setPrice(liNode.getUnitPrice());

            if (liNode.getProduct() != null && liNode.getProduct().getProductId() != null) {
                Long productId = liNode.getProduct().getProductId();

                Product product = productRepo.findById(productId).orElseGet(() -> {
                    Product p = new Product();
                    p.setProductId(productId);
                    p.setStoreId(storeId);
                    return productRepo.save(p);
                });

                li.setProduct(product);
            } else {
                continue;
            }

            li.setOrder(order);
            order.getItems().add(li);
        }
    }

        order.setStoreId(storeId);
        orderRepo.save(order);
    }
}