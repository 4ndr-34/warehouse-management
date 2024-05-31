package com.backend.warehouse_management.controller;

import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserCRUDResponse> createUser(@RequestBody UserCRUDRequest userCRUDRequest) throws Exception {
        return new ResponseEntity<>(userService.adminCreateUser(userCRUDRequest), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserCRUDResponse> updateUser(@RequestBody UserCRUDRequest userCRUDRequest, @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.adminUpdateUser(userCRUDRequest,id ), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserCRUDResponse> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.adminGetUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        userService.adminDeleteUser(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


}
