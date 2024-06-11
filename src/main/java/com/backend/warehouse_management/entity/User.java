package com.backend.warehouse_management.entity;

import com.backend.warehouse_management.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    //TODO - connection with user role
    @Enumerated(EnumType.STRING)
    private UserRole role;
    //TODO - connection with order

    @OneToMany
    private List<Order>orders;

    public Long getId() {
        return id;
    }

}
