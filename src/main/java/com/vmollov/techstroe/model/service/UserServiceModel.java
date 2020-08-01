package com.vmollov.techstroe.model.service;

import com.vmollov.techstroe.model.entity.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {

    private String username;
    private String fullName;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate registeredOn;
    private Set<RoleServiceModel> authorities;
    private ShoppingCartServiceModel shoppingCart;
    private List<AddressServiceModel> addresses;


    public UserServiceModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Set<RoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleServiceModel> authorities) {
        this.authorities = authorities;
    }

    public ShoppingCartServiceModel getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartServiceModel shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<AddressServiceModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressServiceModel> addresses) {
        this.addresses = addresses;
    }
}
