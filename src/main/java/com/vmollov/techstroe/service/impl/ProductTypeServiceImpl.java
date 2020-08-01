package com.vmollov.techstroe.service.impl;

import com.vmollov.techstroe.model.entity.ProductType;
import com.vmollov.techstroe.model.service.ProductTypeServiceModel;
import com.vmollov.techstroe.repository.ProductTypeRepository;
import com.vmollov.techstroe.service.ProductTypeService;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.vmollov.techstroe.constants.Errors.*;
import static com.vmollov.techstroe.constants.GlobalConstants.*;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository, ModelMapper modelMapper) {
        this.productTypeRepository = productTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedProductTypes(){
        if (this.productTypeRepository.findAll().isEmpty()){
            String[] products = {
                    PRODUCT_CATEGORY_PC,
                    PRODUCT_CATEGORY_LAPTOP,
                    PRODUCT_CATEGORY_SERVER,
                    PRODUCT_CATEGORY_PRINTER,
                    PRODUCT_CATEGORY_MFU
            };

            for (String product : products) {
                ProductType productType = new ProductType();
                productType.setName(product);

                this.productTypeRepository.saveAndFlush(productType);
            }
        }
    }

    @Override
    public ProductTypeServiceModel findProductTypeByName(String name) {
        return this.productTypeRepository
                .findByName(name)
                .map(t -> this.modelMapper.map(t, ProductTypeServiceModel.class))
                .orElseThrow(() -> new NotFoundException(PRODUCT_TYPE_NOT_FOUND_EXCEPTION));
    }

    @Override
    public List<ProductTypeServiceModel> findAllTypes() {
        return this.productTypeRepository
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductTypeServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductTypeServiceModel findProductTypeById(String id) {
        ProductType productType = this.productTypeRepository.findById(id).orElseThrow(() -> new NotFoundException(PRODUCT_TYPE_NOT_FOUND_EXCEPTION));
        return this.modelMapper.map(productType, ProductTypeServiceModel.class);
    }

    @Override
    public List<ProductTypeServiceModel> findAllTypesExceptTheGivenParameter(String productType) {
        return this.productTypeRepository
                .findAllByNameNotOrderByNameAsc(productType)
                .stream()
                .map(p -> this.modelMapper.map(p, ProductTypeServiceModel.class))
                .collect(Collectors.toList());
    }
}
