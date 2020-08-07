package com.vmollov.techstroe.unit;

import com.cloudinary.Cloudinary;
import com.vmollov.techstroe.model.entity.Product;
import com.vmollov.techstroe.model.entity.ProductType;
import com.vmollov.techstroe.model.service.ProductServiceModel;
import com.vmollov.techstroe.model.service.ProductTypeServiceModel;
import com.vmollov.techstroe.repository.ProductRepository;
import com.vmollov.techstroe.repository.ProductTypeRepository;
import com.vmollov.techstroe.service.CloudinaryService;
import com.vmollov.techstroe.service.ProductService;
import com.vmollov.techstroe.service.ProductTypeService;
import com.vmollov.techstroe.service.impl.CloudinaryServiceImpl;
import com.vmollov.techstroe.service.impl.ProductServiceImpl;
import com.vmollov.techstroe.service.impl.ProductTypeServiceImpl;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Before;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductTypeRepository productTypeRepository;
    private ProductTypeService productTypeService;
    private ProductService productService;
    private CloudinaryService cloudinaryService;
    private ModelMapper modelMapper;
    private Cloudinary cloudinary;

    @Before
    public void setUp() {
        modelMapper = new ModelMapper();
//        cloudinaryService = new CloudinaryServiceImpl(cloudinary);
//        productTypeService = new ProductTypeServiceImpl(productTypeRepository,modelMapper);
        productService = new ProductServiceImpl(productRepository, productTypeService, cloudinaryService, modelMapper);
    }

    @Test
    public void findAllProducts_returnCorrectProducts(){
        Product testProduct = new Product();
        testProduct.setId("id");
        testProduct.setName("Test");
        ProductType productType = new ProductType();
        productType.setName("TestType");
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        Assert.assertEquals(1, productService.findAllProducts().size());

    }

    @Test
    public void findProductById_validID_returnCorrectProduct(){
        Product testProduct = new Product();
        testProduct.setId("id");
        testProduct.setName("Test");
        ProductType productType = new ProductType();
        productType.setName("TestType");
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        Assert.assertEquals(testProduct.getId(), productService.findProductById(testProduct.getId()).getId());

    }

    @Test(expected = NotFoundException.class)
    public void findProductById_invalidID_throws(){
        Product testProduct = new Product();
        testProduct.setId("id");
        testProduct.setName("Test");
        ProductType productType = new ProductType();
        productType.setName("TestType");
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.empty());

        Assert.assertEquals(testProduct.getId(), productService.findProductById(testProduct.getId()).getId());

    }

//    @Test
//    public void createProduct_saveAndFlushCorrectly() throws IOException {
//        Product testProduct = new Product();
//        testProduct.setId("id");
//        testProduct.setName("Test");
//        ProductType productType = new ProductType();
//        productType.setId("productTypeId");
//        productType.setName("TestType");
//        when(productTypeRepository.findById("productTypeId")).thenReturn(Optional.of(productType));
//        when(productTypeService.findProductTypeById("productTypeId")).thenReturn(this.modelMapper.map(productType, ProductTypeServiceModel.class));
//        testProduct.setProductType(this.modelMapper.map(productTypeService.findProductTypeById("productTypeId"),ProductType.class));
//        ProductServiceModel testProductServiceModel = this.modelMapper.map(testProduct,ProductServiceModel.class);
//        String name = "file.txt";
//        String originalFileName = "file.txt";
//        String contentType = "text/plain";
//        byte[] content = null;
//        MultipartFile multipartFile = new MockMultipartFile(name,originalFileName,contentType,content);
//
//        when(productRepository.save(any())).thenReturn(testProduct);
//
//        productService.createProduct(testProductServiceModel,"productTypeId",multipartFile);
//        verify(productRepository).save(any());
//
//    }


}
