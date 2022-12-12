package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class MainController {
    private final ProductService productService;
    private final ProductRepository productRepository;


    @Autowired
    public MainController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
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

    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("min") String min, @RequestParam("max") String max, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){
        // если диапозон цен ОТ и ДО не пустой
        if (!min.isEmpty() & !max.isEmpty()){
            // если сортировка по цене выбрана
            if (!price.isEmpty()){
                // если в качестве сортировки выбрана сортировка по возрастанию
                if (price.equals("sorted_by_ascending_price")){

                    // если категория товара не пустая
                    if (!category.isEmpty()){
                            // если категория равна animal
                        if (category.equals("animal")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 1));
                        } else if (category.equals("feed")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 2));
                        } else if (category.equals("terrarium")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 3));
                        } else if (category.equals("equipment")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 4));
                        } else if (category.equals("decor")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 5));
                        }

                        // если категория не выбрана
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
                    }

                    // если сортировка выбрана по убыванию
                } else if (price.equals("sorted_by_descending_price")){

                    if (!category.isEmpty()){

                        if (category.equals("animal")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 1));
                        } else if (category.equals("feed")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 2));
                        } else if (category.equals("terrarium")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 3));
                        } else if (category.equals("equipment")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 4));
                        } else if (category.equals("decor")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max), 5));
                        }

                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPrice(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
                    }
                }
            } else {
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThenEqualAndPriceLessThen(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
            }

        } else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }
        model.addAttribute("value_search", search);
        model.addAttribute("value_min", min);
        model.addAttribute("value_max", max);
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }
}
