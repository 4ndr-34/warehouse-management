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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private UUID orderNumber;
    private LocalDate submittedDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    //TODO - items that are in this order
    /*@ManyToMany()
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Inventory> orderItems;*/
    private Integer quantity;
    private LocalDate deadline;
    //TODO - user connection
    //TODO - delivery connection
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
