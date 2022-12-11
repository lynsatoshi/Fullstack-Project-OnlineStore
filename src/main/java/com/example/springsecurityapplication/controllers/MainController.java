package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class MainController {
    private final ProductService productService;


    @Autowired
    public MainController(ProductService productService) {
        this.productService = productService;
    }

    // данные метод отображает товары без прохождения аутентификации и авторизации
    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }

    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "product/infoProduct";
    }
}
