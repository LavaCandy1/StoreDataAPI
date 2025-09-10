package com.AyushGarg.StoreDataAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AyushGarg.StoreDataAPI.Models.Stores;

@Repository
public interface StoresRepo extends JpaRepository<Stores, Long> {
    
}