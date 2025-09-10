package com.AyushGarg.StoreDataAPI.DTO;

import com.AyushGarg.StoreDataAPI.Models.User;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;

    public UserResponseDTO(User user) {
        this.id = user.getUser_id();
        this.name = user.getUsername();
        this.email = user.getEmail();
    }

    // getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
}