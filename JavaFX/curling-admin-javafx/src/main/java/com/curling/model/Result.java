package com.curling.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private String id;
    private String reservationId;
    private User winner;
    private String score;
    private String comments;
    private LocalDateTime recordedAt;
    
    // Campos auxiliares para mostrar info de la reserva asociada
    private String player1Name;
    private String player2Name;
    private LocalDateTime reservationDate;
    
    public Result() {}

    // Getters y Setters estándar
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    
    public User getWinner() { return winner; }
    public void setWinner(User winner) { this.winner = winner; }
    
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    
    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }
    
    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }
    
    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }
    
    public String getWinnerName() {
        return winner != null ? winner.getName() : "Empate/No definido";
    }
}