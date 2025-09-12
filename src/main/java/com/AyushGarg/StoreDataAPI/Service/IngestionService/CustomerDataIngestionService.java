package com.AyushGarg.StoreDataAPI.Service.IngestionService;

import java.sql.Date;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.AyushGarg.StoreDataAPI.DTO.graphql.GraphQLResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.customer.CustomerEdgeDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.customer.CustomerQueryResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.customer.ShopifyCustomerDTO;
import com.AyushGarg.StoreDataAPI.Models.Customer;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.CustomerRepo;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerDataIngestionService {

    private final WebClient.Builder webClientBuilder;
    private final CustomerRepo customerRepo;
    private final StoreRepo storeRepo;

    public CustomerDataIngestionService(WebClient.Builder webClientBuilder, CustomerRepo customerRepo, StoreRepo storeRepo) {
        this.webClientBuilder = webClientBuilder;
        this.customerRepo = customerRepo;
        this.storeRepo = storeRepo;
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
            query getCustomers($cursor: String) {
                customers(first: 50, after: $cursor) {
                    edges {
                    node {
                        id
                        createdAt
                        firstName
                        lastName
                        email
                        verifiedEmail
                        numberOfOrders
                        amountSpent {
                        amount
                        }
                        lastOrder { id }
                        defaultAddress { country }
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

            CustomerQueryResponseDTO response = webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<GraphQLResponseDTO<CustomerQueryResponseDTO>>() {})
                    .map(GraphQLResponseDTO::getData)
                    // .bodyToMono(String.class)
                    .block();

            System.out.println(response);

            if (response != null && response.getCustomers() != null) {
                for (CustomerEdgeDTO edge : response.getCustomers().getEdges()) {
                    saveCustomer(edge.getNode(), store.getStoreId());
                }

                PageInfoDTO pageInfo = response.getCustomers().getPageInfo();
                hasNextPage = pageInfo.isHasNextPage();
                cursor = pageInfo.getEndCursor();
            } else {
                hasNextPage = false;
            }

        } while (hasNextPage);
        System.out.println("Finished initial sync for customers for store: " + store.getDomain());
    }

    private void saveCustomer(ShopifyCustomerDTO node, Long storeId) {
        if (node.getCustomerId() == null) return;

        Customer customer = customerRepo.findById(node.getCustomerId())
                .orElse(new Customer());

        customer.setCustomerId(node.getCustomerId());
        customer.setCreatedAt(new Date(node.getCreatedAt().getTime())); 
        customer.setFirstName(node.getFirstName());
        customer.setLastName(node.getLastName());
        customer.setEmail(node.getEmail());
        customer.setVerifiedEmail(node.isVerifiedEmail());
        customer.setOrdersCount(node.getNumberOfOrders());
        if (node.getTotalSpent() != null) {
            customer.setTotalSpent(node.getTotalSpent().getAmount());
        }
        if (node.getLastOrder() != null) {
            customer.setLastOrderId(node.getLastOrder().getOrderId());
        }
        if (node.getDefaultAddress() != null) {
            customer.setCountry(node.getDefaultAddress().getCountry());
        }
        customer.setStoreId(storeId);

        customerRepo.save(customer);
    }
}