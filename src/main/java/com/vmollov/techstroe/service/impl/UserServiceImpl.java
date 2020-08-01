package com.vmollov.techstroe.service.impl;

import com.vmollov.techstroe.model.entity.ShoppingCart;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.model.service.ShoppingCartServiceModel;
import com.vmollov.techstroe.model.service.UserServiceModel;
import com.vmollov.techstroe.repository.RoleRepository;
import com.vmollov.techstroe.repository.UserRepository;
import com.vmollov.techstroe.service.ShoppingCartService;
import com.vmollov.techstroe.service.UserService;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import com.vmollov.techstroe.web.errors.exceptions.ServiceGeneralException;
import com.vmollov.techstroe.web.errors.exceptions.UnauthorizedActionException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.vmollov.techstroe.constants.Errors.*;
import static com.vmollov.techstroe.constants.GlobalConstants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ShoppingCartService shoppingCartService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ShoppingCartService shoppingCartService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.shoppingCartService = shoppingCartService;
    }


    @Override
    public UserServiceModel addUser(UserServiceModel userServiceModel) {

        User user = this.modelMapper.map(userServiceModel, User.class);

        if (userRepository.count() == 0) {

            user.setAuthorities(this.roleRepository.findAll().stream().collect(Collectors.toSet()));

        } else {
            user.setAuthorities(new LinkedHashSet<>());
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER").orElseThrow(() -> new NotFoundException(USER_ROLE_NOT_FOUND_EXCEPTION)));
        }

        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        user.setRegisteredOn(LocalDate.now());
        ShoppingCartServiceModel shoppingCart = this.shoppingCartService.createShoppingCart();
        user.setShoppingCart(this.modelMapper.map(shoppingCart, ShoppingCart.class));
        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User " + s + " not found!"));
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository
                .findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void changeUserRole(String username, String roleName) {

        if (USER_ROLE_ROOT.equals(roleName)){
            throw new UnauthorizedActionException(CANNOT_MODIFY_ROLE_EXCEPTION);
        }

        UserServiceModel userServiceModel = this.findUserByUsername(username);
        User user = this.modelMapper.map(userServiceModel, User.class);

            if (USER_ROLE_ADMIN.equals(roleName)){
                user.getAuthorities().clear();
                user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN").orElseThrow(() -> new NotFoundException(USER_ROLE_NOT_FOUND_EXCEPTION)));
                user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER").orElseThrow(() -> new NotFoundException(USER_ROLE_NOT_FOUND_EXCEPTION)));
            }else if (USER_ROLE_USER.equals(roleName)){
                user.getAuthorities().clear();
                user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER").orElseThrow(() -> new NotFoundException(USER_ROLE_NOT_FOUND_EXCEPTION)));
            }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserServiceModel editProfile(UserServiceModel model, String oldPassword) {
        User user = this.userRepository.findByUsername(model.getUsername()).orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND_EXCEPTION));

        if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ServiceGeneralException(WRONG_PASSWORD_EXCEPTION);
        }

        user.setPassword(!"".equals(model.getPassword()) ? this.bCryptPasswordEncoder.encode(model.getPassword()) : user.getPassword());
        user.setEmail(model.getEmail());
        user.setFullName(model.getFullName());
        user.setPhoneNumber(model.getPhoneNumber());

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }
}
