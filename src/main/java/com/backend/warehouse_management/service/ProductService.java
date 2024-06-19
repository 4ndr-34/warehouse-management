package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.manager.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(ProductDTO productDTO, Long id);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();
    void deleteProductById(Long id);

}
