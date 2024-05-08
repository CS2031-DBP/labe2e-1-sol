package org.e2e.labe2e01.user_locations.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.passenger.domain.Passenger;

@Entity
@Data
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserLocation {
    @EmbeddedId
    private PassengerCoordinateId id;

    public UserLocation(Passenger passenger, Coordinate coordinate, String description) {
        this.passenger =  passenger;
        this.coordinate = coordinate;
        this.description = description;
        this.id = new PassengerCoordinateId(passenger.getId(), coordinate.getIdCoordinate());
    }

    public UserLocation() {}

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("passengerId")
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("coordinateId")
    private Coordinate coordinate;

}

