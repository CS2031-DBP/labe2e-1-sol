package org.e2e.labe2e01.passenger.domain;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;

public class PassengerTest {

    private Coordinate coordinate;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setFirstName("Pablo");
        passenger.setLastName("Gonzalez");
        passenger.setEmail("pablogonzalez@gmail.com");
        passenger.setPassword("123456");
        passenger.setPhoneNumber("123456789");
        passenger.setCreatedAt(ZonedDateTime.now());
    }

    @Test
    void testPassengerCreation(){
        assertEquals("Pablo", passenger.getFirstName());
        assertEquals("Gonzalez", passenger.getLastName());
        assertEquals("pablogonzalez@gmail.com", passenger.getEmail());
        assertEquals("123456", passenger.getPassword());
        assertEquals("123456789", passenger.getPhoneNumber());
        assertNotNull(passenger.getCreatedAt());
    }

    @Test
    void testAddCoordinate(){
        coordinate = new Coordinate(1.0, 1.0);
        passenger.addCoordinate(coordinate, "description1");
        assertEquals(1, passenger.getCoordinates().size());
    }

    @Test
    void testRemoveCoordinate(){
        coordinate = new Coordinate(1.0, 1.0);
        passenger.addCoordinate(coordinate, "description1");
        passenger.removeCoordinate(coordinate);
        assertEquals(0, passenger.getCoordinates().size());
    }
}
