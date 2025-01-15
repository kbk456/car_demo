package com.example.demo.service.dto;

import com.example.demo.domain.CarCategory;
import com.example.demo.domain.RentalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarDto {

    private Long id;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1000, message = "Production year must be a 4-digit number")
    @Max(value = 9999, message = "Production year must be a 4-digit number")
    private Integer productionYear;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "status is required")
    private RentalStatus status;

    @NotEmpty(message = "At least one category is required")
    @Column(nullable = false)
    private List<String> category = new ArrayList<>();

}
