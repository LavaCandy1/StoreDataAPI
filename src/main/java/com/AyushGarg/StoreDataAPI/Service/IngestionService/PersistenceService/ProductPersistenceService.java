package com.AyushGarg.StoreDataAPI.Service.IngestionService.PersistenceService;

import com.AyushGarg.StoreDataAPI.DTO.graphql.product.ShopifyProductDTO;
import com.AyushGarg.StoreDataAPI.Models.Product;
import com.AyushGarg.StoreDataAPI.Repositories.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPersistenceService {

    private final ProductRepo productRepo;

    public ProductPersistenceService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Transactional
    public void saveProduct(ShopifyProductDTO node, Long storeId) {
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
        if (node.getTags() != null) {
            product.setTags(node.getTags().toString());
        }
        productRepo.save(product);
    }
}
