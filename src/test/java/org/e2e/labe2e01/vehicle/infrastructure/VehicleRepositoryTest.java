package org.e2e.labe2e01.vehicle.infrastructure;

import org.e2e.labe2e01.AbstractContainerBaseTest;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setBrand("Mercedes-Benz");
        vehicle1.setModel("GLB 200");
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setFabricationYear(2020);
        vehicle1.setCapacity(5);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setBrand("Audi");
        vehicle2.setModel("Q3");
        vehicle2.setLicensePlate("DEF123");
        vehicle2.setFabricationYear(2021);
        vehicle2.setCapacity(5);

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setBrand("Toyota");
        vehicle3.setModel("Corolla");
        vehicle3.setLicensePlate("GHI123");
        vehicle3.setFabricationYear(2021);
        vehicle3.setCapacity(5);

        entityManager.persist(vehicle1);
        entityManager.persist(vehicle2);
        entityManager.persist(vehicle3);
        entityManager.flush();
    }

    @Test
    public void testFindByBrandInAndYearGreaterThanEqual() {
        List<String> brands = Arrays.asList("Mercedes-Benz", "Audi");
        List<Vehicle> vehicles = vehicleRepository.findByBrandInAndFabricationYearGreaterThanEqual(brands, 2020);

        assertEquals(2, vehicles.size());
        assertTrue(vehicles.stream().anyMatch(v -> v.getBrand().equals("Mercedes-Benz") && v.getModel().equals("GLB 200")));
        assertTrue(vehicles.stream().anyMatch(v -> v.getBrand().equals("Audi") && v.getModel().equals("Q3")));
    }

    @Test
    public void testLicensePlateUniqueConstraint() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setBrand("Tesla");
        vehicle1.setModel("Model S");
        vehicle1.setLicensePlate("UNI123");
        vehicle1.setFabricationYear(2022);
        vehicle1.setCapacity(5);
        entityManager.persistAndFlush(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setBrand("Tesla");
        vehicle2.setModel("Model 3");
        vehicle2.setLicensePlate("UNI123");
        vehicle2.setFabricationYear(2023);
        vehicle2.setCapacity(5);

        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(vehicle2);
        });
    }

    @Test
    public void testNoVehiclesFoundAfterCleanup() {
        vehicleRepository.deleteAll();
        assertTrue(vehicleRepository.findAll().isEmpty());
    }
}
