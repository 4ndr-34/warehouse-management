package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;

public interface UserService {

    //system admin functionalities
    UserCRUDResponse adminCreateUser(UserCRUDRequest createUserRequest) throws Exception;
    UserCRUDResponse adminUpdateUser(UserCRUDRequest userCRUDRequest, Long id);
    UserCRUDResponse adminGetUserById(Long id);
    void adminDeleteUser(Long id);


}
