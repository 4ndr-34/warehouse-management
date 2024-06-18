package com.backend.warehouse_management.controller.client;


import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;
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


    //client/2/allorders
    @GetMapping("/{id}/allorders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(orderService.getAllOrdersForClientId(userId), HttpStatus.OK);
    }
    //client/2/orders
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByStatus(@PathVariable("userId") Long userId, @RequestParam("status") OrderStatus orderStatus) {
        return new ResponseEntity<>(orderService.getOrdersByStatusAndClientId(userId, orderStatus), HttpStatus.OK);
    }

    @PostMapping("{id}/order/addItem")
    public ResponseEntity<OrderDTO> addItemToOrder(@PathVariable("id") Long userId, @RequestBody AddItemToOrderRequest itemRequest) throws Exception {
        return new ResponseEntity<>(clientService.addItemToOrder(userId, itemRequest), HttpStatus.OK);
    }


    @PostMapping("/updateitem")
    public ResponseEntity<OrderDTO> updateItemQuantity(@RequestBody UpdateOrderItemRequest request) throws Exception {
        return new ResponseEntity<>(clientService.updateItemQuantity(request), HttpStatus.OK);
    }

    //client/order/3/item/delete/4
    @DeleteMapping("/removeitem")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@RequestBody RemoveOrderItemRequest request) throws Exception {
        return new ResponseEntity<>(clientService.removeItemFromOrder(request), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<OrderDTO> submitOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(clientService.submitOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(clientService.cancelOrder(orderId), HttpStatus.OK);
    }

/*    @PutMapping("order/{oderId}")
    updateOrder(@RequestBody OrderDTO order)*/

}
