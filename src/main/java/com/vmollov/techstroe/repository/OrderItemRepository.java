package com.vmollov.techstroe.repository;

import com.vmollov.techstroe.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    Optional<OrderItem> findOrderItemByProduct_Id(String productId);
}

