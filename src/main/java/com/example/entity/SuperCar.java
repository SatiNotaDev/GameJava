// SuperCar.java
package com.example.entity;

import jakarta.persistence.Entity;

@Entity
public class SuperCar extends Vehicle {
   private double turboBoost = 1.5;
   private double nitroCharge = 100.0;
   
   @Override
   public void accelerate() {
       if (nitroCharge > 0) {
           setBaseSpeed(getBaseSpeed() + getAcceleration() * turboBoost);
           nitroCharge -= 0.5;
       } else {
           setBaseSpeed(getBaseSpeed() + getAcceleration());
       }
   }
   
   @Override
   public void brake() {
       setBaseSpeed(Math.max(0, getBaseSpeed() - getAcceleration() * 1.2));
   }
   
   @Override
   public void updatePosition() {
       setDistance(getDistance() + getBaseSpeed());
       setScore(getDistance() * turboBoost);
   }
}