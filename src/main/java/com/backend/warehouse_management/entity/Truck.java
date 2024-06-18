package com.backend.warehouse_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 15)
    private String chassisNumber;
    @Column(unique = true, length = 7)
    private String licensePlate;
    private double capacity;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "truck", orphanRemoval = false)
    private List<Delivery> deliveries;
}
