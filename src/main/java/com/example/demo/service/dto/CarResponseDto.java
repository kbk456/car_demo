package com.example.demo.service.dto;

import com.example.demo.domain.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CarResponseDto {
    private Long id;
    private String manufacturer;
    private String model;
    private int productionYear;
    private RentalStatus status;
    private String categoryNames;

}
