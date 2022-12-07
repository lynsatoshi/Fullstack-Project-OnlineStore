package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {

    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }


    //    @PreAuthorize("hasRole('ROLE_ADMIN') or/and hasRole('')")

    // метод по отображению главной страницы администратора с выводом всех товаров
    @GetMapping()
    public String admin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        String role = personDetails.getPerson().getRole();

        if (role.equals("ROLE_USER")){
            return "redirect:/index";
        }
        model.addAttribute("products", productService.getAllProduct());
        return "admin/admin";
    }

    // метод по заполнению формы для отправки продукта на добавление
    @GetMapping("product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        return "product/addProduct";
    }

    // метод добавления продукта
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product")Product product){
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    // метод по заполнению формы для отправки продукта на редактирование
    @GetMapping("product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("editProduct", productService.getProductId(id));
        return "product/editProduct";
    }

    // метод по отправки изменений продукта
    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("editProduct") Product product, @PathVariable("id") int id){
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    // метод удаления продукта
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
}
