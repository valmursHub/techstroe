package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserServiceModel addUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

    UserServiceModel editProfile(UserServiceModel model, String oldPassword);

    List<UserServiceModel> findAllUsers();

    void changeUserRole(String username, String roleName);
}