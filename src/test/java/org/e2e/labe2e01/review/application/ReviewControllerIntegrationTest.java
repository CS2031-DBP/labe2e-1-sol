package org.e2e.labe2e01.review.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e01.review.domain.Review;
import org.e2e.labe2e01.review.infrastructure.ReviewRepository;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.domain.Status;
import org.e2e.labe2e01.ride.infrastructure.RideRepository;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Review review;
    private Passenger author;
    private Driver target;
    private Ride ride;

    @BeforeEach
    public void setUp() {

        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(1.0);
        coordinate.setLongitude(1.0);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Mercedes-Benz");
        vehicle.setModel("GLB 200");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);


        author = new Passenger();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setEmail("author@example.com");
        author.setPassword("password");
        author.setPhoneNumber("123456789");
        author.setRole(Role.PASSENGER);
        author.setCreatedAt(ZonedDateTime.now());

        target = new Driver();
        target.setFirstName("Alice");
        target.setLastName("Smith");
        target.setCoordinate(coordinate);
        target.setVehicle(vehicle);
        target.setEmail("target@example.com");
        target.setPassword("password");
        target.setPhoneNumber("987654321");
        target.setRole(Role.DRIVER);
        target.setCategory(Category.X);
        target.setCreatedAt(ZonedDateTime.now());

        ride = new Ride();
        ride.setDriver(target);
        ride.setPassenger(author);
        ride.setDepartureDate(ZonedDateTime.now().toLocalDateTime());
        ride.setArrivalDate(ZonedDateTime.now().plusHours(1).toLocalDateTime());
        ride.setDestinationName("Destination");
        ride.setOriginName("Origin");
        ride.setStatus(Status.COMPLETED);
        ride.setPrice(100.0);


        rideRepository.save(ride);


        review = new Review();
        review.setComment("Great ride!");
        review.setRating(5);
    }

    @Test
    public void testCreateNewReview() throws Exception {
        passengerRepository.save(author);
        driverRepository.save(target);
        Ride currnetRide = rideRepository.save(this.ride);

        mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("rideId", currnetRide.getId().toString())
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk());

        Review createdReview = reviewRepository.findAll().get(0);

        Review savedReview = reviewRepository.findAll().get(0);
        Assertions.assertEquals(createdReview.getComment(), savedReview.getComment());
        Assertions.assertEquals(createdReview.getRating(), savedReview.getRating());
        Assertions.assertEquals(createdReview.getAuthor().getId(), savedReview.getAuthor().getId());
        Assertions.assertEquals(createdReview.getTarget().getId(), savedReview.getTarget().getId());
        Assertions.assertEquals(createdReview.getRide().getId(), savedReview.getRide().getId());
    }

    @Test
    public void testDeleteReview() throws Exception {
        Review savedReview = reviewRepository.save(review);

        mockMvc.perform(delete("/review/{id}", savedReview.getId()))
                .andExpect(status().isOk());

        Assertions.assertFalse(reviewRepository.findById(savedReview.getId()).isPresent());
    }
}

