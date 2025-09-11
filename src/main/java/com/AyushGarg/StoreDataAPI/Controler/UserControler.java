package com.AyushGarg.StoreDataAPI.Controler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.DTO.UserResponseDTO;
import com.AyushGarg.StoreDataAPI.Models.Store;
import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Service.UserService;


@RestController
@RequestMapping("/user")
public class UserControler {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}") //for getting userdetials for profile
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {

        UserResponseDTO user = userService.getUserById(userId);
        
        if(user!=null){
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping //for signup
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user){

        UserResponseDTO createdUser = userService.createUser(user);
        
        if(createdUser!=null){
            return ResponseEntity.ok(createdUser);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/{userId}/stores") //for getting stores (and related data maybe?!)
    public ResponseEntity<Set<Store>> getStores(@PathVariable Long userId){

        Set<Store> stores = userService.getUserStores(userId);

        if(stores!=null){
            return ResponseEntity.ok(stores);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
