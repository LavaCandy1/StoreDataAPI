package com.AyushGarg.StoreDataAPI.Controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AyushGarg.StoreDataAPI.Models.User;


@RestController
@RequestMapping("/user")
public class UserControler {

    @GetMapping()
    public User user() {
        return new User();
    }
    
}
