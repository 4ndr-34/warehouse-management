package com.backend.warehouse_management.controller;

import com.backend.warehouse_management.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderCannotBeProcessedException.class)
    public ResponseEntity<?> handleOrderCannotBeProcessedException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order cannot be processed!");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested object is not found");
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleItemAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("This item already exists");
    }

    @ExceptionHandler(DeliveryDateException.class)
    public ResponseEntity<?> handleDeliveryDateException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot process a delivery for this date!");
    }

    @ExceptionHandler(NoRemainingSpaceException.class)
    public ResponseEntity<?> handleNoRemainingSpaceException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no more space for this order");
    }

    @ExceptionHandler(ProductQuantityException.class)
    public ResponseEntity<?> handleProductQuantityException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested quantity is bigger than the remaining for this product");
    }

}
