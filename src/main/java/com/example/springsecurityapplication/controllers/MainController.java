package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/index")
    public String index(){
        // Получае объект аутентификации - > c помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутентификации пользователя
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Преобразовываем объект аутентификации в специальный объект класса по работе с пользователями
//        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
        return "index";
    }
}
