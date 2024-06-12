package com.backend.warehouse_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate scheduledDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "delivery")
    private List<Order> orders;
    @ManyToOne
    @JoinColumn(name = "truck_id", referencedColumnName = "id")
    private Truck truck;
    private Double remainingSpace;
}
