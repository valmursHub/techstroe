package com.vmollov.techstroe.repository;

import com.vmollov.techstroe.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findAllByHiddenIsFalse();

    List<Product> findAllByHiddenIsFalseAndProductType_Name(String productType);

    @Query(value = "SELECT * FROM products p\n" +
            "JOIN product_types pt on p.product_type_id = pt.id\n" +
            "WHERE pt.name IN ('Computer', 'Laptop', 'Server', 'Printer', 'Mfu') AND p.is_hidden = false\n" +
            "ORDER BY RAND()\n" +
            "LIMIT 8", nativeQuery = true)
    List<Product> indexPageProducts();

    Optional<Product> findByName(String name);
}