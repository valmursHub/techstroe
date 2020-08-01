package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductServiceModel createProduct(ProductServiceModel productServiceModel, String productTypeId, MultipartFile image) throws IOException;

    List<ProductServiceModel> findAllProducts();

    ProductServiceModel findProductById(String id);

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel, MultipartFile image) throws IOException;

    List<ProductServiceModel> findAllNotHiddenProducts();

    List<ProductServiceModel> findAllNotHiddenProductsByType(String productType);

    List<ProductServiceModel> findIndexPageProducts();

    boolean isProductHidden(String productId);
}