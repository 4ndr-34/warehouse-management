package com.backend.warehouse_management.controller.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.manager.ProductDTO;
import com.backend.warehouse_management.service.ManagerService;
import com.backend.warehouse_management.service.OrderService;
import com.backend.warehouse_management.service.ProductService;
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
    @GetMapping("/order/all")
    public ResponseEntity<List<OrderDTO>> getAllOrdersGeneralDetail() {
        return new ResponseEntity<>(managerService.managerGetAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getDetailedOrder(@PathVariable("orderId")Long orderId) throws Exception {
        return new ResponseEntity<>(managerService.managerGetDetailedOrder(orderId), HttpStatus.OK);
    }

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

}
