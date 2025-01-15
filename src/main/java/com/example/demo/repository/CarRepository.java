package com.example.demo.repository;

import com.example.demo.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c JOIN CarCategory cc ON c.id = cc.car.id " +
            "JOIN Category cat ON cc.category.id = cat.id " +
            "WHERE cat.name = :categoryName")
    List<Car> findCarsByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM Car c WHERE " +
            "(:manufacturer IS NULL OR c.manufacturer LIKE %:manufacturer%) AND " +
            "(:model IS NULL OR c.model LIKE %:model%) AND " +
            "(:productionYear = 0 OR c.productionYear = :productionYear)")
    List<Car> findCarsWithDetails(@Param("manufacturer") String manufacturer, @Param("model") String model, @Param("productionYear") int productionYear);

}
