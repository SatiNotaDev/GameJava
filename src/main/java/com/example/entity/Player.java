package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nickname;
    private int score;                // Лучший счет
    private String bestDifficulty;    // Уровень сложности лучшего счета
    private String preferredCar;      // Предпочитаемая машина
    private int gamesPlayed;          // Количество сыгранных игр
    private int totalCoinsCollected;  // Общее количество собранных монет
    
    // Конструктор по умолчанию
    public Player() {}
    
    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getBestDifficulty() { return bestDifficulty; }
    public void setBestDifficulty(String bestDifficulty) { this.bestDifficulty = bestDifficulty; }
    
    public String getPreferredCar() { return preferredCar; }
    public void setPreferredCar(String preferredCar) { this.preferredCar = preferredCar; }
    
    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    
    public int getTotalCoinsCollected() { return totalCoinsCollected; }
    public void setTotalCoinsCollected(int totalCoinsCollected) { this.totalCoinsCollected = totalCoinsCollected; }
    
    // Метод для обновления статистики после игры
    public void updateStats(int newScore, String difficulty, String carType, int coinsCollected) {
        this.gamesPlayed++;
        this.totalCoinsCollected += coinsCollected;
        
        if (newScore > this.score) {
            this.score = newScore;
            this.bestDifficulty = difficulty;
            this.preferredCar = carType;
        }
    }
}