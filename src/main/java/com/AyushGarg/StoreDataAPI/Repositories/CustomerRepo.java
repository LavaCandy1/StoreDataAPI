package com.AyushGarg.StoreDataAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Customer extends JpaRepository<Customer,Long>{
    
}
