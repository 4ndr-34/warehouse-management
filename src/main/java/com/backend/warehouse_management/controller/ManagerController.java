package com.backend.warehouse_management.controller;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.manager.*;
import com.backend.warehouse_management.service.OrderService;
import com.backend.warehouse_management.service.ProductService;
import com.backend.warehouse_management.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manager")
public class ManagerController {


    private final OrderService orderService;
    private final ProductService productService;
    private final TruckService truckService;
    @GetMapping("/order/all")
    public ResponseEntity<List<OrderDTO>> getAllOrdersGeneralDetail() {
        return new ResponseEntity<>(orderService.managerGetAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/getdetails")
    public ResponseEntity<OrderDTO> getDetailedOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(orderService.managerGetDetailedOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/order/approve")
    public ResponseEntity<OrderDTO> approveOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(orderService.managerApproveOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/order/decline")
    public ResponseEntity<OrderDTO> declineOrder(@RequestBody DeclineOrderRequest request) {
        return new ResponseEntity<>(orderService.managerDeclineOrder(request), HttpStatus.OK);
    }

    @PostMapping("/truck/newdelivery")
    public ResponseEntity<DeliveryDTO> newDeliveryForTruck(@RequestBody CreateDeliveryRequest deliveryRequest) {
        return new ResponseEntity<>(orderService.managerCreateDeliveryWithTruck(deliveryRequest), HttpStatus.CREATED);
    }

    @PostMapping("/delivery/addorder")
    public ResponseEntity<DeliveryDTO> addOrderToDelivery(@RequestBody AddOrderToDeliveryRequest request) {
        return new ResponseEntity<>(orderService.managerAddOrderToDelivery(request), HttpStatus.OK);
    }



    //PRODUCT CRUD ENDPOINTS
    @PostMapping("/product/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    @PutMapping("/product/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestParam Long productId, @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.updateProduct(productDTO, productId), HttpStatus.OK);
    }

    @GetMapping("/product/get")
    public ResponseEntity<ProductDTO> getProductById(@RequestParam Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @GetMapping("/product/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @DeleteMapping("/product/delete")
    public ResponseEntity<?> deleteProductById(@RequestParam Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }


    //TRUCK CRUD ENDPOINTS
    @PostMapping("/truck/add")
    public ResponseEntity<TruckDTO> addNewTruck(@RequestBody TruckDTO truckDTO) throws Exception {
        return new ResponseEntity<>(truckService.addTruck(truckDTO), HttpStatus.CREATED);
    }

    @GetMapping("/truck/get")
    public ResponseEntity<TruckDTO> getTruckByLicensePlate(@RequestParam String licensePlate) throws Exception {
        return new ResponseEntity<>(truckService.getTruckByLicensePlate(licensePlate), HttpStatus.OK);
    }

    @PutMapping("/truck/update")
    public ResponseEntity<TruckDTO> updateTruckDetails(@RequestParam Long truckId, @RequestBody TruckDTO truckDTO) throws Exception {
        return new ResponseEntity<>(truckService.editTruckDetails(truckId, truckDTO), HttpStatus.OK);
    }

    @DeleteMapping("/truck/remove")
    public ResponseEntity removeTruck(@RequestParam String licensePlate) throws Exception {
        truckService.removeTruckByLicensePlate(licensePlate);
        return new ResponseEntity(HttpStatus.OK);
    }
}
