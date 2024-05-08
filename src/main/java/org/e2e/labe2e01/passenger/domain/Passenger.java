package org.e2e.labe2e01.passenger.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.user.domain.User;
import org.e2e.labe2e01.user_locations.domain.UserLocation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(scope = Passenger.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Passenger extends User {

    @OneToMany(mappedBy = "passenger",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<UserLocation> coordinates = new ArrayList<>();

    @OneToMany(mappedBy = "passenger")
    private List<Ride> rides = new ArrayList<>();

    public List<Coordinate> getCoordinatesList() {
        List <Coordinate> coordinates = new ArrayList<>();

        for (UserLocation userLocation : this.coordinates) {
            Coordinate newCoordinate = new Coordinate(userLocation.getCoordinate().getLatitude(), userLocation.getCoordinate().getLongitude());
            newCoordinate.setIdCoordinate(userLocation.getCoordinate().getIdCoordinate());
            coordinates.add(newCoordinate);
        }

        return coordinates;
    }

    public void addCoordinate(Coordinate coordinate, String description) {
        UserLocation userLocation = new UserLocation(this, coordinate, description);
        coordinates.add(userLocation);
        coordinate.getPassengers().add(userLocation);
    }

    public void removeCoordinate(Coordinate coordinate) {
        for (UserLocation userLocation : coordinates) {
            if (userLocation.getPassenger().equals(this) && userLocation.getCoordinate().equals(coordinate)) {
                coordinates.remove(userLocation);
                userLocation.getCoordinate().getPassengers().remove(userLocation);
                userLocation.setPassenger(null);
                userLocation.setCoordinate(null);
                break;
            }
        }
    }
}