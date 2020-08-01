package com.vmollov.techstroe.web;

import com.vmollov.techstroe.model.binding.ProductAddToCartBindingModel;
import com.vmollov.techstroe.model.service.ShoppingCartServiceModel;
import com.vmollov.techstroe.model.service.UserServiceModel;
import com.vmollov.techstroe.model.view.AddressViewModel;
import com.vmollov.techstroe.model.view.OrderItemViewModel;
import com.vmollov.techstroe.model.view.ShoppingCartViewModel;
import com.vmollov.techstroe.service.ShoppingCartService;
import com.vmollov.techstroe.service.UserService;
import com.vmollov.techstroe.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ModelMapper modelMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cart")
    @PageTitle(value = "Shopping Cart")
    public ModelAndView shoppingCart(ModelAndView modelAndView, Principal principal) {

        //TODO Reviewing this work - detailed !!!

        UserServiceModel user = this.userService.findUserByUsername(principal.getName());
        ShoppingCartServiceModel shoppingCartServiceModel = this.shoppingCartService.findShoppingCartById(user.getShoppingCart().getId());
        ShoppingCartViewModel shoppingCartViewModel = this.modelMapper.map(shoppingCartServiceModel, ShoppingCartViewModel.class);

        shoppingCartViewModel.setOrderItems(shoppingCartServiceModel.getOrderItems()
                .stream()
                .map(oi -> this.modelMapper.map(oi, OrderItemViewModel.class))
                .collect(Collectors.toList()));

        BigDecimal total = shoppingCartViewModel.getOrderItems()
                .stream()
                .map(oi -> oi.getProduct().getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<AddressViewModel> addresses = user.getAddresses()
                .stream()
                .map(a -> this.modelMapper.map(a, AddressViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("shoppingCartViewModel", shoppingCartViewModel.getOrderItems());
        modelAndView.addObject("total", total);
        modelAndView.addObject("addresses", addresses);
        modelAndView.setViewName("shopping-cart");

        return modelAndView;
    }

    @PostMapping("/cart/add/")
    public ModelAndView addToShoppingCart(@Valid @ModelAttribute(name = "productAddToCartBindingModel") ProductAddToCartBindingModel productAddToCartBindingModel,
                                          BindingResult bindingResult, ModelAndView modelAndView, Principal principal) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("productAddToCartBindingModel", productAddToCartBindingModel);
            modelAndView.setViewName("redirect:/products/view/" + productAddToCartBindingModel.getId());
            return modelAndView;
        }

        UserServiceModel user = this.userService.findUserByUsername(principal.getName());
        this.shoppingCartService.addToShoppingCart(productAddToCartBindingModel.getId(),
                productAddToCartBindingModel.getQuantity(), user.getShoppingCart().getId());

        modelAndView.setViewName("redirect:/cart");

        return modelAndView;
    }

    @GetMapping("/cart/remove/{id}")
    public ModelAndView removeOrderItem(@PathVariable String id, ModelAndView modelAndView, Principal principal) {
        UserServiceModel user = this.userService.findUserByUsername(principal.getName());
        this.shoppingCartService.removeOrderItem(id, user.getShoppingCart().getId());

        modelAndView.setViewName("redirect:/cart");

        return modelAndView;
    }
}
