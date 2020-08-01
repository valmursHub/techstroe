package com.vmollov.techstroe.repository;

import com.vmollov.techstroe.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {

    List<ShoppingCart> findAllByExpiresOn(LocalDate localDate);
}
