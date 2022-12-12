package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByTitle(String title);

    // поиск по части наименования товара вне зависимости от регистра
    List<Product> findByTitleContainingIgnoreCase(String name);

    // поиск по части наименования товара и фильтрация по диапазону цен
    @Query(value = "select * from product where ((lower(title) like %?1%) or (lower(title) like '?1%') or (lower(title) like '%?1') and (price >= ?2 and price <= ?3))", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThenEqualAndPriceLessThen(String title, float min, float max);

    // поиск по части наименования товара и фильтрация по диапазону цен, сортировка по возрастанию
    @Query(value = "select * from product where ((lower(title) like %?1%) or (lower(title) like '?1%') or (lower(title) like '%?1') and (price >= ?2 and price <= ?3) order by price)", nativeQuery = true)
    List<Product> findByTitleOrderByPrice(String title, float min, float max);

    // поиск по части наименования товара и фильтрация по диапазону цен, сортировка по убыванию
    @Query(value = "select * from product where ((lower(title) like %?1%) or (lower(title) like '?1%') or (lower(title) like '%?1') and (price >= ?2 and price <= ?3) order by price desc)", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float min, float max);

    // поиск по части наименования товара и фильтрация по диапазону цен, сортировка по возрастанию, фильтрация по категории
    @Query(value = "select * from product where category_id=?4 and ((lower(title) like %?1%) or (lower(title) like '?1%') or (lower(title) like '%?1')) and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPrice(String title, float min, float max, int category);

    // поиск по части наименования товара и фильтрация по диапазону цен, сортировка по убыванию, фильтрация по категории
    @Query(value = "select * from product where category_id=?4 and ((lower(title) like %?1%) or (lower(title) like '?1%')) or (lower(title) like '%?1') and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float min, float max, int category);
}
