package com.example.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.entity.Player;
import com.example.repository.PlayerRepository;
import com.example.models.LoginRequest;
import com.example.models.ScoreRequest;
import com.example.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Player>> loginPlayer(@RequestBody LoginRequest request) {
        try {
            Player player = playerRepository.findByNickname(request.getNickname())
                .orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setNickname(request.getNickname());
                    newPlayer.setScore(0);
                    newPlayer.setGamesPlayed(0);
                    newPlayer.setTotalCoinsCollected(0);
                    return playerRepository.save(newPlayer);
                });
            
            return ResponseEntity.ok(ApiResponse.success(player));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error during login: " + e.getMessage()));
        }
    }
    
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<Player>> updateScore(@RequestBody ScoreRequest request) {
        try {
            Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));
            
            // Обновляем статистику игрока
            player.updateStats(
                request.getScore(),
                request.getDifficulty(),
                request.getCarType(),
                request.getCoinsCollected()
            );
            
            playerRepository.save(player);
            return ResponseEntity.ok(ApiResponse.success(player));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error updating score: " + e.getMessage()));
        }
    }

    @GetMapping("/stats/{nickname}")
    public ResponseEntity<ApiResponse<Player>> getPlayerStats(@PathVariable String nickname) {
        try {
            Player player = playerRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found"));
            
            return ResponseEntity.ok(ApiResponse.success(player));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error getting stats: " + e.getMessage()));
        }
    }
}