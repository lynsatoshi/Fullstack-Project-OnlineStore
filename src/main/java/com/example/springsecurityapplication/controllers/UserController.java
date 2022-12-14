package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final ProductService productService;
    private final CartRepository cartRepository;

    @Autowired
    public UserController(ProductService productService, CartRepository cartRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
    }


    @GetMapping("/index")
    public String index(Model model){
        // Получае объект аутентификации - > c помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутентификации пользователя

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Преобразовываем объект аутентификации в специальный объект класса по работе с пользователями
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        String role = personDetails.getPerson().getRole();

        if (role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
            model.addAttribute("products", productService.getAllProduct());
            return "user/index";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        // извлекаем пользователя из сессии по id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();

        // по id получаем всю корзину пользователя из листа
        List<Cart> cartList = cartRepository.findByPersonId((id_person));
        List<Product> productList = new ArrayList<>();
        for (Cart cart: cartList
             ) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // высчитываем цену за все продукты
        float price = 0;
        for (Product product: productList
             ) {
            price += product.getPrice();
        }

        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList);
        return "user/cart";
    }

    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "product/infoProduct";
    }

    // корзина

    // добавить товар в козрину
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        Product product = productService.getProductId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        int id_person = personDetails.getPerson().getId();
        Cart cart = new Cart(id_person, product.getId());
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    // удалить из корзины
    @GetMapping("/cart/delete/{id}")
    public String deleteProductInCart(@PathVariable("id") int id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();

        // нужен метод, который позволит удалить товар из корзины по определенному условию (-> репозиторий)
        cartRepository.deleteCartById(id, id_person);
        return "redirect:/cart";
    }
}
