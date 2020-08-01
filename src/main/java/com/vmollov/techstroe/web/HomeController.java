package com.vmollov.techstroe.web;

import com.vmollov.techstroe.model.view.ProductViewModel;
import com.vmollov.techstroe.service.ProductService;
import com.vmollov.techstroe.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public HomeController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/")
//    public String index(){
//        return "index";
//    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("index")
    public ModelAndView home(ModelAndView modelAndView){
        List<ProductViewModel> products = this.productService
                .findIndexPageProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("products", products);
        modelAndView.setViewName("index");

        return modelAndView;
    }
}
