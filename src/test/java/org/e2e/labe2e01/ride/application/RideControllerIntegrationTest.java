package org.e2e.labe2e01.ride.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.domain.Status;
import org.e2e.labe2e01.ride.infrastructure.RideRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RideControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Driver driver;
    
    private Passenger passenger;

    private Ride ride;

    @BeforeEach
    public void setUp() {


        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(42.1234);
        coordinate.setLongitude(-71.9876);

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

        passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setEmail("passenger@example.com");
        passenger.setPassword("password");
        passenger.setPhoneNumber("123456789");
        passenger.setRole(Role.PASSENGER);
        passenger.setCreatedAt(ZonedDateTime.now());

        ride = new Ride();
        ride.setDriver(driver);
        ride.setPassenger(passenger);
        ride.setDepartureDate(ZonedDateTime.now().toLocalDateTime());
        ride.setArrivalDate(ZonedDateTime.now().plusHours(1).toLocalDateTime());
        ride.setDestinationName("Destination");
        ride.setOriginName("Origin");
        ride.setStatus(Status.COMPLETED);
        ride.setPrice(100.0);

    }

    @Test
    public void passengerBookRideAndReturnOk() throws Exception {

        driverRepository.save(driver);
        passengerRepository.save(passenger);
        Ride currentRide = rideRepository.save(ride);

        mockMvc.perform(post("/ride")
                        .content(objectMapper.writeValueAsString(currentRide))
                        .contentType("application/json"))
                        .andExpect(status().isOk());

        Ride updatedRide = rideRepository.findAll().get(0);
        Assertions.assertEquals(ride.getDriver().getEmail(), updatedRide.getDriver().getEmail());
        Assertions.assertEquals(ride.getPassenger().getEmail(), updatedRide.getPassenger().getEmail());
        Assertions.assertEquals(ride.getDepartureDate(), updatedRide.getDepartureDate());

    }

    @Test
    public void updateRideStatusToAcceptedAndReturnOk() throws Exception{
        Ride currentRide = new Ride();
        currentRide.setDriver(driver);
        currentRide.setPassenger(passenger);
        currentRide.setDepartureDate(ZonedDateTime.now().toLocalDateTime());
        currentRide.setArrivalDate(ZonedDateTime.now().plusHours(1).toLocalDateTime());
        currentRide.setDestinationName("Destination");
        currentRide.setOriginName("Origin");
        currentRide.setStatus(Status.REQUESTED);
        currentRide.setPrice(100.0);

        currentRide = rideRepository.save(currentRide);

        mockMvc.perform(patch("/ride/assign/{rideId}", currentRide.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
        );

        Ride updatedRide = rideRepository.findById(currentRide.getId()).orElseThrow(() -> new RuntimeException("Ride not found"));
        Assertions.assertEquals(Status.ACCEPTED, updatedRide.getStatus());
    }

    @Test
    public void updateRideStatusToCanceledAndReturnNoContent() throws Exception{
        Ride currentRide = rideRepository.save(ride);

        mockMvc.perform(delete("/ride/{rideId}", currentRide.getId()))
                .andExpect(status().isNoContent());

        Ride updatedRide = rideRepository.findById(currentRide.getId()).orElseThrow();
        Assertions.assertEquals(Status.CANCELLED, updatedRide.getStatus());
    }


    @Test
    public void getRidesByUserAndReturnPageOfRides() throws Exception{

        Passenger currentPassenger= passengerRepository.save(passenger);
        Driver currentDriver = driverRepository.save(driver);

        ride.setDriver(currentDriver);
        ride.setPassenger(currentPassenger);

        Ride currentRide = rideRepository.save(ride);

        mockMvc.perform(get("/ride/{id}", currentRide.getPassenger().getId())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(currentRide.getId()))
                .andExpect(status().isOk());
    }
}
