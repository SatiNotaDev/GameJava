package com.example.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.entity.*;
import com.example.repository.*;
import com.example.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private VehicleRepository vehicleRepository;
@PostMapping("/initialize")
public ResponseEntity<ApiResponse<Vehicle>> initializeVehicle(
        @RequestBody Map<String, String> request) {
    try {
        String vehicleType = request.get("vehicleType");
        Vehicle vehicle;

        switch (vehicleType) {
            case "super":
                vehicle = new SuperCar();
                break;
            case "sport":
                vehicle = new SportCar();
                break;
            default:
                vehicle = new StandardCar();
                break;
        }

        vehicle.setBaseSpeed(0);
        vehicle.setAcceleration(0);
        vehicle.setDistance(0);
        vehicle.setScore(0);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.ok(ApiResponse.success(savedVehicle));
    } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error initializing vehicle: " + e.getMessage()));
    }
}


    @PostMapping("/move")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehiclePosition(
            @RequestBody VehicleUpdateRequest request) {
        try {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            
            switch(request.getAction()) {
                case "accelerate":
                    vehicle.accelerate();
                    break;
                case "brake":
                    vehicle.brake();
                    break;
            }
            
            vehicle.updatePosition();
            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            return ResponseEntity.ok(ApiResponse.success(updatedVehicle));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error updating vehicle: " + e.getMessage()));
        }
    }
    
    @GetMapping("/score/{vehicleId}")
    public ResponseEntity<ApiResponse<Double>> getScore(@PathVariable Long vehicleId) {
        try {
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            return ResponseEntity.ok(ApiResponse.success(vehicle.getScore()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error getting score: " + e.getMessage()));
        }
    }
}

class VehicleUpdateRequest {
    private Long vehicleId;
    private String action;
    
    // Геттеры и сеттеры
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}