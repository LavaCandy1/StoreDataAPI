package com.AyushGarg.StoreDataAPI.Service;

import com.AyushGarg.StoreDataAPI.DTO.LoginRequestDTO;
import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepository; 

    public UserResponseDTO loginUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        System.out.println("loggin in");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication"));
        System.out.println("logged in");
        return new UserResponseDTO(user);
    }
}
