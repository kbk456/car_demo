package com.example.demo.repository;

import com.example.demo.domain.Car;
import com.example.demo.domain.CarCategory;
import com.example.demo.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {

    void deleteByCar(Car car);

    List<CarCategory> findByCategory(Category category);

}
