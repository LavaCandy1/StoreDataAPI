package com.AyushGarg.StoreDataAPI.DTO;

import com.AyushGarg.StoreDataAPI.Models.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;

    public UserResponseDTO(User user) {
        this.id = user.getUser_id();
        this.name = user.getUsername();
        this.email = user.getEmail();
    }

}