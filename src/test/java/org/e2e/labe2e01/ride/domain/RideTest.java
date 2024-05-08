package org.e2e.labe2e01.ride.domain;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RideTest {

    private Ride ride;
    private Driver driver;
    private Passenger passenger;
    private Coordinate origin;
    private Coordinate destination;
    private Vehicle vehicle;

    void setUpVehicle() {
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
    }

    void setUpCoordinates() {
        origin = new Coordinate(1.0, 1.0);
        destination = new Coordinate(2.0, 2.0);
    }

    void setUpDriver() {
        driver = new Driver();
        driver.setRole(Role.DRIVER);
        driver.setFirstName("Juan");
        driver.setLastName("Perez");
        driver.setEmail("juanperez@gmail.com");
        driver.setPassword("123456");
        driver.setPhoneNumber("987654321");
        driver.setCategory(Category.X);
        driver.setVehicle(vehicle);

        Coordinate coordinate = new Coordinate(40.730610, -73.935242);
        driver.setCoordinate(coordinate);
    }

    void setUpPassenger() {
        passenger = new Passenger();
        passenger.setFirstName("Pablo");
        passenger.setLastName("Gonzalez");
        passenger.setEmail("pablogonzalez@gmail.com");
        passenger.setPassword("123456");
        passenger.setPhoneNumber("123456789");
        passenger.setCreatedAt(ZonedDateTime.now());
    }

    void setUpRide() {
        ride = new Ride();
        ride.setOriginName("UTEC");
        ride.setDestinationName("Plaza San Miguel");
        ride.setPrice(10.0);
        ride.setStatus(Status.REQUESTED);
        ride.setDepartureDate(LocalDateTime.now());
        ride.setArrivalDate(LocalDateTime.now().plusHours(1));
        ride.setOriginCoordinates(origin);
        ride.setDestinationCoordinates(destination);
        ride.setDriver(driver);
        ride.setPassenger(passenger);
    }

    @BeforeEach
    void setUp() {
        setUpVehicle();
        setUpCoordinates();
        setUpDriver();
        setUpPassenger();
        setUpRide();
    }

    @Test
    void testRideCreation() {
        assertEquals("UTEC", ride.getOriginName());
        assertEquals("Plaza San Miguel", ride.getDestinationName());
        assertEquals(10.0, ride.getPrice());
        assertEquals(Status.REQUESTED, ride.getStatus());
        assertNotNull(ride.getDepartureDate());
        assertNotNull(ride.getArrivalDate());
        assertEquals(origin, ride.getOriginCoordinates());
        assertEquals(destination, ride.getDestinationCoordinates());
        assertEquals(driver, ride.getDriver());
        assertEquals(passenger, ride.getPassenger());
    }
}
