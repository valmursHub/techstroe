package com.vmollov.techstroe.web;


import com.vmollov.techstroe.model.binding.AddressCreateBindingModel;
import com.vmollov.techstroe.model.service.AddressServiceModel;
import com.vmollov.techstroe.model.view.AddressViewModel;
import com.vmollov.techstroe.service.AddressService;
import com.vmollov.techstroe.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressController(AddressService addressService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PageTitle(value = "Create Address")
    public ModelAndView create(ModelAndView modelAndView, @ModelAttribute(name = "addressCreateBindingModel") AddressCreateBindingModel addressCreateBindingModel){
        modelAndView.addObject("addressCreateBindingModel", addressCreateBindingModel);
        modelAndView.setViewName("address-add");

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "addressCreateBindingModel") AddressCreateBindingModel addressCreateBindingModel,
                                      BindingResult bindingResult, ModelAndView modelAndView, Principal principal){
        if (bindingResult.hasErrors()){
            modelAndView.addObject("addressCreateBindingModel", addressCreateBindingModel);
            modelAndView.setViewName("address-add");
            return modelAndView;
        }

        this.addressService.createAddress(this.modelMapper.map(addressCreateBindingModel, AddressServiceModel.class), principal.getName());

        modelAndView.setViewName("redirect:/users/profile");

        return modelAndView;
    }

    @GetMapping("/view/{id}")
    @PageTitle(value = "Address")
    public ModelAndView view(@PathVariable String id, @ModelAttribute(name = "addressCreateBindingModel") AddressCreateBindingModel addressCreateBindingModel,
                             ModelAndView modelAndView, Principal principal){

        if (!this.addressService.userHasAddress(id, principal.getName())){
            modelAndView.setViewName("redirect:/profile");

            return modelAndView;
        }

        AddressViewModel addressViewModel = this.modelMapper.map(this.addressService.findAddressById(id), AddressViewModel.class);

        this.modelMapper.map(addressViewModel, addressCreateBindingModel);

        modelAndView.addObject("address", addressViewModel);
        modelAndView.addObject("addressCreateBindingModel", addressCreateBindingModel);
        modelAndView.setViewName("address-view");

        return modelAndView;
    }

    @PostMapping("/view/{id}")
    public ModelAndView editConfirm(@PathVariable String id, @Valid @ModelAttribute(name = "addressCreateBindingModel") AddressCreateBindingModel addressCreateBindingModel,
                                    BindingResult bindingResult, ModelAndView modelAndView, Principal principal){
        System.out.println();
        if (!this.addressService.userHasAddress(id, principal.getName())){
            modelAndView.setViewName("redirect:/profile");

            return modelAndView;
        }

        if (bindingResult.hasErrors()){
            modelAndView.addObject("addressCreateBindingModel", addressCreateBindingModel);
            modelAndView.addObject("address", this.modelMapper.map(this.addressService.findAddressById(id), AddressViewModel.class));
            modelAndView.setViewName("address-view");
            return modelAndView;
        }

        AddressServiceModel newAddress = this.modelMapper.map(addressCreateBindingModel, AddressServiceModel.class);
        newAddress.setId(id);

        this.addressService.updateAddress(newAddress, principal.getName());

        modelAndView.setViewName("redirect:/users/profile");

        return modelAndView;
    }

    @GetMapping("/view/{id}/delete")
    @PageTitle(value = "Delete Address")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView, Principal principal){
        if (!this.addressService.userHasAddress(id, principal.getName())){
            modelAndView.setViewName("redirect:/users/profile");

            return modelAndView;
        }

        this.addressService.deleteAddress(id, principal.getName());

        modelAndView.setViewName("redirect:/users/profile");

        return modelAndView;
    }


}
