package com.AyushGarg.StoreDataAPI.Service.IngestionService.PersistenceService;

import com.AyushGarg.StoreDataAPI.DTO.graphql.customer.ShopifyCustomerDTO;
import com.AyushGarg.StoreDataAPI.Models.Customer;
import com.AyushGarg.StoreDataAPI.Repositories.CustomerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

/**
 * This service is responsible for persisting customer data to the database.
 * Each public method runs in its own transaction to prevent memory leaks.
 */
@Service
public class CustomerPersistenceService {

    private final CustomerRepo customerRepo;

    public CustomerPersistenceService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    /**
     * Saves a single customer to the database in a new transaction.
     *
     * @param node    The customer data from Shopify.
     * @param storeId The ID of the store this customer belongs to.
     */
    @Transactional
    public void saveCustomer(ShopifyCustomerDTO node, Long storeId) {
        if (node.getCustomerId() == null) {
            // Do not process customers without an ID.
            return;
        }

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
