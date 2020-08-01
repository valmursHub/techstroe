package com.vmollov.techstroe.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginPage_BeforeLogin() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isOk()).
                andExpect(view().name("login"));//.
    }

    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
    public void testLoginPage_SuccessLogin() throws Exception {
        mockMvc.perform(get("/menu")).
                andExpect(status().isOk()).
                andExpect(view().name("menu"));//.
//                andExpect(model().attributeExists("requestCount", "startedOn"));
    }

    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
    public void testLoginPage_AdminSuccessLogin() throws Exception {
        mockMvc.perform(get("/users/admin/users")).
                andExpect(status().isOk()).
                andExpect(view().name("users"));//.
//                andExpect(model().attributeExists("requestCount", "startedOn"));
    }

//    @Test
//    @WithMockUser(username = "user", authorities={"ROLE_USER"})
//    public void testLoginPage_AccessDeniedForNormalUser() throws Exception {
//        mockMvc.perform(get("/users/admin/users")).
//                andExpect(status().isForbidden());//.
////                andExpect(view().name("users"));//.
////                andExpect(model().attributeExists("requestCount", "startedOn"));
//    }

}
