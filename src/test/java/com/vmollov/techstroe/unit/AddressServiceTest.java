package com.vmollov.techstroe.unit;

import com.vmollov.techstroe.model.entity.Address;
import com.vmollov.techstroe.model.entity.ShoppingCart;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.model.service.AddressServiceModel;
import com.vmollov.techstroe.model.service.UserServiceModel;
import com.vmollov.techstroe.repository.AddressRepository;
import com.vmollov.techstroe.repository.RoleRepository;
import com.vmollov.techstroe.repository.UserRepository;
import com.vmollov.techstroe.service.AddressService;
import com.vmollov.techstroe.service.ShoppingCartService;
import com.vmollov.techstroe.service.UserService;
import com.vmollov.techstroe.service.impl.AddressServiceImpl;
import com.vmollov.techstroe.service.impl.UserServiceImpl;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    private AddressService addressService;
//    private ShoppingCartService shoppingCartService;
//    private UserService userService;
//    private ShoppingCart shoppingCart;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Before
    public void setUp() {
        modelMapper = new ModelMapper();
        addressService = new AddressServiceImpl(addressRepository, userRepository, modelMapper);
//        userService = new UserServiceImpl(userRepository, modelMapper, roleRepository, bCryptPasswordEncoder, shoppingCartService);
    }

    @Test
    public void findAddressById_returnCorrectAddress(){

        Address testAddres = new Address();
        String addrId = "1";
        testAddres.setId(addrId);
        testAddres.setName("TestAddressName");
        testAddres.setCity("TestCity");
        testAddres.setAddress("TestAddress");
        testAddres.setPhoneNumber("032");
        testAddres.setNotes("Note");

        when(addressRepository.findById(addrId)).thenReturn(Optional.of(testAddres));

        Assert.assertEquals(testAddres.getName(),addressService.findAddressById(addrId).getName());
    }

    @Test(expected = NotFoundException.class)
    public void findAddressById_invalidID_throws(){

        Address testAddres = new Address();
        String addrId = "1";

        when(addressRepository.findById(addrId)).thenReturn(Optional.empty());

        addressService.findAddressById(addrId);
    }

}

