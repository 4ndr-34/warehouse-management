package com.backend.warehouse_management.admin;

import com.backend.warehouse_management.dto.admin.UserCRUDRequest;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.enums.UserRole;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.Rollback;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @Rollback
    public void UserService_createUser_ReturnsUserCRUDRequest() throws Exception {

        User user = User.builder()
                .firstName("Andrea")
                .lastName("Ranxha")
                .email("xhimi@gmail.com")
                .password("1234")
                .role(UserRole.CLIENT)
                .build();

        UserCRUDRequest userCRUDRequest = UserCRUDRequest.builder()
                .firstName("Andrea")
                .lastName("Ranxha")
                .email("xhimi@gmail.com")
                .password("1234")
                .role(UserRole.CLIENT)
                .build();


        //Act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        //Assert
        UserCRUDResponse response = userService.adminCreateUser(userCRUDRequest);
        Assertions.assertThat(response).isNotNull();

    }
}
