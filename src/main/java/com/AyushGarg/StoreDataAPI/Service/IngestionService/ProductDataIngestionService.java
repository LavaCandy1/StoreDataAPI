package com.AyushGarg.StoreDataAPI.Service.IngestionService;

import java.util.Collections;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.AyushGarg.StoreDataAPI.DTO.graphql.GraphQLResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.PageInfoDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.product.ProductEdgeDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.product.ProductQueryResponseDTO;
import com.AyushGarg.StoreDataAPI.DTO.graphql.product.ShopifyProductDTO;
import com.AyushGarg.StoreDataAPI.Models.Product;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Repositories.ProductRepo;
import com.AyushGarg.StoreDataAPI.Repositories.StoreRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductDataIngestionService {

    private final WebClient.Builder webClientBuilder;
    private final ProductRepo productRepo;
    private final StoreRepo storeRepo;

    public ProductDataIngestionService(WebClient.Builder webClientBuilder, ProductRepo productRepo, StoreRepo storeRepo) {
        this.webClientBuilder = webClientBuilder;
        this.productRepo = productRepo;
        this.storeRepo = storeRepo;
    }

    @Transactional
    public void ingest(Long storeId){

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
            query getProducts($cursor: String) {
                products(first: 50, after: $cursor) {
                    edges {
                        node {
                            id
                            title
                            createdAt
                            vendor
                            productType
                            status
                            handle
                            tags
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

            Map<String,Object> variables = Collections.singletonMap("cursor", cursor);
            Map<String, Object> body = Map.of("query", graphqlQuery, "variables", variables);

            ProductQueryResponseDTO response = webClient.post()
                                                        .bodyValue(body)
                                                        .retrieve()
                                                        .bodyToMono(new ParameterizedTypeReference<GraphQLResponseDTO<ProductQueryResponseDTO>>() {})
                                                        .map(GraphQLResponseDTO::getData)
                                                        .block();

            if (response != null && response.getProducts() != null) {
                for (ProductEdgeDTO edge : response.getProducts().getEdges()) {
                    saveProduct(edge.getNode(), store.getStoreId());
                }

                PageInfoDTO pageInfo = response.getProducts().getPageInfo();
                hasNextPage = pageInfo.isHasNextPage();
                cursor = pageInfo.getEndCursor();
            } else {
                hasNextPage = false;
            }



        } while (hasNextPage);
        

    }

    private void saveProduct(ShopifyProductDTO node, Long storeId) {

        // System.out.println(node);
        
        Product product = productRepo.findById(node.getProductId())
                .orElse(new Product());

        product.setProductId(node.getProductId());
        product.setTitle(node.getTitle());
        product.setVendor(node.getVendor());
        product.setProductType(node.getProductType());
        product.setStoreId(storeId);
        product.setCreatedAt(node.getCreatedAt());
        product.setHandle(node.getHandle());
        product.setStatus(node.getStatus());
        // product.setTags(node.getTags());



        productRepo.save(product);
    }
    
}
