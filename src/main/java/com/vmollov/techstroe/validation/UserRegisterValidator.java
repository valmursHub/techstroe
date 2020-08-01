package com.vmollov.techstroe.validation;

import com.vmollov.techstroe.model.binding.UserRegisterBindingModel;
import com.vmollov.techstroe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.vmollov.techstroe.validation.ValidationConstants.*;

@Component
public class UserRegisterValidator implements org.springframework.validation.Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) target;

        if (this.userRepository.findUserByUsername(userRegisterBindingModel.getUsername()).isPresent()){
            errors.rejectValue("username", USERNAME_ALREADY_EXISTS_ERROR, USERNAME_ALREADY_EXISTS_ERROR);
        }

        if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()){
            errors.rejectValue("email", EMAIL_ALREADY_EXISTS_ERROR, EMAIL_ALREADY_EXISTS_ERROR);
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            errors.rejectValue("password", PASSWORDS_DONT_MATCH_ERROR, PASSWORDS_DONT_MATCH_ERROR);
            errors.rejectValue("confirmPassword", PASSWORDS_DONT_MATCH_ERROR, PASSWORDS_DONT_MATCH_ERROR);
        }
    }
}