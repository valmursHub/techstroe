package com.vmollov.techstroe.repository;

import com.vmollov.techstroe.model.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {

    Optional<ProductType> findByName(String productType);

    List<ProductType> findAllByNameNotOrderByNameAsc(String productType);
}