package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double baseSpeed;
    private double acceleration;
    private double distance;
    private double score;

    public abstract void accelerate();
    public abstract void brake();
    public abstract void updatePosition();

    // Геттеры и сеттеры
    public double getBaseSpeed() { return baseSpeed; }
    public void setBaseSpeed(double baseSpeed) { this.baseSpeed = baseSpeed; }
    
    public double getAcceleration() { return acceleration; }
    public void setAcceleration(double acceleration) { this.acceleration = acceleration; }
    
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}