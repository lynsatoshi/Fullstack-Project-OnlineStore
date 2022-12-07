package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // метод по получению всех продуктов
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    // метод, возвращающий товар по id
    public Product getProductId(int id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    // метод сохраняет товар (объект), который пришел с формы. переопределяем, потому что уже не read only
    @Transactional
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    // метод по редактированию товара
    @Transactional
    public void updateProduct(int id, Product product){
        product.setId(id);
        productRepository.save(product);
    }

    // удаление товара
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }
}
