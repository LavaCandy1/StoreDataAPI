package com.AyushGarg.StoreDataAPI.DTO;

import com.AyushGarg.StoreDataAPI.Models.User;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;

    public UserResponseDTO(User user) {
        this.id = user.getUser_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}