package com.AyushGarg.StoreDataAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AyushGarg.StoreDataAPI.Models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    
}
