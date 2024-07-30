package com.backend.warehouse_management.utils;

import com.backend.warehouse_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class DeliveryScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 45 19 * * 1-5")
    public void deliveryScheuling() {
        orderService.completeDelivery();
    }

}
