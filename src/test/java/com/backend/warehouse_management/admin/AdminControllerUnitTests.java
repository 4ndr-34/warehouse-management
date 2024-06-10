package com.backend.warehouse_management.admin;

import com.backend.warehouse_management.controller.admin.AdminController;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    /*@Test
    @Rollback
    void shouldReturnNewUser() throws Exception {
        UserCRUDRequest request = new UserCRUDRequest("Andrea", "Ranxha", "xhimi@gmail.com", "1234", UserRole.CLIENT);
        UserCRUDResponse response = new UserCRUDResponse();
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setRole(request.getRole().toString());

        given()..willReturn();

        when(userService.adminCreateUser(request)).thenReturn(response);
        this.mockMvc.perform(post("/admin/create"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json").equals(
                        """
                                {
                                    "firstName": "Andrea",
                                    "lastName": "Ranxha",
                                    "email": "andrea@gmail.com",
                                    "role": "SYSTEM_ADMIN"
                                }"""));
    }*/


}
