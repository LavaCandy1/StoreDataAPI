package com.AyushGarg.StoreDataAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AyushGarg.StoreDataAPI.Models.Store;

@Repository
public interface StoreRepo extends JpaRepository<Store, Long> {

    boolean existsByUrl(String url);
    
}