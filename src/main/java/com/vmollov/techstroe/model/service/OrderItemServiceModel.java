package com.vmollov.techstroe.model.service;

public class OrderItemServiceModel extends BaseServiceModel {

    private ProductServiceModel product;
    private int quantity;

    public OrderItemServiceModel() {
    }

    public ProductServiceModel getProduct() {
        return this.product;
    }

    public void setProduct(ProductServiceModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
