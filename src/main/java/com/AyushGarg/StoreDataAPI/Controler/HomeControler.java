package com.AyushGarg.StoreDataAPI.Controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeControler {
    
    @GetMapping()
    public String home() {
        return "Welcome to home page of Store DaTa API";
    }
}
