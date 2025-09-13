package com.AyushGarg.StoreDataAPI.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.AyushGarg.StoreDataAPI.Models.User;
import com.AyushGarg.StoreDataAPI.Models.UserPrincipal;
import com.AyushGarg.StoreDataAPI.Repositories.UserRepo;



@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        User user = userRepo.findByEmail(email)
                                    .orElse(null);

        if(user == null) {
            System.out.println("User not Found");
            throw new UsernameNotFoundException("User not found with email: " + email);
        } else {
            return new UserPrincipal(user);
        }
    }
}
