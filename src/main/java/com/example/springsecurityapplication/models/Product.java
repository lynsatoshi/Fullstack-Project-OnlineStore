package com.example.springsecurityapplication.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена не может быть отрицательной или нулевой")
    @NotNull(message = "Цена товара не может быть пустой")
    private float price;

    @Column(name = "provider", nullable = false)
    @NotEmpty(message = "Поле поставщика не может быть пустым")
    private String provider;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    List<Image> imageList = new ArrayList<>(); // изображение будет храниться в листе, поэтому был создан объект в файле AdminController

    @ManyToOne(optional = false)
    private Category category;

    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    // работа с заказом продукта

    @OneToMany(mappedBy = "product")
    private List<Order> orderList;

    private LocalDateTime dataTimeOfCreated;

    // будет заполняться дата и время при создании объекта класса
    @PrePersist
    private void init(){
        dataTimeOfCreated = LocalDateTime.now();
    }

    // в этом методе получаем объет фотографии, который будет помещет в лист фотографий
    public void addImageProduct(Image image){
        image.setProduct(this); // указываем что работаеи с текущим продуктом
        imageList.add(image); // и добавляем этот объект в лист
    }
}
