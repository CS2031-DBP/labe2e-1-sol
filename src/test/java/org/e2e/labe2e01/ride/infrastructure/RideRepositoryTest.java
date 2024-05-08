package org.e2e.labe2e01.ride.infrastructure;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.domain.Status;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger passenger;
    private Driver driver;

    @BeforeEach
    public void setUp() {
        passenger = new Passenger();
        passenger.setFirstName("Jane");
        passenger.setLastName("Doe");
        passenger.setEmail("jane@example.com");
        passenger.setPassword("password");
        passenger.setPhoneNumber("0987654321");
        passenger.setRole(Role.PASSENGER);
        passenger.setCreatedAt(ZonedDateTime.now());
        entityManager.persist(passenger);

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setBrand("Mercedes-Benz");
        vehicle1.setModel("GLB 200");
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setFabricationYear(2020);
        vehicle1.setCapacity(5);
        entityManager.persist(vehicle1);

        Coordinate coordinate = new Coordinate(40.730610, -73.935242);

        driver = new Driver();
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setEmail("john@example.com");
        driver.setPassword("password");
        driver.setPhoneNumber("1234567890");
        driver.setCategory(Category.X);
        driver.setVehicle(vehicle1);
        driver.setRole(Role.DRIVER);
        driver.setCreatedAt(ZonedDateTime.now());
        driver.setCoordinate(coordinate);
        entityManager.persist(driver);

        setupAndPersistTestRide(Status.REQUESTED);
        setupAndPersistTestRide(Status.ACCEPTED);
        setupAndPersistTestRide(Status.COMPLETED);
    }

    private void setupAndPersistTestRide(Status status) {
        Coordinate origin = new Coordinate(10.0 + Math.random() * 20, 20.0 + Math.random() * 20);
        Coordinate destination = new Coordinate(30.0 + Math.random() * 20, 40.0 + Math.random() * 20);
        entityManager.persist(origin);
        entityManager.persist(destination);

        Ride ride = new Ride();
        ride.setPassenger(passenger);
        ride.setDriver(driver);
        ride.setOriginCoordinates(origin);
        ride.setDestinationCoordinates(destination);
        ride.setOriginName("Origin");
        ride.setDestinationName("Destination");
        ride.setPrice(100.0);
        ride.setStatus(status);
        ride.setDepartureDate(java.time.LocalDateTime.now());
        ride.setArrivalDate(java.time.LocalDateTime.now().plusHours(2));
        entityManager.persist(ride);
    }

    @Test
    public void testFindAllByPassengerIdAndStatus() {
        Page<Ride> ridesPage = rideRepository.findAllByPassengerIdAndStatus(passenger.getId(), Status.COMPLETED, PageRequest.of(0, 10));

        assertEquals(1, ridesPage.getTotalElements());
        Ride retrievedRide = ridesPage.getContent().get(0);
        assertEquals(Status.COMPLETED, retrievedRide.getStatus());
    }
}
