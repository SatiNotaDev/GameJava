// SportCar.java
package com.example.entity;

import jakarta.persistence.Entity;

@Entity
public class SportCar extends Vehicle {
    private double sportBoost = 1.3;    // Между 1.2 (Standard) и 1.5 (Super)
    private double energyReserve = 100.0;  // Аналог nitroCharge в SuperCar
    private double handlingBonus = 1.1;    // Уникальная характеристика
    
    @Override
    public void accelerate() {
        if (energyReserve > 0) {
            setBaseSpeed(getBaseSpeed() + getAcceleration() * sportBoost);
            energyReserve -= 0.3;  // Медленнее тратит энергию чем SuperCar
        } else {
            setBaseSpeed(getBaseSpeed() + getAcceleration() * handlingBonus);
        }
    }
    
    @Override
    public void brake() {
        // Лучше тормозит чем StandardCar, но хуже чем SuperCar
        setBaseSpeed(Math.max(0, getBaseSpeed() - getAcceleration() * 1.1));
    }
    
    @Override
    public void updatePosition() {
        setDistance(getDistance() + getBaseSpeed());
        // Множитель очков между Standard (1.0) и Super (1.5)
        setScore(getDistance() * sportBoost);
    }
    
    // Геттеры для уникальных свойств
    public double getSportBoost() { return sportBoost; }
    public double getEnergyReserve() { return energyReserve; }
    public double getHandlingBonus() { return handlingBonus; }
}