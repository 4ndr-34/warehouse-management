package com.backend.warehouse_management.controller.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.dto.manager.ProductDTO;
import com.backend.warehouse_management.dto.manager.TruckDTO;
import com.backend.warehouse_management.service.ManagerService;
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
    private final ManagerService managerService;
    private final ProductService productService;
    private final TruckService truckService;
    @GetMapping("/order/all")
    public ResponseEntity<List<OrderDTO>> getAllOrdersGeneralDetail() {
        return new ResponseEntity<>(managerService.managerGetAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getDetailedOrder(@PathVariable("orderId")Long orderId) throws Exception {
        return new ResponseEntity<>(managerService.managerGetDetailedOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/approve")
    public ResponseEntity<OrderDTO> approveOrder(@PathVariable("orderId") Long orderId) throws Exception {
        return new ResponseEntity<>(managerService.managerApproveOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/decline")
    public ResponseEntity<OrderDTO> declineOrder(@PathVariable("orderId") Long orderId, @RequestBody String reason) throws Exception {
        return new ResponseEntity<>(managerService.managerDeclineOrder(orderId, reason), HttpStatus.OK);
    }

    @PostMapping("/truck/{truckId}/newdelivery")
    public ResponseEntity<DeliveryDTO> addDeliveryToTruck(@RequestBody CreateDeliveryRequest deliveryRequest, @PathVariable("truckId") Long truckId) throws Exception {
        return new ResponseEntity<>(managerService.managerCreateDeliveryWithTruck(deliveryRequest, truckId), HttpStatus.CREATED);
    }

    @PostMapping("/delivery/{deliveryId}/add")
    public ResponseEntity<DeliveryDTO> addOrderToDelivery(@PathVariable("deliveryId") Long deliveryId, @RequestParam("orderId") Long orderId) throws Exception {
        return new ResponseEntity<>(managerService.managerAddOrderToDelivery(orderId, deliveryId), HttpStatus.OK);
    }

    /*@GetMapping("/delivery/available")
    public ResponseEntity<List<DeliveryDTO>>*/

    //PRODUCT CRUD ENDPOINTS
    @PostMapping("/product/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws Exception {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.updateProduct(productDTO, id), HttpStatus.OK);
    }

    @GetMapping("/product/get/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/product/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) throws Exception {
        productService.deleteProductById(id);
        return new ResponseEntity(HttpStatus.OK);
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

    @PutMapping("/truck/update/{truckId}")
    public ResponseEntity<TruckDTO> updateTruckDetails(@PathVariable("truckId") Long truckId, @RequestBody TruckDTO truckDTO) throws Exception {
        return new ResponseEntity<>(truckService.editTruckDetails(truckId, truckDTO), HttpStatus.OK);
    }

    @DeleteMapping("/truck/remove")
    public ResponseEntity removeTruck(@RequestParam String licensePlate) throws Exception {
        truckService.removeTruckByLicensePlate(licensePlate);
        return new ResponseEntity(HttpStatus.OK);
    }
}
