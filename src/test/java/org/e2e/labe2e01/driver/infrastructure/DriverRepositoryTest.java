package org.e2e.labe2e01.driver.infrastructure;

import org.e2e.labe2e01.AbstractContainerBaseTest;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Vehicle vehicle1;

    @BeforeEach
    public void setUp() {
        vehicle1 = new Vehicle();
        vehicle1.setBrand("Mercedes-Benz");
        vehicle1.setModel("GLB 200");
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setFabricationYear(2020);
        vehicle1.setCapacity(5);
        entityManager.persist(vehicle1);

        Coordinate coordinate = new Coordinate(40.730610, -73.935242);

        Driver driver1 = new Driver();
        driver1.setFirstName("John");
        driver1.setLastName("Doe");
        driver1.setEmail("john@example.com");
        driver1.setPassword("password");
        driver1.setPhoneNumber("1234567890");
        driver1.setCategory(Category.X);
        driver1.setVehicle(vehicle1);
        driver1.setRole(Role.DRIVER);
        driver1.setCreatedAt(ZonedDateTime.now());
        driver1.setCoordinate(coordinate);
        entityManager.persist(driver1);

        Passenger passenger1 = new Passenger();
        passenger1.setFirstName("Jane");
        passenger1.setLastName("Doe");
        passenger1.setEmail("jane@example.com");
        passenger1.setPassword("password");
        passenger1.setPhoneNumber("0987654321");
        passenger1.setRole(Role.PASSENGER);
        passenger1.setCreatedAt(ZonedDateTime.now());
        entityManager.persist(passenger1);

        Coordinate originCoordinate = new Coordinate(40.730610, -73.935242);
        Coordinate destinationCoordinate = new Coordinate(40.782865, -73.967531);
        entityManager.persist(originCoordinate);
        entityManager.persist(destinationCoordinate);

        Ride ride = new Ride();
        ride.setOriginName("Origin");
        ride.setDestinationName("Destination");
        ride.setPrice(10.0);
        ride.setStatus(Status.ACCEPTED);
        ride.setDepartureDate(LocalDateTime.now());
        ride.setArrivalDate(LocalDateTime.now().plusHours(1));
        ride.setOriginCoordinates(originCoordinate);
        ride.setDestinationCoordinates(destinationCoordinate);
        ride.setDriver(driver1);
        ride.setPassenger(passenger1);
        entityManager.persist(ride);

        entityManager.flush();
    }

    @Test
    public void testFindNearbyDrivers() {
        List<Driver> nearbyDrivers = driverRepository.findAllByCategory(Category.X);

        assertEquals(1, nearbyDrivers.size());
        Driver driver = nearbyDrivers.get(0);
        assertEquals("John", driver.getFirstName());
        assertEquals("Doe", driver.getLastName());
        assertEquals("john@example.com", driver.getEmail());
        assertEquals(Category.X, driver.getCategory());
        assertEquals(vehicle1, driver.getVehicle());
    }

    @Test
    public void testFindNearbyDriversWithNoDrivers() {
        List<Driver> nearbyDrivers = driverRepository.findAllByCategory(Category.BLACK);
        assertTrue(nearbyDrivers.isEmpty());
    }
}
