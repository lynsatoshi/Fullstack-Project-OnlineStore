package com.example.springsecurityapplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImgConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    // addResourceHandlers предназначен для раздачи ресурсов. Этот метод предназначен для раздачи информации, например при указании пути нужно брать файл из определенной папки.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // регистрируем добавление ресорсов и указываем что добавление данных ресурсов будет идти по данному пути
        // ** - какой-либо текст
        registry.addResourceHandler("/img/**").addResourceLocations("file://" + uploadPath + "/"); // извлечение из директории uploadPath

    }
}
