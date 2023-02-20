package com.example.springsecurityapplication.models;

import com.example.springsecurityapplication.enumm.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;

    private int count;

    private float price;

    private LocalDateTime dateTime;

    private Status status;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Person person;

    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public Order(String number, int count, float price, Status status, Product product, Person person) {
        this.number = number;
        this.count = count;
        this.price = price;
        this.status = status;
        this.product = product;
        this.person = person;
    }
}
