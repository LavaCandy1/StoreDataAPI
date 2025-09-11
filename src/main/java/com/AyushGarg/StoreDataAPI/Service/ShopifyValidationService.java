package com.AyushGarg.StoreDataAPI.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.AyushGarg.StoreDataAPI.Models.Store;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import reactor.core.publisher.Mono;

@Service
public class ShopifyValidationService {

    private final WebClient.Builder webClientBuilder;

    public ShopifyValidationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Store> gerShopNameIfValid(String storeDomain, String accessToken) {

        WebClient webClient = webClientBuilder.baseUrl("https://"+ storeDomain+ "/admin/api/2025-07/")
                                                .defaultHeader("X-Shopify-Access-Token", accessToken)
                                                .build();

        return webClient.get()
                        .uri("shop.json")
                        .retrieve()
                        .bodyToMono(ShopifyResponseDTO.class)
                        .map(response -> {
                            Store store = new Store();
                            store.setDomain(storeDomain);
                            store.setAccessToken(accessToken);
                            store.setStoreName(response.shop.name);
                            store.setEmail(response.shop.email);
                            return store;
                        })
                        .onErrorResume(e -> Mono.empty());

    }
    
}


@JsonIgnoreProperties(ignoreUnknown = true)
class ShopifyResponseDTO {
    public Shop shop;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Shop {
        public String name;
        public String email;
    }
}

