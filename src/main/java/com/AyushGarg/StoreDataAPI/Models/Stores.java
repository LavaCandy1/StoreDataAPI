package com.AyushGarg.StoreDataAPI.Models;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="stores")
public class Stores {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long store_id;

    @Column(nullable=false, unique=true)
    private String store_name;

    @Column(nullable=false)
    private String access_token;
    
    @Column(nullable=false)
    private String store_url;

    @Column(nullable=false)
    private Date last_synced;

}
