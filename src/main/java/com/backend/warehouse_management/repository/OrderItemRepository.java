package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByProductIdAndOrderId(Long productId, Long orderId);

}
