package com.backend.warehouse_management.controller.client;


import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.service.ClientService;
import com.backend.warehouse_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final OrderService orderService;

    @PostMapping("/order/create/{id}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable("id") Long userId) throws Exception {
        return new ResponseEntity<>(clientService.createOrder(userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/allorders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(orderService.getAllOrdersForClientId(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByStatus(@PathVariable("userId") Long userId, @RequestParam("status") OrderStatus orderStatus) {
        return new ResponseEntity<>(orderService.getOrdersByStatusAndClientId(userId, orderStatus), HttpStatus.OK);
    }

    @PostMapping("{id}/order/addItem")
    public ResponseEntity<OrderDTO> addItemToOrder(@PathVariable("id") Long userId, @RequestBody AddItemToOrderRequest itemRequest) throws Exception {
        return new ResponseEntity<>(clientService.addItemToOrder(userId, itemRequest), HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/item/{itemId}/update")
    public ResponseEntity<OrderDTO> updateItemQuantity(@PathVariable("orderId") Long orderId,@PathVariable("itemId") Long itemId, @RequestParam Integer quantity) throws Exception {
        return new ResponseEntity<>(clientService.updateItemQuantity(orderId, itemId, quantity), HttpStatus.OK);
    }

    @DeleteMapping("/order/{orderId}/item/delete/{itemId}")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) throws Exception {
        return new ResponseEntity<>(clientService.removeItemFromOrder(itemId, orderId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/order/{orderId}/submit")
    public ResponseEntity<OrderDTO> submitOrder(@PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId) throws Exception {
        return new ResponseEntity<>(clientService.submitOrder(userId, orderId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/order/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId) throws Exception {
        return new ResponseEntity<>(clientService.cancelOrder(userId, orderId), HttpStatus.OK);
    }

}
