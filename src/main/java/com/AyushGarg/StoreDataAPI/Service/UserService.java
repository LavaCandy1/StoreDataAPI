package com.AyushGarg.StoreDataAPI.Service;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Repositories.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    
    public User getUserById(Long id) {

        return userRepo.findById(id)
                        .orElse(null);
    }
    public UserResponseDTO getUserDetailsById(Long id) {

        return userRepo.findById(id)
                        .map(UserResponseDTO::new)
                        .orElse(null);
    }

    public UserResponseDTO createUser(User user){
        
        if(userRepo.findByEmail(user.getEmail())==null){
            return null;
        }
        user.setPassword(passwordEncoder.encode((user.getPassword())));
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

    public String verifyUser(User user) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        
        return "Authentication failed";
    }

}
