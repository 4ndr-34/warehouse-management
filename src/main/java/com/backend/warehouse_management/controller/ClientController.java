package com.backend.warehouse_management.controller;


import com.backend.warehouse_management.dto.client.*;
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

    private final OrderService orderService;

    @PostMapping("/neworder")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam Long userId) throws Exception {
        return new ResponseEntity<>(orderService.clientCreateOrder(userId), HttpStatus.CREATED);
    }

    @GetMapping("/allorders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestParam Long userId) {
        return new ResponseEntity<>(orderService.getAllOrdersForClientId(userId), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByStatus(@RequestBody GetOrdersByStatusRequest request) {
        return new ResponseEntity<>(orderService.getOrdersByStatusAndClientId(request), HttpStatus.OK);
    }

    @PostMapping("/addItem")
    public ResponseEntity<OrderDTO> addItemToOrder(@RequestBody AddItemToOrderRequest itemRequest) {
        return new ResponseEntity<>(orderService.clientAddItemToOrder(itemRequest), HttpStatus.OK);
    }

    @PostMapping("/updateitem")
    public ResponseEntity<OrderDTO> updateItemQuantity(@RequestBody UpdateOrderItemRequest request) {
        return new ResponseEntity<>(orderService.clientUpdateItemQuantity(request), HttpStatus.OK);
    }

    @DeleteMapping("/removeitem")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@RequestBody RemoveOrderItemRequest request) {
        return new ResponseEntity<>(orderService.clientRemoveItemFromOrder(request), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<OrderDTO> submitOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(orderService.clientSubmitOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(orderService.clientCancelOrder(orderId), HttpStatus.OK);
    }



}
