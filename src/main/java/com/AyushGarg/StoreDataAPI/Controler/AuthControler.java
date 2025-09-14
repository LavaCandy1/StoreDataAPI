package com.AyushGarg.StoreDataAPI.Controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.DTO.LoginRequestDTO;
import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Service.AuthService;
import com.AyushGarg.StoreDataAPI.Service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthControler {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user) {

        UserResponseDTO createdUser = userService.createUser(user);
        // UserResponseDTO createdUser = new UserResponseDTO(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){

        System.out.println("Someone loggin in.");
        UserResponseDTO userResponseDTO = authService.loginUser(loginRequestDTO);

        return ResponseEntity.ok(userResponseDTO);
        
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // No user is authenticated for the current session
            return ResponseEntity.status(401).build();
        }

        // The 'principal' contains the user details from your UserDetailsService
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // You would convert your User entity to a UserResponseDTO here.
        // This is a simplified example; you should fetch the full User object from your repository.
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setEmail(userDetails.getUsername());
        // Populate other fields like name, id, etc. from your User object
        
        return ResponseEntity.ok(userDto);
    }
    
}
