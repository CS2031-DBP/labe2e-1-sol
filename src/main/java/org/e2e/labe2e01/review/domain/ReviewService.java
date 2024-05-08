package org.e2e.labe2e01.review.domain;

import org.e2e.labe2e01.review.infrastructure.ReviewRepository;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.infrastructure.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RideRepository rideRepository;
    public void createNewReview(Review newReview, Long rideId) {
        Ride ride = rideRepository
                .findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        newReview.setRide(ride);
        newReview.setAuthor(ride.getPassenger());
        newReview.setTarget(ride.getDriver());
        reviewRepository.save(newReview);
    }

    public void deleteReview(Long id) {
        reviewRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepository.deleteById(id);
    }
}
