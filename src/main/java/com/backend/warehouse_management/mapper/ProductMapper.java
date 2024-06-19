package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.manager.ProductDTO;
import com.backend.warehouse_management.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    Product productDTOToProduct(ProductDTO productDTO);

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productListToProductDTOList(List<Product> products);
}
