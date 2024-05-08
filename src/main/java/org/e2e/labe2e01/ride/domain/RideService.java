package org.e2e.labe2e01.ride.domain;

import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e01.ride.infrastructure.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private RideRepository rideRepository;


    public void createRide(Ride rideRequest) {
        rideRepository.save(rideRequest);
    }

    public void assignRide(Long rideId) {
        Ride ride = rideRepository
                .findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(Status.ACCEPTED);
        rideRepository.save(ride);
    }

    public void cancelRide(Long rideId) {
        Ride ride = rideRepository
                .findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(Status.CANCELLED);
        rideRepository.save(ride);
    }

    public void updateRideStatus(Long rideId, String status) {
        Ride ride = rideRepository
                .findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(Status.valueOf(status));
        rideRepository.save(ride);
    }

    public Page<Ride> getRidesByUser(Long id, int page, int size) {
        Passenger passenger = passengerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        Pageable pageable = PageRequest.of(page, size);

        return rideRepository.findAllByPassengerIdAndStatus(passenger.getId(), Status.COMPLETED, pageable);

    }
}
