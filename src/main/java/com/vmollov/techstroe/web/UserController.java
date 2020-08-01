package com.vmollov.techstroe.web;

import com.vmollov.techstroe.model.binding.UserLoginBindingModel;
import com.vmollov.techstroe.model.binding.UserProfileBindingModel;
import com.vmollov.techstroe.model.binding.UserRegisterBindingModel;
import com.vmollov.techstroe.model.service.UserServiceModel;
import com.vmollov.techstroe.model.view.OrderViewModel;
import com.vmollov.techstroe.model.view.UserProfileViewModel;
import com.vmollov.techstroe.model.view.UserViewModel;
import com.vmollov.techstroe.service.OrderService;
import com.vmollov.techstroe.service.UserService;
import com.vmollov.techstroe.validation.UserProfileEditValidator;
import com.vmollov.techstroe.validation.UserRegisterValidator;
import com.vmollov.techstroe.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserProfileEditValidator userProfileEditValidator;


    public UserController(UserService userService, ModelMapper modelMapper, OrderService orderService, UserRegisterValidator userRegisterValidator, UserProfileEditValidator userProfileEditValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.userRegisterValidator = userRegisterValidator;
        this.userProfileEditValidator = userProfileEditValidator;
    }

    @GetMapping("/login")
    @PageTitle("login")
    public String login(){
        return "login";
    }

    @PostMapping("/login-error")
    public ModelAndView onLoginError(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.
                    SPRING_SECURITY_FORM_USERNAME_KEY) String username
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", "bad.credentials");
        modelAndView.addObject("username", username);
        modelAndView.setViewName("login");

        return modelAndView;
    }


    @GetMapping("/register")
    @PageTitle("register")
    public String register(Model model){

        if (!model.containsAttribute("userRegisterBindingModel")){
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }

        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel")UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){

        this.userRegisterValidator.validate(userRegisterBindingModel, bindingResult);

        if (bindingResult.hasErrors() || !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){

            redirectAttributes.addFlashAttribute("userRegisterBindingModel" ,userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:register";
        }
        this.userService
                .addUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

//        return "login";
        return "redirect:login";
    }

    //TODO Admin to view and change role of all Users
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "Users")
    public ModelAndView users(@AuthenticationPrincipal Principal principal, ModelAndView modelAndView ) {
        //TODO Da popitam dali e dobre da vrushtem derectno "principal" ili e xybavo da go trensveriram
        // " UserProfileViewModel user = this.modelMapper.map(userPrincipal, UserProfileViewModel.class);" kakto po dolu
        modelAndView.addObject("username", principal);
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @GetMapping(value = "/fetch/users", produces = "application/json")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object fetchUsers() {
        System.out.println();
        return this.userService
                .findAllUsers()
                .stream()
                .map(u -> {
                    Set<String> roles = new HashSet<>();
                    u.getAuthorities().forEach(r -> roles.add(r.getAuthority()));

                    UserViewModel model = this.modelMapper.map(u, UserViewModel.class);
                    model.setRoles(roles);
                    System.out.println();

                    return model;
                })
                .collect(Collectors.toList());

    }

    @GetMapping("/admin/user/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "User")
    public ModelAndView profileViewAdmin(@PathVariable String username, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        UserProfileViewModel user = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("user-view-admin");

        return modelAndView;
    }

    @GetMapping("/admin/user/{username}/change-role/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "Change Role")
    public ModelAndView userChangeRole(@PathVariable String username, @PathVariable String role, ModelAndView modelAndView) {

        this.userService.changeUserRole(username, role);
        System.out.println();
        modelAndView.setViewName("redirect:/users/admin/users");

        return modelAndView;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle(value = "Profile")
    public ModelAndView profile(ModelAndView modelAndView, Principal principal) {
        UserDetails userPrincipal = this.userService.loadUserByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(userPrincipal, UserProfileViewModel.class);

        List<OrderViewModel> recentOrders = this.orderService.findRecentOrdersByUsername(principal.getName()).stream().map(o -> this.modelMapper.map(o, OrderViewModel.class)).collect(Collectors.toList());

        modelAndView.addObject("user", userProfileViewModel);
        modelAndView.addObject("orders", recentOrders);
        modelAndView.setViewName("user-profile");

        return modelAndView;
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle(value = "Edit Profile")
    public ModelAndView profileEdit(ModelAndView modelAndView,
                                    @ModelAttribute(name = "userProfileBindingModel") UserProfileBindingModel userProfileBindingModel, Principal principal) {

        UserDetails userPrincipal = this.userService.loadUserByUsername(principal.getName());
        UserProfileViewModel user = this.modelMapper.map(userPrincipal, UserProfileViewModel.class);

        this.modelMapper.map(user, userProfileBindingModel);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userProfileBindingModel", userProfileBindingModel);
        modelAndView.setViewName("user-profile-edit");

        return modelAndView;
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profileEditConfirm(@Valid @ModelAttribute(name = "userProfileBindingModel") UserProfileBindingModel userProfileBindingModel,
                                           BindingResult bindingResult, ModelAndView modelAndView, Principal principal) {
        userProfileBindingModel.setUsername(principal.getName());
        this.userProfileEditValidator.validate(userProfileBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("userProfileBindingModel", userProfileBindingModel);
            UserDetails userPrincipal = this.userService.loadUserByUsername(principal.getName());
            UserProfileViewModel user = this.modelMapper.map(userPrincipal, UserProfileViewModel.class);
            modelAndView.addObject("user", user);
            modelAndView.setViewName("user-profile-edit");
            return modelAndView;
        }

        this.userService.editProfile(this.modelMapper.map(userProfileBindingModel, UserServiceModel.class), userProfileBindingModel.getOldPassword());
        modelAndView.setViewName("redirect:/users/profile");

        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }
}