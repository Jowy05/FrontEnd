package com.curling.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {
    private String id;
    private LocalDateTime date;
    private Integer sheetNumber;
    private String status; // PENDIENTE, CONFIRMADA, FINALIZADA, CANCELADA
    private User player1;
    private User player2;
    
    public Reservation() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public Integer getSheetNumber() { return sheetNumber; }
    public void setSheetNumber(Integer sheetNumber) { this.sheetNumber = sheetNumber; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public User getPlayer1() { return player1; }
    public void setPlayer1(User player1) { this.player1 = player1; }
    
    public User getPlayer2() { return player2; }
    public void setPlayer2(User player2) { this.player2 = player2; }
    
    public String getPlayer1Name() {
        return player1 != null ? player1.getName() : "N/A";
    }
    
    public String getPlayer2Name() {
        return player2 != null ? player2.getName() : "Pendiente";
    }
}