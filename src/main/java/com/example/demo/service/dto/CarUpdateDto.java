package com.example.demo.service.dto;

import com.example.demo.domain.RentalStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CarUpdateDto {

    private RentalStatus status;

    private List<String> category;
}
