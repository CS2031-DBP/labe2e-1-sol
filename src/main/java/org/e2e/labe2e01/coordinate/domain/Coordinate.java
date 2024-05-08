package org.e2e.labe2e01.coordinate.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.user_locations.domain.UserLocation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(scope = Coordinate.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCoordinate")
public class Coordinate {

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoordinate;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;


    @OneToMany(mappedBy = "coordinate",
            orphanRemoval = true
    )
    private List<UserLocation> passengers = new ArrayList<>();

}
