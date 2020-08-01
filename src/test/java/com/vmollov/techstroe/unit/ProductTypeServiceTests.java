package com.vmollov.techstroe.unit;

import com.vmollov.techstroe.model.entity.ProductType;
import com.vmollov.techstroe.repository.ProductTypeRepository;
import com.vmollov.techstroe.service.ProductTypeService;
import com.vmollov.techstroe.service.impl.ProductTypeServiceImpl;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class) //Zamestva @RunWith(MockitoJUnitRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductTypeServiceTests {

    @Mock
    private ProductTypeRepository productTypeRepository;

    private ModelMapper modelMapper;
    private ProductTypeService productTypeService;

    @Before
    public void setUp(){
        modelMapper = new ModelMapper();
        productTypeService = new ProductTypeServiceImpl(productTypeRepository, modelMapper);
    }

    @Test
    public void findProductTypeByName_validName_returnProductType(){
        ProductType productType = new ProductType();
        productType.setName("Test");

        when(productTypeRepository.findByName(productType.getName())).thenReturn(java.util.Optional.of(productType));

        Assert.assertEquals(productType.getName(), productTypeService.findProductTypeByName(productType.getName()).getName());
    }

    @Test(expected = NotFoundException.class)
    public void findProductTypeByName_invalidName_throws(){
        ProductType productType = new ProductType();
        productType.setName("Test");

        when(productTypeRepository.findByName(productType.getName())).thenReturn(Optional.empty());

        productTypeService.findProductTypeByName(productType.getName());
    }

    @Test
    public void findAllTypes_valid_correct(){
        ProductType productType = new ProductType();
        productType.setName("Test");

        when(productTypeRepository.findAll()).thenReturn(List.of(productType));

        Assert.assertEquals(1, productTypeService.findAllTypes().size());
    }

    @Test
    public void findProductTypeById_validId_returnsType(){
        ProductType productType = new ProductType();
        productType.setId("uu-id");
        productType.setName("Test");

        when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));

        Assert.assertEquals(productType.getId(), productTypeService.findProductTypeById(productType.getId()).getId());
    }

    @Test(expected = NotFoundException.class)
    public void findProductTypeById_invalidId_throws(){
        ProductType productType = new ProductType();
        productType.setId("uu-id");
        productType.setName("Test");

        when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.empty());

        productTypeService.findProductTypeById(productType.getId());
    }
}