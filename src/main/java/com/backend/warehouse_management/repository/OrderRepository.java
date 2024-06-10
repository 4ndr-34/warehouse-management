package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select order from Order order where order.user.id = :userId")
    Optional<Order> findByUserId(Long userId);

    Optional<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    Order findByIdAndOrderStatus(Long id, OrderStatus orderStatus);

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
