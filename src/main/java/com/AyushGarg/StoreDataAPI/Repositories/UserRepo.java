package com.AyushGarg.StoreDataAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AyushGarg.StoreDataAPI.Models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    
}
