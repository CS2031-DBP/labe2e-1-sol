package org.e2e.labe2e01.driver.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.e2e.labe2e01.vehicle.infrastructure.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DriverControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Driver driver;

    @BeforeEach
    public void setUp() {
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(42.1234);
        coordinate.setLongitude(-71.9876);
        coordinateRepository.save(coordinate);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
        vehicle.setLicensePlate("ABC123");
        vehicleRepository.save(vehicle);

        driver = new Driver();
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setEmail("john.doe@example.com");
        driver.setPassword("password");
        driver.setPhoneNumber("123456789");
        driver.setRole(Role.DRIVER);
        driver.setCoordinate(coordinate);
        driver.setVehicle(vehicle);
        driver.setCreatedAt(ZonedDateTime.now());
        driver.setCategory(Category.X);
        
    }

    @Test
    public void testGetDriver() throws Exception {
        Driver savedDriver = driverRepository.save(driver);

        mockMvc.perform(get("/driver/{id}", savedDriver.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(driver.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(driver.getLastName()));
    }

    @Test
    public void testSaveDriver() throws Exception {

        Driver currentDriver = driverRepository.save(driver);


        mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentDriver)))
                .andExpect(status().isCreated());

        List<Driver> drivers = driverRepository.findAll();
        Assertions.assertEquals(drivers.size(), 1);
        Assertions.assertEquals(drivers.get(0).getFirstName(), currentDriver.getFirstName());
        Assertions.assertEquals(drivers.get(0).getLastName(), currentDriver.getLastName());
    }

    @Test
    public void testDeleteDriver() throws Exception {
        Driver savedDriver = driverRepository.save(driver);

        mockMvc.perform(delete("/driver/{id}", savedDriver.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(driverRepository.existsById(savedDriver.getId()));
    }

    @Test
    public void testUpdateDriver() throws Exception {
        Driver savedDriver = driverRepository.save(driver);

        savedDriver.setFirstName("UpdatedFirstName");
        savedDriver.setLastName("UpdatedLastName");

        mockMvc.perform(put("/driver/{id}", savedDriver.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedDriver)))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(savedDriver.getId()).orElseThrow();
        Assertions.assertEquals(updatedDriver.getFirstName(), "UpdatedFirstName");
        Assertions.assertEquals(updatedDriver.getLastName(), "UpdatedLastName");
    }

    @Test
    public void testUpdateDriverLocation() throws Exception {
        Driver savedDriver = driverRepository.save(driver);

        mockMvc.perform(patch("/driver/{id}/location", savedDriver.getId())
                        .param("latitude", "42.123")
                        .param("longitude", "-71.987"))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(savedDriver.getId()).orElseThrow();
        Assertions.assertEquals(updatedDriver.getCoordinate().getLatitude(), 42.123);
        Assertions.assertEquals(updatedDriver.getCoordinate().getLongitude(), -71.987);
    }

    @Test
    public void testUpdateDriverCar() throws Exception {
        Driver savedDriver = driverRepository.save(driver);

        Vehicle newVehicle = new Vehicle();
        newVehicle.setBrand("Honda");
        newVehicle.setModel("Accord");
        newVehicle.setFabricationYear(2021);
        newVehicle.setCapacity(5);
        newVehicle.setLicensePlate("XYZ123");


        mockMvc.perform(patch("/driver/{id}/car", savedDriver.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newVehicle)))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(savedDriver.getId()).orElseThrow();
        Assertions.assertEquals(updatedDriver.getVehicle().getBrand(), "Honda");
        Assertions.assertEquals(updatedDriver.getVehicle().getModel(), "Accord");
    }
}
