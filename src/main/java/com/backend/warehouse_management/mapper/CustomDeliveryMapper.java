package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.entity.Delivery;

import java.util.stream.Collectors;

public class CustomDeliveryMapper {

    public static DeliveryDTO managerMapDeliveryToDeliveryDTO(Delivery delivery) {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setDeliveryId(delivery.getId());
        deliveryDTO.setTruckId(delivery.getTruck().getId());
        if(delivery.getOrders() != null) {
            deliveryDTO.setOrders(delivery.getOrders().stream()
                    .map(CustomOrderMapper::managerMapOrderToOrderDTODetailed)
                    .collect(Collectors.toList()));
        }
        deliveryDTO.setScheduledDate(delivery.getScheduledDate());
        deliveryDTO.setRemainingSpace(delivery.getRemainingSpace());
        return deliveryDTO;
    }


}
