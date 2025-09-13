package com.AyushGarg.StoreDataAPI.Controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthControler {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user) {

        UserResponseDTO createdUser = userService.createUser(user);
        // UserResponseDTO createdUser = new UserResponseDTO(user);
        return ResponseEntity.ok(createdUser);
    }

    
}
