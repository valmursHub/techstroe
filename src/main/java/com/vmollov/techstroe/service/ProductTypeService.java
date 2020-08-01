package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.ProductTypeServiceModel;

import java.util.List;

public interface ProductTypeService {

    void seedProductTypes();

    ProductTypeServiceModel findProductTypeByName(String name);

    ProductTypeServiceModel findProductTypeById(String id);

    List<ProductTypeServiceModel> findAllTypes();

    List<ProductTypeServiceModel> findAllTypesExceptTheGivenParameter(String productType);
}
