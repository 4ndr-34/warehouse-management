package com.backend.warehouse_management.controller.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.service.ManagerService;
import com.backend.warehouse_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final OrderService orderService;
    private final ManagerService managerService;
    @GetMapping("/order/all")
    public ResponseEntity<List<OrderDTO>> getAllOrdersGeneralDetail() {
        return new ResponseEntity<>(managerService.managerGetAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getDetailedOrder(@PathVariable("orderId")Long orderId) throws Exception {
        return new ResponseEntity<>(managerService.managerGetDetailedOrder(orderId), HttpStatus.OK);
    }

}
