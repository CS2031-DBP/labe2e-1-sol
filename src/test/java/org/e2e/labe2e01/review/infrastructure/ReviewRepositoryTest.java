package org.e2e.labe2e01.review.infrastructure;

import org.e2e.labe2e01.AbstractContainerBaseTest;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.review.domain.Review;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger author;
    private Driver target;
    private Ride ride, anotherRide;

    @BeforeEach
    public void setUp() {
        Coordinate coordinate = new Coordinate(40.730610, -73.935242);

        author = new Passenger();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setEmail("john@example.com");
        author.setPassword("password");
        author.setPhoneNumber("1234567890");
        author.setRole(Role.PASSENGER);
        author.setCreatedAt(ZonedDateTime.now());
        entityManager.persist(author);


        Vehicle vehicle1 = new Vehicle();
        vehicle1.setBrand("Mercedes-Benz");
        vehicle1.setModel("GLB 200");
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setFabricationYear(2020);
        vehicle1.setCapacity(5);
        entityManager.persist(vehicle1);

        target = new Driver();
        target.setFirstName("Jane");
        target.setLastName("Doe");
        target.setEmail("jane@example.com");
        target.setPassword("password");
        target.setPhoneNumber("0987654321");
        target.setRole(Role.DRIVER);
        target.setCategory(Category.XL);
        target.setVehicle(vehicle1);
        target.setCreatedAt(ZonedDateTime.now());
        target.setCoordinate(coordinate);
        entityManager.persist(target);

        ride = new Ride();
        ride.setOriginName("Origin");
        ride.setDestinationName("Destination");
        ride.setPrice(10.0);
        ride.setStatus(Status.COMPLETED);
        ride.setDepartureDate(LocalDateTime.now().minusHours(2));
        ride.setArrivalDate(LocalDateTime.now().minusHours(1));
        ride.setDriver(target);
        ride.setPassenger(author);
        entityManager.persist(ride);

        anotherRide = new Ride();
        anotherRide.setOriginName("Second Origin");
        anotherRide.setDestinationName("Second Destination");
        anotherRide.setPrice(15.0);
        anotherRide.setStatus(Status.COMPLETED);
        anotherRide.setDepartureDate(LocalDateTime.now().minusDays(1));
        anotherRide.setArrivalDate(LocalDateTime.now().minusDays(1).plusHours(1));
        anotherRide.setDriver(target);
        anotherRide.setPassenger(author);
        entityManager.persist(anotherRide);

        entityManager.flush();
    }

    @Test
    public void testFindByRating() {
        Review review1 = new Review();
        review1.setRide(ride);
        review1.setAuthor(author);
        review1.setTarget(target);
        review1.setRating(4);
        review1.setComment("Great ride!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setRide(anotherRide);
        review2.setAuthor(target);
        review2.setTarget(author);
        review2.setRating(3);
        review2.setComment("Okay ride.");
        reviewRepository.save(review2);

        Set<Review> reviews = reviewRepository.findByRating(4);
        assertEquals(1, reviews.size());
        assertEquals(4, reviews.iterator().next().getRating());
    }

    @Test
    public void testFindReviewByAuthor() {
        Review review1 = new Review();
        review1.setRide(ride);
        review1.setAuthor(author);
        review1.setTarget(target);
        review1.setRating(4);
        review1.setComment("Great ride!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setRide(anotherRide);
        review2.setAuthor(target);
        review2.setTarget(author);
        review2.setRating(3);
        review2.setComment("Okay ride.");
        reviewRepository.save(review2);

        Set<Review> reviews = reviewRepository.findByAuthor_Id(author.getId());
        assertNotNull(reviews);
        assertFalse(reviews.isEmpty());
        assertTrue(reviews.stream().anyMatch(review -> review.getRating().equals(4)));
    }

    @Test
    public void testCountReviewsByAuthor() {
        Review review1 = new Review();
        review1.setRide(ride);
        review1.setAuthor(author);
        review1.setTarget(target);
        review1.setRating(4);
        review1.setComment("Great ride!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setRide(anotherRide);
        review2.setAuthor(target);
        review2.setTarget(author);
        review2.setRating(3);
        review2.setComment("Okay ride.");
        reviewRepository.save(review2);

        Long count = reviewRepository.countByAuthor_Id(author.getId());
        assertEquals(1, count);
    }

}
