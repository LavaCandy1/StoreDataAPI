package com.AyushGarg.StoreDataAPI.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AyushGarg.StoreDataAPI.Models.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long>{
    
    List<Customer> findTop5ByStoreIdOrderByTotalSpentDesc(Long storeId);
}
