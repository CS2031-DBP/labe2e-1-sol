package org.e2e.labe2e01.driver.domain;

import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.domain.Status;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DriverTest {

    private Driver driver;
    private Vehicle vehicle;

    public void setUpVehicle(){
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
    }

    public void setUpDriver(){
        driver = new Driver();
        driver.setRole(Role.DRIVER);
        driver.setFirstName("Juan");
        driver.setLastName("Perez");
        driver.setEmail("juanperez@gmail.com");
        driver.setPassword("123456");
        driver.setPhoneNumber("987654321");

        driver.setCategory(Category.X);
        driver.setVehicle(vehicle);
    }

    @BeforeEach
    void setUp(){
        setUpVehicle();
        setUpDriver();
    }

    @Test
    void testDriverCreation(){
        assertNotNull(driver);
        assertEquals(Role.DRIVER, driver.getRole());
        assertEquals("Juan", driver.getFirstName());
        assertEquals("Perez", driver.getLastName());
        assertEquals("juanperez@gmail.com", driver.getEmail());
        assertEquals("123456", driver.getPassword());
        assertEquals("987654321", driver.getPhoneNumber());

        assertEquals(Category.X, driver.getCategory());
        assertEquals(vehicle, driver.getVehicle());
    }

    @Test
    void testDriverCategory(){
        assertEquals(Category.X, driver.getCategory());
        driver.setCategory(Category.XL);
        assertEquals(Category.XL, driver.getCategory());
    }

    @Test
    void testGetVehicle(){
        assertEquals(vehicle, driver.getVehicle());
        assertEquals("Toyota", driver.getVehicle().getBrand());
    }


    @Test
    void testDriverNull(){
        driver = null;
        assertNull(driver);
    }
}
