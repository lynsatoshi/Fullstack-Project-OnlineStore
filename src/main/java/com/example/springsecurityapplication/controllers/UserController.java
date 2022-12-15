package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartRepository cartRepository;

    @Autowired
    public UserController(ProductRepository productRepository, OrderRepository orderRepository, ProductService productService, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
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

    // работа с заказами

    @GetMapping("/order/create")
    public String createOrder(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();

        // по id получаем всю корзину пользователя из листа. получаем все продукты из заказа.
        List<Cart> cartList = cartRepository.findByPersonId((id_person));
        List<Product> productList = new ArrayList<>();
        for (Cart cart: cartList
        ) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        String uuid = UUID.randomUUID().toString();

        for (Product product: productList
             ) {
            Order newOrder = new Order(uuid, 1, product.getPrice(), Status.Оформлен, product, personDetails.getPerson());
            orderRepository.save(newOrder);
            cartRepository.deleteCartById(product.getId(), id_person);
        }

        return "redirect:/orders";

    }

    // история заказов пользователя
    @GetMapping("/orders")
    public String ordersUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findByPerson((personDetails.getPerson()));
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }

    @PostMapping("index/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("min") String min, @RequestParam("max") String max, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){
//        System.out.println(search);
//        System.out.println(min);
//        System.out.println(max);
//        System.out.println(price);
//        System.out.println(category);
        // если диапозон цен ОТ и ДО не пустой
        if (!min.isEmpty() & !max.isEmpty()) {
            // если сортировка по цене выбрана
            if (!price.isEmpty()) {
                // если в качестве сортировки выбрана сортировка по возрастанию
                if (price.equals("sorted_by_ascending_price")) {

                    // если категория товара не пустая
                    if (!category.isEmpty()) {
                        // если категория равна animal
                        if (category.equals("animal")) {
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
                } else if (price.equals("sorted_by_descending_price")) {

                    if (!category.isEmpty()) {

                        if (category.equals("animal")) {
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
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(min), Float.parseFloat(max)));
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
//        model.addAttribute("products", productService.getAllProduct());
        return "user/index";
    }
}
