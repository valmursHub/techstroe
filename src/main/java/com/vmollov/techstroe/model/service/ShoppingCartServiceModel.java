package com.vmollov.techstroe.model.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ShoppingCartServiceModel extends BaseServiceModel {

    private List<OrderItemServiceModel> orderItems;
    private LocalDate expiresOn;

    public ShoppingCartServiceModel() {
        this.orderItems = new LinkedList<>();
    }

    public List<OrderItemServiceModel> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<OrderItemServiceModel> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDate getExpiresOn() {
        return this.expiresOn;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }
}
