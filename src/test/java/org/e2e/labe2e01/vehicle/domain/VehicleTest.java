package org.e2e.labe2e01.vehicle.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp(){
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
    }

    @Test
    void testVehicleCreation(){
        assertNotNull(vehicle);
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals("ABC123", vehicle.getLicensePlate());
        assertEquals(2020, vehicle.getFabricationYear());
        assertEquals(5, vehicle.getCapacity());
    }

    @Test
    void testVehicleBrand(){
        assertEquals("Toyota", vehicle.getBrand());
        vehicle.setBrand("Volvo");
        assertEquals("Volvo", vehicle.getBrand());
    }

    @Test
    void testVehicleModel(){
        assertEquals("Corolla", vehicle.getModel());
        vehicle.setModel("Rav4");
        assertEquals("Rav4", vehicle.getModel());
    }

    @Test
    void testVehicleLicensePlate(){
        assertEquals("ABC123", vehicle.getLicensePlate());
        vehicle.setLicensePlate("DEF456");
        assertEquals("DEF456", vehicle.getLicensePlate());
    }

    @Test
    void testVehicleYear(){
        assertEquals(2020, vehicle.getFabricationYear());
        vehicle.setFabricationYear(2021);
        assertEquals(2021, vehicle.getFabricationYear());
    }

    @Test
    void testVehicleCapacity(){
        assertEquals(5, vehicle.getCapacity());
        vehicle.setCapacity(7);
        assertEquals(7, vehicle.getCapacity());
    }

    @Test
    void testVehicleDelete(){
        vehicle.setId(1L);
        assertNotNull(vehicle.getId());
        vehicle.setId(null);
        assertNull(vehicle.getId());
    }
}