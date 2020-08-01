package com.vmollov.techstroe.model.service;

import java.math.BigDecimal;

public class ProductServiceModel extends BaseServiceModel {

    private String name;
    private String description;
    private ProductTypeServiceModel productType;
    private BigDecimal price;
    private boolean isHidden;
    private String image;

    public ProductServiceModel() {
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductTypeServiceModel getProductType() {
        return this.productType;
    }

    public void setProductType(ProductTypeServiceModel productType) {
        this.productType = productType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
