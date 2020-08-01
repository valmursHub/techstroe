package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

//    OrderServiceModel createOrder(String username, String addressId) throws MessagingException;
    OrderServiceModel createOrder(String username, String addressId);

    List<OrderServiceModel> findRecentOrdersByUsername(String username);

    List<OrderServiceModel> findAllOrdersByUsername(String username);

    OrderServiceModel findOrderById(String id);

    List<OrderServiceModel> findTodaysOrders();

    List<OrderServiceModel> findAllOrders();

    void orderFinish(String orderId);

    List<OrderServiceModel> findAllNotFinishedOrderByUsername(String username);

}