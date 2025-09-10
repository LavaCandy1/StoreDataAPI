package com.AyushGarg.StoreDataAPI.Models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
        name="user_stores",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="store_id")
    )
    private Set<Store> stores;
    
}
