package com.backend.warehouse_management.entity;

import com.backend.warehouse_management.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID orderNumber;
    private LocalDate submittedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //TODO - items that are in this order
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderItems> orderItems;

    private Integer quantity;
    private LocalDate deadline;

    //TODO - delivery connection
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //TODO - user connection
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
