package com.vmollov.techstroe.unit;


import com.vmollov.techstroe.model.binding.UserRegisterBindingModel;
import com.vmollov.techstroe.model.entity.ShoppingCart;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.model.service.RoleServiceModel;
import com.vmollov.techstroe.model.service.ShoppingCartServiceModel;
import com.vmollov.techstroe.model.service.UserServiceModel;
import com.vmollov.techstroe.repository.ProductRepository;
import com.vmollov.techstroe.repository.RoleRepository;
import com.vmollov.techstroe.repository.ShoppingCartRepository;
import com.vmollov.techstroe.repository.UserRepository;
import com.vmollov.techstroe.service.ShoppingCartService;
import com.vmollov.techstroe.service.UserService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTests {


    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ProductRepository productRepository;

    private ShoppingCartService shoppingCartService;
    private UserService userService;
    private ShoppingCart shoppingCart;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Before
    public void setup(){
        modelMapper = new ModelMapper();
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, modelMapper, roleRepository, bCryptPasswordEncoder, shoppingCartService);
    }

    @Test
    public void loadUserByUsernameValidUserReturnCorrectUser(){
        String username = "Test";

        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        Assert.assertEquals(user, userService.loadUserByUsername(username));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameWithNoValidUserReturnError(){
        String username = "Test";

        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assert.assertEquals(user, userService.loadUserByUsername(username));
    }

    @Test
    public void findAllUsersReturnsUsersCorrectUsers(){
        User user = new User();
        user.setUsername("Test");
        User user1 = new User();
        user1.setUsername("Test1");

        when(userRepository.findAll()).thenReturn(List.of(user, user1));

        Assert.assertEquals(2, userService.findAllUsers().size());
    }

    @Test(expected = AssertionError.class)
    public void findAllUsersReturnsIncorrectUsersAndThrowError(){
        User user = new User();
        user.setUsername("Test");
        User user1 = new User();
        user1.setUsername("Test1");

        when(userRepository.findAll()).thenReturn(List.of(user));

        Assert.assertEquals(2, userService.findAllUsers().size());
    }

    @Test
    public void findUserByUsernameReturnCorrectUser(){
        String username = "Test";

        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        Assert.assertEquals(user.getUsername(), userService.findUserByUsername(username).getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findUserByUsernameReturnIncorrectUserAndThrowError(){
        String username = "Test";

        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assert.assertEquals(user.getUsername(), userService.findUserByUsername(username).getUsername());
    }

//    @Test
//    public void userService_registerUserWithCorrectValues_ReturnsCorrect() {
//
//        UserService userService = new UserServiceImpl(this.userRepository, this.modelMapper,this.roleRepository,this.bCryptPasswordEncoder, this.shoppingCartService);
//
//        UserRegisterBindingModel model = new UserRegisterBindingModel();
//
//        model.setUsername("User 1");
//        model.setPassword("userpass");
//        model.setConfirmPassword("userpass");
//        model.setFullName("Test");
//        model.setEmail("user@gmail.com");
//        model.setPhoneNumber("123");
//        UserServiceModel userServiceModel = this.modelMapper.map(model,UserServiceModel.class);
//
//        userServiceModel.setRegisteredOn(LocalDate.now());
//        RoleServiceModel rsm = new RoleServiceModel();
//        rsm.setAuthority("ROLE_ADMIN");
//        userServiceModel.setAuthorities(Set.of(rsm));
//        ShoppingCart shoppingCart = new ShoppingCart();
//        shoppingCart.setId("1");
//        shoppingCart.setExpiresOn(LocalDate.now());
//        shoppingCartRepository.save(shoppingCart);
//
//        userServiceModel.setShoppingCart(this.modelMapper.map(shoppingCart, ShoppingCartServiceModel.class));
//
//        userService.addUser(userServiceModel);
//
//        User expected = this.userRepository.findAll().get(0);
//
//        Assert.assertEquals(model.getUsername(), expected.getUsername());
//        Assert.assertEquals(model.getEmail(), expected.getEmail());
////        Assert.assertEquals(4, expected.getAuthorities().size());
//    }


}
