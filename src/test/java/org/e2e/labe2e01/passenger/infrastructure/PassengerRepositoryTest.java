package org.e2e.labe2e01.passenger.infrastructure;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.user_locations.domain.UserLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger passenger;

    @BeforeEach
    public void setUp() {
        passenger = new Passenger();
        passenger.setFirstName("Test");
        passenger.setLastName("Passenger");
        passenger.setEmail("test@passenger.com");
        passenger.setPassword("test123");
        passenger.setPhoneNumber("123456789");
        passenger.setRole(Role.PASSENGER);
        passenger.setCreatedAt(ZonedDateTime.now());
        entityManager.persist(passenger);
    }

    @Test
    public void testAddAndGetPassengerPlace() {
        Coordinate coordinate = new Coordinate(10.0, 20.0);
        entityManager.persist(coordinate);

        String description = "Test Location";
        passenger.addCoordinate(coordinate, description);
        passengerRepository.save(passenger);

        entityManager.flush();
        entityManager.clear();

        Passenger retrievedPassenger = passengerRepository.findById(passenger.getId()).orElse(null);
        assertNotNull(retrievedPassenger);
        assertFalse(retrievedPassenger.getCoordinates().isEmpty());
        UserLocation userLocation = retrievedPassenger.getCoordinates().get(0);
        assertEquals(description, userLocation.getDescription());
        assertEquals(coordinate.getLatitude(), userLocation.getCoordinate().getLatitude());
        assertEquals(coordinate.getLongitude(), userLocation.getCoordinate().getLongitude());
    }

    @Test
    public void testDeletePassengerPlace() {
        Coordinate coordinate = new Coordinate(10.0, 20.0);
        entityManager.persist(coordinate);

        String description = "Test Location";
        passenger.addCoordinate(coordinate, description);
        passengerRepository.saveAndFlush(passenger);

        passenger.removeCoordinate(coordinate);
        passengerRepository.saveAndFlush(passenger);

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElse(null);
        assertNotNull(updatedPassenger);
        assertTrue(updatedPassenger.getCoordinates().isEmpty());
    }
}
