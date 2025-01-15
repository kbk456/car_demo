package com.example.demo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;

    private String model;

    private int productionYear;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @NotNull
    @Column(nullable = false)
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<CarCategory> carCategories = new HashSet<>();

}
