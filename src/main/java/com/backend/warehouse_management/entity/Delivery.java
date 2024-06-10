package com.backend.warehouse_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //TODO - connection with orders
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "delivery")
    private List<Order> orders;
    //TODO - connection with trucks

    public Long getId() {
        return id;
    }
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
