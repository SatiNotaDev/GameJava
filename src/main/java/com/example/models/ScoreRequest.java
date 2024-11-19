package com.example.models;

public class ScoreRequest {
    private Long playerId;
    private int score;
    private String difficulty;
    private String carType;
    private int coinsCollected;
    
    // Конструктор
    public ScoreRequest() {}
    
    // Геттеры и сеттеры
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }
    
    public int getCoinsCollected() { return coinsCollected; }
    public void setCoinsCollected(int coinsCollected) { this.coinsCollected = coinsCollected; }
}