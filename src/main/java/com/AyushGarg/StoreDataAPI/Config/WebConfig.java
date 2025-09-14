package com.AyushGarg.StoreDataAPI.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class WebConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // This line directly solves the warning you're seeing
        serializer.setSameSite("None"); 
        serializer.setUseSecureCookie(false); 
        return serializer;
    }
}