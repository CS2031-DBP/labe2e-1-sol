package org.e2e.labe2e01.review.domain;

import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTest {

    private Review review;
    private Ride ride;
    private Passenger user;

    void setUpPassenger() {
        user = new Passenger();
        user.setRole(Role.PASSENGER);
        user.setFirstName("Carlos");
        user.setLastName("Gutierrez");
        user.setEmail("carlosgutierrez@gmail.com");
        user.setPassword("123456");
        user.setPhoneNumber("987654321");
        user.setCreatedAt(ZonedDateTime.now());
    }

    @BeforeEach
    void setUp(){
        setUpPassenger();
        review = new Review();
        review.setComment("comment1");
        review.setRating(5);
        review.setAuthor(user);
        review.setTarget(user);
        review.setRide(ride);
    }

    @Test
    void testReviewCreation(){
        assertEquals("comment1", review.getComment());
        assertEquals(5, review.getRating());
        assertEquals(user, review.getAuthor());
        assertEquals(user, review.getTarget());
        assertEquals(ride, review.getRide());
    }
}
