package com.vmollov.techstroe.web;

import com.vmollov.techstroe.model.binding.ProductAddToCartBindingModel;
import com.vmollov.techstroe.model.binding.ProductCreateBindingModel;
import com.vmollov.techstroe.model.binding.ProductEditBindingModel;
import com.vmollov.techstroe.model.service.ProductServiceModel;
import com.vmollov.techstroe.model.service.ProductTypeServiceModel;
import com.vmollov.techstroe.model.view.ProductTypeViewModel;
import com.vmollov.techstroe.model.view.ProductViewModel;
import com.vmollov.techstroe.service.ProductService;
import com.vmollov.techstroe.service.ProductTypeService;
import com.vmollov.techstroe.validation.ProductCreateValidator;
import com.vmollov.techstroe.validation.ProductEditValidator;
import com.vmollov.techstroe.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("isAuthenticated()")
public class ProductController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final ModelMapper modelMapper;
    private final ProductCreateValidator productCreateValidator;
    private final ProductEditValidator productEditValidator;

    public ProductController(ProductService productService, ProductTypeService productTypeService, ModelMapper modelMapper, ProductCreateValidator productCreateValidator, ProductEditValidator productEditValidator) {
        this.productService = productService;
        this.productTypeService = productTypeService;
        this.modelMapper = modelMapper;
        this.productCreateValidator = productCreateValidator;
        this.productEditValidator = productEditValidator;
    }

    @GetMapping("/admin/products/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "Create Product")
    public ModelAndView create(@ModelAttribute(name = "productCreateBindingModel") ProductCreateBindingModel productCreateBindingModel,
                               @ModelAttribute ProductTypeServiceModel types, ModelAndView modelAndView) {
        List<ProductTypeViewModel> productTypeViewModels = this.productTypeService.findAllTypes()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductTypeViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("types", productTypeViewModels);

        modelAndView.addObject("productCreateBindingModel", productCreateBindingModel);
        modelAndView.setViewName("product-create");

        return modelAndView;
    }

    @PostMapping("/admin/products/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "productCreateBindingModel") ProductCreateBindingModel productCreateBindingModel,
                                      BindingResult bindingResult, ModelAndView modelAndView) throws IOException {

        //TODO REDIRECT WITH "redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productCreateBindingModel", bindingResult);" if have time
        this.productCreateValidator.validate(productCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()){
            modelAndView.addObject("types", this.productTypeService.findAllTypes()
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductTypeViewModel.class))
                    .collect(Collectors.toList()));

            modelAndView.addObject("productCreateBindingModel", productCreateBindingModel);
            modelAndView.setViewName("product-create");
            return modelAndView;
        }

        ProductServiceModel product = this.modelMapper.map(productCreateBindingModel, ProductServiceModel.class);
        this.productService.createProduct(product, productCreateBindingModel.getProductType(), productCreateBindingModel.getImage());

        modelAndView.setViewName("redirect:/admin/products/all");

        return modelAndView;
    }

    @GetMapping("/admin/products/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "All Products")
    public ModelAndView all(ModelAndView modelAndView) {
        modelAndView.setViewName("product-all");

        return modelAndView;
    }

    @GetMapping(value = "/fetch/products", produces = "application/json")
    @ResponseBody
    public Object fetchProducts() {
        return this.productService
                .findAllProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("Duplicates")
    @GetMapping("/admin/products/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(value = "Edit Product")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "productEditBindingModel") ProductEditBindingModel productEditBindingModel) {
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductViewModel productViewModel = this.modelMapper.map(productServiceModel, ProductViewModel.class);
        List<ProductTypeViewModel> types = this.productTypeService
                .findAllTypesExceptTheGivenParameter(productServiceModel.getProductType().getName())
                .stream()
                .map(p -> this.modelMapper.map(p, ProductTypeViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("productViewModel", productViewModel);
        modelAndView.addObject("types", types);
        this.modelMapper.map(productViewModel, productEditBindingModel);
        modelAndView.addObject("productEditBindingModel", productEditBindingModel);
        modelAndView.setViewName("product-edit");

        return modelAndView;
    }


    // TODO CHECK @SuppressWarnings("Duplicates")
    @SuppressWarnings("Duplicates")
    @PostMapping("/admin/products/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editConfirm(@PathVariable String id, @Valid @ModelAttribute(name = "productEditBindingModel") ProductEditBindingModel productEditBindingModel,
                                    BindingResult bindingResult, ModelAndView modelAndView) throws IOException {

        this.productEditValidator.validate(productEditBindingModel, bindingResult);

        if (bindingResult.hasErrors()){
            ProductServiceModel productServiceModel = this.productService.findProductById(id);
            ProductViewModel productViewModel = this.modelMapper.map(productServiceModel, ProductViewModel.class);
            List<ProductTypeViewModel> types = this.productTypeService
                    .findAllTypesExceptTheGivenParameter(productServiceModel.getProductType().getName())
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductTypeViewModel.class))
                    .collect(Collectors.toList());

            modelAndView.addObject("productEditBindingModel", productEditBindingModel);
            modelAndView.addObject("productViewModel", productViewModel);
            modelAndView.addObject("types", types);
            //TODO IF Refresh Browser with filed fields bacouse hire not use redirect and "redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productEditBindingModel", bindingResult);" Trayans Way
            modelAndView.setViewName("product-edit");

            return modelAndView;
        }

        ProductServiceModel productServiceModel = this.modelMapper.map(productEditBindingModel, ProductServiceModel.class);
        ProductTypeServiceModel productType = this.productTypeService.findProductTypeById(productEditBindingModel.getProductType());
        productServiceModel.setProductType(productType);
        this.productService.editProduct(id, productServiceModel, productEditBindingModel.getImage());

        modelAndView.setViewName("redirect:/admin/products/all");

        return modelAndView;
    }

    @GetMapping("/menu")
    @PageTitle(value = "Menu")
    public ModelAndView menu(ModelAndView modelAndView) {
        modelAndView.setViewName("menu");
        return modelAndView;
    }

    //TODO Reviewing thish fetch and JS
    @GetMapping(value = {"/fetch/menu", "/fetch/menu/{type}"}, produces = "application/json")
    @ResponseBody
    public Object fetchMenuItems(@PathVariable Optional<String> type) {
        if (type.isPresent()) {
            return this.productService
                    .findAllNotHiddenProductsByType(type.get())
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                    .collect(Collectors.toList());
        } else {
            return this.productService
                    .findAllNotHiddenProducts()
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                    .collect(Collectors.toList());
        }
    }


    @GetMapping("/products/view/{id}")
    @PageTitle(value = "Product")
    public ModelAndView view(@ModelAttribute(name = "productAddToCartBindingModel") ProductAddToCartBindingModel productAddToCartBindingModel,
                             @PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductViewModel productViewModel = this.modelMapper.map(productServiceModel, ProductViewModel.class);

        modelAndView.addObject("productViewModel", productViewModel);
        modelAndView.addObject("productAddToCartBindingModel", productAddToCartBindingModel);
        modelAndView.setViewName("product-view");

        return modelAndView;
    }
}
