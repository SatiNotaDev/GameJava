// StandardCar.java
package com.example.entity;

import jakarta.persistence.Entity;

@Entity
public class StandardCar extends Vehicle {
    private double stabilityFactor = 1.2;
    
    @Override
    public void accelerate() {
        setBaseSpeed(getBaseSpeed() + getAcceleration() * stabilityFactor);
    }
    
    @Override
    public void brake() {
        setBaseSpeed(Math.max(0, getBaseSpeed() - getAcceleration()));
    }
    
    @Override
    public void updatePosition() {
        setDistance(getDistance() + getBaseSpeed());
        setScore(getDistance());
    }
}