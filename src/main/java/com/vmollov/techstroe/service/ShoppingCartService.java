package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.OrderItemServiceModel;
import com.vmollov.techstroe.model.service.ShoppingCartServiceModel;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCartServiceModel createShoppingCart();

    ShoppingCartServiceModel findShoppingCartById(String id);

    void addToShoppingCart(String productId, Integer quantity, String shoppingCartId);

    void removeOrderItem(String orderItemId, String shoppingCartId);

    void removeOrderItems(List<OrderItemServiceModel> orderItemServiceModels, String shoppingCartId);
}
