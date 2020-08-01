package com.vmollov.techstroe.validation;

import com.vmollov.techstroe.model.binding.UserProfileBindingModel;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.vmollov.techstroe.validation.ValidationConstants.*;

@Component
public class UserProfileEditValidator implements org.springframework.validation.Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileEditValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserProfileBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserProfileBindingModel user = (UserProfileBindingModel) target;

        if (this.userRepository.findByEmail(user.getEmail()).isPresent()){
            User userModel = this.userRepository.findByEmail(user.getEmail()).orElse(null);
            if (userModel != null && !userModel.getUsername().equals(user.getUsername())){
                errors.rejectValue("email", EMAIL_ALREADY_EXISTS_ERROR, EMAIL_ALREADY_EXISTS_ERROR);
            }
        }

        if ((user.getPassword().length() > 0 || user.getConfirmPassword().length() > 0) && !user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("password", PASSWORDS_DONT_MATCH_ERROR, PASSWORDS_DONT_MATCH_ERROR);
            errors.rejectValue("confirmPassword", PASSWORDS_DONT_MATCH_ERROR, PASSWORDS_DONT_MATCH_ERROR);
        }

    }
}