package com.vmollov.techstroe.integration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
    public void testAdminCreateProduct_SuccessfulLoadig() throws Exception {
        mockMvc.perform(get("/cart")).
                andExpect(status().isOk());
        //TODO userService, shoppingCartService

//                andExpect(view().name("shopping-cart"));
//                andExpect(model().attributeExists("shoppingCartViewModel","total","addresses"));
    }

}
