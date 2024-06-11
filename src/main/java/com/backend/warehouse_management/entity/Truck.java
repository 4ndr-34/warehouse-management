package com.backend.warehouse_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    //TODO - connection with delivery
}
