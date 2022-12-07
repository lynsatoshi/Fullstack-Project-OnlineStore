package com.example.springsecurityapplication.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {

//    @PreAuthorize("hasRole('ROLE_ADMIN') or/and hasRole('')")
    @GetMapping()
    public String admin(){
        return "admin/admin";
    }
}
