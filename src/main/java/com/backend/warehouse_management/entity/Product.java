package com.backend.warehouse_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 100, nullable = false)
    private String itemName;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Double volume;

}


