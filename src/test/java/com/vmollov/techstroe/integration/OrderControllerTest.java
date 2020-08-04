package com.vmollov.techstroe.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", authorities={"ROLE_USER"})
    public void testUserViewOrders_SuccessfulLoadig() throws Exception {
        mockMvc.perform(get("/orders")).
                andExpect(status().isOk()).
                andExpect(view().name("order-all-user")).
                andExpect(model().attributeExists("orders"));
    }

    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
    public void testAdminViewAllOrders_SuccessfulLoadig() throws Exception {
        mockMvc.perform(get("/admin/orders/all")).
                andExpect(status().isOk()).
                andExpect(view().name("order-admin")).
                andExpect(model().attributeExists("orders"));
    }

    @Test
    @WithMockUser(username = "user", authorities={"ROLE_USER"})
    public void testUserViewAllOrders_UnsuccessfulLoadig() throws Exception {
        mockMvc.perform(get("/admin/orders/all")).
                andExpect(status().isOk()).
                andExpect(view().name("errors/error"));
    }

}
