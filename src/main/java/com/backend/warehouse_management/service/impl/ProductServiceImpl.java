package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.manager.ProductDTO;
import com.backend.warehouse_management.entity.Product;
import com.backend.warehouse_management.mapper.ProductMapper;
import com.backend.warehouse_management.repository.ProductRepository;
import com.backend.warehouse_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        if(productRepository.findByItemName(productDTO.getItemName()).isEmpty()){
            log.info("saving new product...");
            productRepository.save(
                    productMapper.productDTOToProduct(productDTO));
        } else {
            throw new Exception("A product with this name already exists");
        }

        return productMapper.productToProductDTO(
                productRepository.findByItemName(productDTO.getItemName()).get());
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {

        Product product = productRepository.findById(id).get();
        return productMapper.productToProductDTO(
                productRepository.save(
                        updateProductFromProductDTO(product, productDTO)));

    }

    private Product updateProductFromProductDTO(Product product, ProductDTO productDTO) {
        if(productDTO.getItemName()!= null){
            product.setItemName(productDTO.getItemName());
        }
        if(productDTO.getQuantity() != null) {
            product.setQuantity(productDTO.getQuantity());
        }
        if(productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if(productDTO.getVolume() != null) {
            product.setVolume(productDTO.getVolume());
        }

        return product;
    }

    @Override
    public ProductDTO getProductById(Long id) throws Exception {
        if(!productRepository.findById(id).isPresent()){
            throw new Exception("Product with this ID: " + id + " does not exist");
        }
        else {
            return productMapper.productToProductDTO(
                    productRepository.findById(id).get());
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productMapper.getAllProducts(productRepository.findAll());
    }

    @Override
    public void deleteProductById(Long id) throws Exception {
        if(productRepository.findById(id).isPresent()){
            throw new Exception("User with this ID: " + id + " does not exist");
        }
        else {
            productRepository.deleteById(id);
        }
    }
}
