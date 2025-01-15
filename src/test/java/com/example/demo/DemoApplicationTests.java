package com.example.demo;

import com.example.demo.domain.Car;
import com.example.demo.domain.CarCategory;
import com.example.demo.domain.Category;
import com.example.demo.domain.RentalStatus;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CarService;
import com.example.demo.service.dto.CarDto;
import com.example.demo.service.dto.CarResponseDto;
import com.example.demo.service.dto.CarUpdateDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class DemoApplicationTests {
    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void testGetCarsByCategory() {
        // Given
        String categoryName = "Sedan";

        Category sedanCategory = new Category();
        sedanCategory.setName(categoryName);

        Car car = new Car();
        car.setId(1L);
        car.setManufacturer("Toyota");
        car.setModel("Camry");
        car.setProductionYear(2022);
        car.setStatus(RentalStatus.AVAILABLE);

        CarCategory carCategory = new CarCategory();
        carCategory.setCar(car);
        carCategory.setCategory(sedanCategory);

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        when(carRepository.findCarsByCategoryName(categoryName)).thenReturn(cars);

        // When
        List<CarResponseDto> carResponseDtos = carService.getCarsByCategory(categoryName);

        // Then
        assertNotNull(carResponseDtos);
        assertEquals(1, carResponseDtos.size());

        CarResponseDto carResponseDto = carResponseDtos.get(0);
        assertEquals("Toyota", carResponseDto.getManufacturer());
        assertEquals("Camry", carResponseDto.getModel());
        assertEquals(2022, carResponseDto.getProductionYear());
        assertEquals(RentalStatus.AVAILABLE, carResponseDto.getStatus());
    }

    @Test
    public void testSearchCars() {
        // Given
        String manufacturer = "Toyota";
        String model = "Camry";
        int productionYear = 2022;

        Category sedanCategory = new Category();
        sedanCategory.setName("Sedan");

        Car car = new Car();
        car.setId(1L);
        car.setManufacturer(manufacturer);
        car.setModel(model);
        car.setProductionYear(productionYear);
        car.setStatus(RentalStatus.AVAILABLE);

        CarCategory carCategory = new CarCategory();
        carCategory.setCar(car);
        carCategory.setCategory(sedanCategory);

        car.setCarCategories(new HashSet<>(Collections.singletonList(carCategory)));

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        when(carRepository.findCarsWithDetails(manufacturer, model, productionYear)).thenReturn(cars);

        // When
        List<CarResponseDto> carResponseDtos = carService.searchCars(manufacturer, model, productionYear);

        // Then
        assertNotNull(carResponseDtos);
        assertEquals(1, carResponseDtos.size());

        CarResponseDto carResponseDto = carResponseDtos.get(0);
        assertEquals(manufacturer, carResponseDto.getManufacturer());
        assertEquals(model, carResponseDto.getModel());
        assertEquals(productionYear, carResponseDto.getProductionYear());
        assertEquals(RentalStatus.AVAILABLE, carResponseDto.getStatus());
        assertEquals("Sedan", carResponseDto.getCategoryNames());
    }

    @Test
    public void testSaveCar() {
        // Given
        CarDto carDto = new CarDto();
        carDto.setManufacturer("Toyota");
        carDto.setModel("Corolla");
        carDto.setProductionYear(2023);
        carDto.setStatus(RentalStatus.AVAILABLE);
        carDto.setCategory(Arrays.asList("Sedan", "Hybrid"));

        Category sedanCategory = new Category();
        sedanCategory.setName("Sedan");

        Category hybridCategory = new Category();
        hybridCategory.setName("Hybrid");

        Car car = new Car();
        car.setManufacturer("Toyota");
        car.setModel("Corolla");
        car.setProductionYear(2023);
        car.setStatus(RentalStatus.AVAILABLE);

        CarCategory sedanCarCategory = new CarCategory();
        sedanCarCategory.setCar(car);
        sedanCarCategory.setCategory(sedanCategory);

        CarCategory hybridCarCategory = new CarCategory();
        hybridCarCategory.setCar(car);
        hybridCarCategory.setCategory(hybridCategory);

        car.setCarCategories(new HashSet<>(Arrays.asList(sedanCarCategory, hybridCarCategory)));

        Car savedCar = new Car();
        savedCar.setId(1L);
        savedCar.setManufacturer("Toyota");
        savedCar.setModel("Corolla");
        savedCar.setProductionYear(2023);
        savedCar.setStatus(RentalStatus.AVAILABLE);
        savedCar.setCarCategories(new HashSet<>(Arrays.asList(sedanCarCategory, hybridCarCategory)));

        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        // When
        CarDto savedCarDto = carService.saveCar(carDto);

        // Then
        assertNotNull(savedCarDto);
        assertEquals(1L, savedCarDto.getId());
        assertEquals("Toyota", savedCarDto.getManufacturer());
        assertEquals("Corolla", savedCarDto.getModel());
        assertEquals(2023, savedCarDto.getProductionYear());
        assertEquals(RentalStatus.AVAILABLE, savedCarDto.getStatus());
        assertTrue(savedCarDto.getCategory().contains("Sedan"));
        assertTrue(savedCarDto.getCategory().contains("Hybrid"));
    }

    @Test
    public void testUpdateCar_StatusChanged() {
        // Given
        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setManufacturer("Toyota");
        existingCar.setModel("Corolla");
        existingCar.setProductionYear(2023);
        existingCar.setStatus(RentalStatus.AVAILABLE);

        CarUpdateDto carUpdateDto = new CarUpdateDto();
        carUpdateDto.setStatus(RentalStatus.REPAIR);

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));

        // When
        carService.updateCar(carId, carUpdateDto);

        // Then
        assertEquals(RentalStatus.REPAIR, existingCar.getStatus());
        verify(carRepository, Mockito.times(1)).save(existingCar);
    }


    @Test
    public void testUpdateCar_NotFound() {
        // Given
        Long carId = 1L;
        CarUpdateDto carUpdateDto = new CarUpdateDto();
        carUpdateDto.setStatus(RentalStatus.AVAILABLE);

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            carService.updateCar(carId, carUpdateDto);
        });
    }

    @Test
    public void testUpdateCategory_NewCategory() {
        // Given
        Car car = new Car();
        car.setId(1L);
        car.setManufacturer("Toyota");
        car.setModel("Corolla");

        List<String> categories = Arrays.asList("SUV", "Sedan");

        Category suvCategory = new Category();
        suvCategory.setName("SUV");

        Category sedanCategory = new Category();
        sedanCategory.setName("Sedan");

        when(categoryRepository.findByName("SUV")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Sedan")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Car updatedCar = carService.updateCategory(car, categories);

        // Then
        assertEquals(2, updatedCar.getCarCategories().size());
        Set<String> categoryNames = updatedCar.getCarCategories().stream()
                .map(carCategory -> carCategory.getCategory().getName())
                .collect(Collectors.toSet());

        assertTrue(categoryNames.contains("SUV"));
        assertTrue(categoryNames.contains("Sedan"));
        verify(categoryRepository, Mockito.times(2)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory_ExistingCategory() {
        // Given
        Car car = new Car();
        car.setId(1L);
        car.setManufacturer("Toyota");
        car.setModel("Corolla");

        List<String> categories = Arrays.asList("SUV", "Sedan");

        Category suvCategory = new Category();
        suvCategory.setName("SUV");

        Category sedanCategory = new Category();
        sedanCategory.setName("Sedan");

        when(categoryRepository.findByName("SUV")).thenReturn(Optional.of(suvCategory));
        when(categoryRepository.findByName("Sedan")).thenReturn(Optional.of(sedanCategory));

        // When
        Car updatedCar = carService.updateCategory(car, categories);

        // Then
        assertEquals(2, updatedCar.getCarCategories().size());
        Set<String> categoryNames = updatedCar.getCarCategories().stream()
                .map(carCategory -> carCategory.getCategory().getName())
                .collect(Collectors.toSet());

        assertTrue(categoryNames.contains("SUV"));
        assertTrue(categoryNames.contains("Sedan"));
        verify(categoryRepository, Mockito.times(0)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory_CategoryEmpty() {
        // Given
        Car car = new Car();
        car.setId(1L);
        car.setManufacturer("Toyota");
        car.setModel("Corolla");

        List<String> categories = new ArrayList<>();

        // When
        Car updatedCar = carService.updateCategory(car, categories);

        // Then
        assertTrue(updatedCar.getCarCategories().isEmpty());
    }

}
