package com.AyushGarg.StoreDataAPI.Service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Repositories.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;
    
    public UserResponseDTO getUserById(Long id) {

        return userRepo.findById(id)
                        .map(UserResponseDTO::new)
                        .orElse(null);
    }

    public UserResponseDTO createUser(User user){
        
        if(userRepo.findByEmail(user.getEmail())!=null){
            return null;
        }
        return new UserResponseDTO(userRepo.save(user));

    }
    
    public Set<Store> getUserStores(String email) {

        return userRepo.findByEmail(email)
                        .map(User::getStores)
                        .orElse(null);
    }

    public User getUserByEmail(String email) {
        
        return userRepo.findByEmail(email)
                        .orElse(null);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

}
