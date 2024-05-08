package org.e2e.labe2e01.driver.application;


import jakarta.websocket.server.PathParam;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.domain.DriverService;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriver(id));
    }

    @PostMapping()
    public ResponseEntity<Void> saveDriver(@RequestBody Driver driver) {
        driverService.saveDriver(driver);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        driverService.updateDriver(id, driver);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<Void> updateDriverLocation(
            @PathVariable Long id,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        driverService.updateDriverLocation(id, latitude, longitude);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/car")
    public ResponseEntity<Void> updateDriverCar(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        driverService.updateDriverCar(id, vehicle);
        return ResponseEntity.ok().build();
    }


}
