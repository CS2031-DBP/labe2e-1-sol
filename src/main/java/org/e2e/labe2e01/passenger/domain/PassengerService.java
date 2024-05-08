package org.e2e.labe2e01.passenger.domain;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    public Passenger getPassenger(Long id) {
        return passengerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
    }

    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    public void addPassengerPlace(Long id, Coordinate coordinate, String description) {

        Optional<Coordinate> coord =
                coordinateRepository
                        .findByLatitudeAndLongitude(
                                coordinate.getLatitude(),
                                coordinate.getLongitude());

        Passenger passenger = passengerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        if (coord.isEmpty()) {
            coordinateRepository.save(coordinate);
            passenger.addCoordinate(coordinate, description);
            passengerRepository.save(passenger);
        } else {
            passenger.addCoordinate(coord.get(), description);
            passengerRepository.save(passenger);
        }
    }

    public void deletePassengerPlace(Long id, Long coordinateId) {
        Passenger passenger = passengerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        Coordinate coordinate = coordinateRepository
                .findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("Coordinate not found"));

        passenger.removeCoordinate(coordinate);
        passengerRepository.save(passenger);
    }

    public List<Coordinate> getPassengerPlaces(Long id) {
        Passenger passenger = passengerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        return passenger.getCoordinatesList();
    }

}
