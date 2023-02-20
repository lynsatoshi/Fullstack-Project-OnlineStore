package com.example.springsecurityapplication.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 5, max = 50, message = "Логин должен быть от 5 до 50 символов")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", message = "Пароль должен содержать не менее 6 символов, хотя бы одну цифру, спец символ, букву в верхнем и нижнем регистре ")
    private String password;

    @Column(name = "role")
    private String role;

    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    // работа с заказом продукта

    @OneToMany(mappedBy = "person")
    private List<Order> orderList;

    public Person(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
