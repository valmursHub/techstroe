package com.vmollov.techstroe.service.impl;

import com.vmollov.techstroe.model.entity.OrderItem;
import com.vmollov.techstroe.model.entity.Product;
import com.vmollov.techstroe.model.service.OrderItemServiceModel;
import com.vmollov.techstroe.repository.OrderItemRepository;
import com.vmollov.techstroe.service.OrderItemService;
import com.vmollov.techstroe.service.ProductService;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.vmollov.techstroe.constants.Errors.*;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductService productService, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemServiceModel createOrderItem(String productId, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        Product product = this.modelMapper.map(this.productService.findProductById(productId), Product.class);

        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        return this.modelMapper.map(this.orderItemRepository.saveAndFlush(orderItem), OrderItemServiceModel.class);
    }

    @Override
    public OrderItemServiceModel findOrderItemById(String id) {
        OrderItem orderItem = this.orderItemRepository.findById(id).orElseThrow(() -> new NotFoundException(ORDER_ITEM_NOT_FOUND_EXCEPTION));

        return this.modelMapper.map(orderItem, OrderItemServiceModel.class);
    }

    @Override
    public OrderItemServiceModel findOrderItemByProductId(String productId) {
        OrderItem orderItem = this.orderItemRepository.findOrderItemByProduct_Id(productId).orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_EXCEPTION));

        return this.modelMapper.map(orderItem, OrderItemServiceModel.class);
    }

    @Override
    public OrderItemServiceModel updateOrderItem(OrderItemServiceModel orderItemServiceModel) {
        OrderItem orderItem = this.modelMapper.map(orderItemServiceModel, OrderItem.class);
        orderItem = this.orderItemRepository.saveAndFlush(orderItem);

        return this.modelMapper.map(orderItem, OrderItemServiceModel.class);
    }

    @Override
    public void removeOrderItem(OrderItemServiceModel orderItemServiceModel) {
        this.orderItemRepository.deleteById(orderItemServiceModel.getId());
    }
}
