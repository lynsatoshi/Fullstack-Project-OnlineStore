package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Image;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {

    @Value("/C:/Users/robin/uploads")
    private String uploadPath;

    private final ProductValidator productValidator;

    private final ProductService productService;

    private final CategoryRepository categoryRepository;
    private final OrderService orderService;

    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;

    @Autowired
    public AdminController(ProductValidator productValidator, ProductService productService, CategoryRepository categoryRepository, OrderService orderService, OrderRepository orderRepository, PersonRepository personRepository, PersonService personService) {
        this.productValidator = productValidator;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.personRepository = personRepository;
        this.personService = personService;
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
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    // метод добавления продукта
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five) throws IOException {

// Данная операция проводится для всех добавляемых видов файла (у нас их 5)
//

        productValidator.validate(product, bindingResult);

        // проверка на ошибки валидации
        if (bindingResult.hasErrors()){
            return "product/addProduct";
        }

        // проверка есть ли файл в переменной
        if (file_one != null){
            //оект по хранению пути сохранению
            File uploadDir = new File(uploadPath);
            // если данный путь не существует..
            if (!uploadDir.exists()){
                // ..то мы его создаем
                uploadDir.mkdir();
            }

            // создаем уникальное имя файла
            // UUID представляет неизмененный универсальный уникальный идентификатор
            String uuidFile = UUID.randomUUID().toString();
            // file_one.getOriginalFilename() - наименование файла с формы
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            // загружаем файл по указанному пути
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // сздали объект для листа и далее нужно заполнить его поля
            image.setProduct(product); // в качестве объета берется объект из формы
            image.setFileName(resultFileName); // в качестве наименования берется то именование, которое сгенерировалось из наименования загруженного файла и добавления туда UUID
            product.addImageProduct(image);

        }

//

        if (file_two != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);

        }

        if (file_three != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);

        }

        if (file_four != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);

        }

        if (file_five != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);

        }
//
        productService.saveProduct(product);
        return "redirect:/admin";
    }




    // метод по заполнению формы для отправки продукта на редактирование
    @GetMapping("product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("editProduct", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    // метод по отправки изменений продукта
    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("editProduct") Product product, @PathVariable("id") int id){
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
/*
    // измнение статуса заказа
    @GetMapping("/editStatus/{id}")
    public String editStatus(@PathVariable("id") int id, Model model){
        model.addAttribute("editStatus", orderService.getOrderId(id));
        model.addAttribute("status", Status.values());
        return "admin/editStatus";
    }

    @PostMapping("/editStatus/{id}")
    public String editStatus(@ModelAttribute("editStatus") Order order, @PathVariable("id") int id){
        orderService.updateStatus(order.getStatus(), id);
        return "redirect:/orderList";
    }*/

    // метод удаления продукта
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    // метод вывода заказов всех пользователей
    @GetMapping("orderList")
    public String ordersUsers(Model model){
        model.addAttribute("ordersList", orderRepository.findAll());
        model.addAttribute("personalProduct", productService.getAllProduct());
        return "/admin/orderList";
    }

    // метод вывода списка пользователей
    @GetMapping("userList")
    public String userList(Model model){
        model.addAttribute("userList", personRepository.findAll());
        return "/admin/userList";
    }

    // изменение роли
    @PostMapping("userList")
    public String editStatus(@ModelAttribute("editRole") Person person){
        personRepository.save(person);
        return "redirect:/userList";
    }
}
