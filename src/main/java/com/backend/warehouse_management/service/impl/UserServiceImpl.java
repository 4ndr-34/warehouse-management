package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.mapper.UserMapper;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserCRUDResponse adminCreateUser(UserCRUDRequest createUserRequest) throws Exception {
        if(userRepository.findByEmail(createUserRequest.getEmail()).isPresent()){
            throw new Exception("User already exists!");
        } else {
            //TODO - when implementing security, encrypt password
            userRepository.save(userMapper.userRequestToUser(createUserRequest));
        }
        return userMapper.userToUserResponse(userRepository.findByEmail(createUserRequest.getEmail()).get());
    }

    @Override
    public UserCRUDResponse adminUpdateUser(UserCRUDRequest userCRUDRequest, Long id) {
        User user = userRepository.findById(id).get();

        return userMapper.userToUserResponse(
                userRepository.save(
                        updateUserFromUserRequest(userCRUDRequest, user)));
    }

    private User updateUserFromUserRequest(UserCRUDRequest userCRUDRequest, User user) {

        if(userCRUDRequest.getFirstName() != null) {
            user.setFirstName(userCRUDRequest.getFirstName());
        }

        if(userCRUDRequest.getLastName() != null) {
            user.setLastName(userCRUDRequest.getLastName());
        }
        if(userCRUDRequest.getEmail() != null) {
            user.setEmail(userCRUDRequest.getEmail());
        }
        if(userCRUDRequest.getRole() != null) {
            user.setRole(userCRUDRequest.getRole());
        }

        return user;
    }

    @Override
    public UserCRUDResponse adminGetUserById(Long id) {
        User user = userRepository.findById(id).get();
        return userMapper.userToUserResponse(user);
    }

    @Override
    public void adminDeleteUser(Long id) {
        log.info("deleting user with id: " + id);
        userRepository.delete(userRepository.findById(id).get());
    }
}
