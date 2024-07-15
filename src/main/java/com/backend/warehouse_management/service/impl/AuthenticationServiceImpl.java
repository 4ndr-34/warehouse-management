package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.AuthenticationRequest;
import com.backend.warehouse_management.dto.AuthenticationResponse;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.dto.client.UserRegisterRequest;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.exception.AlreadyExistsException;
import com.backend.warehouse_management.mapper.CustomUserMapper;
import com.backend.warehouse_management.mapper.UserMapper;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.security.JwtHelper;
import com.backend.warehouse_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserCRUDResponse register(UserRegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) {
            throw new AlreadyExistsException();
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(hashedPassword);

        return userMapper.userToUserResponse(userRepository.save(CustomUserMapper.registerRequestToUser(registerRequest)));
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch(BadCredentialsException e) {
            throw e;
        }
        String token = JwtHelper.generateToken(authenticationRequest.getEmail());
        return new AuthenticationResponse(authenticationRequest.getEmail(), token);
    }
}
