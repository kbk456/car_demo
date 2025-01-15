package com.example.demo.controller;

import com.example.demo.domain.Car;
import com.example.demo.domain.RentalStatus;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CarService;
import com.example.demo.service.dto.CarDto;
import com.example.demo.service.dto.CarResponseDto;
import com.example.demo.service.dto.CarUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    private final CarRepository carRepository;

    private final CategoryRepository categoryRepository;


    @Operation(summary = "자동차 등록")
    @PostMapping
    public ResponseEntity<CarDto> create(@Valid @RequestBody CarDto carDto) {
        CarDto car = carService.saveCar(carDto);
        return ResponseEntity.ok(car);
    }

    @Operation(summary = "자동차 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id,
                                            @Valid @RequestBody CarUpdateDto carUpdateDto) {
        carService.updateCar(id, carUpdateDto);
        return ResponseEntity.ok("Car updated successfully");
    }

    @Operation(summary = "자동차 대여 가능 여부 조회")
    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Boolean>> checkCarAvailability(@PathVariable Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        boolean isAvailable = car.getStatus() == RentalStatus.AVAILABLE;
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자동차 카테고리 별 조회")
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<CarResponseDto>> getCarsByCategory(@PathVariable String categoryName) {
        categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        List<CarResponseDto> cars = carService.getCarsByCategory(categoryName);
        return ResponseEntity.ok(cars);
    }

    @Operation(summary = "자동차 제조사,모델명,생산년도 조회")
    @GetMapping("/search")
    public ResponseEntity<List<CarResponseDto>> getCarsWithDetails(
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String model,
            @RequestParam(required = false, defaultValue = "0") int productionYear
    ) {

        List<CarResponseDto> carDetails = carService.searchCars(manufacturer, model,productionYear);
        return ResponseEntity.ok(carDetails);
    }

}
