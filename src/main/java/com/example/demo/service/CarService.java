package com.example.demo.service;


import com.example.demo.domain.Car;
import com.example.demo.domain.CarCategory;
import com.example.demo.domain.Category;
import com.example.demo.repository.CarCategoryRepository;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.dto.CarDto;
import com.example.demo.service.dto.CarResponseDto;
import com.example.demo.service.dto.CarUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class CarService {

    private final CarRepository carRepository;
    private final CategoryRepository categoryRepository;
    private final CarCategoryRepository carCategoryRepository;

    public CarDto saveCar(CarDto carDto) {
        Car car = new Car();
        car.setManufacturer(carDto.getManufacturer());
        car.setModel(carDto.getModel());
        car.setProductionYear(carDto.getProductionYear());
        car.setStatus(carDto.getStatus());

        Car upateCar = updateCategory(car, carDto.getCategory());

        Car savedCar = carRepository.save(upateCar);

        CarDto savedCarDto = new CarDto();
        savedCarDto.setId(savedCar.getId());
        savedCarDto.setManufacturer(savedCar.getManufacturer());
        savedCarDto.setModel(savedCar.getModel());
        savedCarDto.setProductionYear(savedCar.getProductionYear());
        savedCarDto.setStatus(savedCar.getStatus());

        List<String> categories = savedCar.getCarCategories().stream()
                .map(carCategory -> carCategory.getCategory().getName())
                .collect(Collectors.toList());
        savedCarDto.setCategory(categories);

        return savedCarDto;
    }

    public void updateCar(Long id, @Valid CarUpdateDto carUpdateDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        if (carUpdateDto.getStatus() != null) {
            car.setStatus(carUpdateDto.getStatus());
        }
        if (carUpdateDto.getCategory() != null) {
            car.getCarCategories().clear();
            carCategoryRepository.deleteByCar(car);
            updateCategory(car, carUpdateDto.getCategory());
        }

        carRepository.save(car);
    }

    public Car updateCategory(Car car, List<String> categories) {
        Set<CarCategory> carCategories = new HashSet<>();

        for (String categoryName : categories) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> categoryRepository.findByName(categoryName)
                            .orElseGet(() -> {
                                Category newCategory = new Category();
                                newCategory.setName(categoryName);
                                return categoryRepository.save(newCategory);
                            }));

            CarCategory carCategory = new CarCategory();
            carCategory.setCar(car);
            carCategory.setCategory(category);
            carCategories.add(carCategory);
        }
        car.setCarCategories(carCategories);
        return car;
    }

    public List<CarResponseDto> getCarsByCategory(String categoryName) {
        List<Car> cars = carRepository.findCarsByCategoryName(categoryName);
        return getCarResponseDtos(cars);

    }

    private List<CarResponseDto> getCarResponseDtos(List<Car> cars) {
        return cars.stream().map(car -> {
            String categoryNamesString = car.getCarCategories().stream()
                    .map(carCategory -> carCategory.getCategory().getName())
                    .collect(Collectors.joining(","));
            return new CarResponseDto(car.getId(), car.getManufacturer(), car.getModel(), car.getProductionYear(), car.getStatus(), categoryNamesString);
        }).collect(Collectors.toList());
    }

    public List<CarResponseDto> searchCars(String manufacturer, String model, int productionYear) {
        List<Car> cars = carRepository.findCarsWithDetails(manufacturer, model, productionYear);
        return getCarResponseDtos(cars);
    }
}
