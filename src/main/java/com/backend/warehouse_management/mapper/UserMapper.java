package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.entity.User;
import org.mapstruct.*;

@Mapper
public interface UserMapper {

    User userRequestToUser(UserCRUDRequest userCRUDRequest);

    UserCRUDResponse userToUserResponse(User user);
}
