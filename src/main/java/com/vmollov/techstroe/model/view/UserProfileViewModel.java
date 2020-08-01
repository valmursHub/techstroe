package com.vmollov.techstroe.model.view;

import java.util.List;

public class UserProfileViewModel {

    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<OrderViewModel> orders;
    private List<AddressViewModel> addresses;


    public UserProfileViewModel() {
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

    public List<OrderViewModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderViewModel> orders) {
        this.orders = orders;
    }

    public List<AddressViewModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressViewModel> addresses) {
        this.addresses = addresses;
    }
}
