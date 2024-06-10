package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.manager.ProductDTO;
import com.backend.warehouse_management.entity.Product;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO) throws Exception;

    ProductDTO updateProduct(ProductDTO productDTO, Long id);

    ProductDTO getProductById(Long id) throws Exception;

    List<ProductDTO> getAllProducts();
    void deleteProductById(Long id) throws Exception;

}
