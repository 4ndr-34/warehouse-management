package com.backend.warehouse_management.controller;

import com.backend.warehouse_management.dto.admin.ConfigDTO;
import com.backend.warehouse_management.dto.admin.EditConfigRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.service.AdminConfigService;
import com.backend.warehouse_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminConfigService adminConfigService;
    @PostMapping("/user/create")
    public ResponseEntity<UserCRUDResponse> createUser(@RequestBody UserCRUDRequest userCRUDRequest) throws Exception {
        return new ResponseEntity<>(userService.adminCreateUser(userCRUDRequest), HttpStatus.CREATED);
    }

    @PutMapping("/user/edit")
    public ResponseEntity<UserCRUDResponse> updateUser(@RequestBody UserCRUDRequest userCRUDRequest, @RequestParam Long userId) {
        return new ResponseEntity<>(userService.adminUpdateUser(userCRUDRequest, userId), HttpStatus.OK);
    }

    @GetMapping("/user/get")
    public ResponseEntity<UserCRUDResponse> getUserById(@RequestParam Long userId) {
        return new ResponseEntity<>(userService.adminGetUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUserById(@RequestParam Long userId) {
        userService.adminDeleteUser(userId);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PostMapping("/config/add")
    public ResponseEntity<ConfigDTO> addNewConfig(@RequestBody ConfigDTO configDTO) {
        return new ResponseEntity<>(adminConfigService.adminAddConfig(configDTO), HttpStatus.CREATED);
    }

    @PutMapping("/config/edit")
    public ResponseEntity<ConfigDTO> editConfigValue(@RequestBody EditConfigRequest request) {
        return new ResponseEntity<>(adminConfigService.adminChangeConfigValue(request), HttpStatus.OK);
    }

}
