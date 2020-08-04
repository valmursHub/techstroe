package com.vmollov.techstroe.integration;

import com.vmollov.techstroe.model.entity.Address;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.model.service.AddressServiceModel;
import com.vmollov.techstroe.model.view.AddressViewModel;
import com.vmollov.techstroe.service.AddressService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    private AddressService addressService;
//
//    private ModelMapper modelMapper;


    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
    public void testCreate_SuccessfulLoadig() throws Exception {
        mockMvc.perform(get("/addresses/add")).
                andExpect(status().isOk()).
                andExpect(view().name("address-add"));//.
    }

    @Test
    @WithMockUser(username = "admin", authorities={"ROLE_ROOT", "ROLE_ADMIN", "ROLE_USER"})
//    @WithUserDetails()
//    @WithUserDetails(value="customUsername", userDetailsServiceBeanName="myUserDetailsService")
    public void testViewAddresById_SuccessfulLoadig() throws Exception {
//        String id = "63a32d78-073b-4484-864e-eedfb057cf53";
        String id = "1";
//        AddressServiceModel address = new AddressServiceModel();
//        address.setId(id);

//        when(addressService.findAddressById(id)).thenReturn(address);
        mockMvc.perform(get("/addresses/view/{id}", id))
                .andExpect(status().isOk());
//        .andExpect(view().name("address-view"));
    }
}
