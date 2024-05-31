package com.backend.warehouse_management.entity;

import com.backend.warehouse_management.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID orderNumber;
    private LocalDate submittedDate;
    //TODO - status enum connection
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    //TODO - item connection
    private Integer quantity;
    private LocalDate deadline;
    //TODO - user connection
}
