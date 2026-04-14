package com.curling.model;

public class Sheet {
    private Integer id;
    private Integer number;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE
    private String currentReservationId;
    private String currentPlayers;
    
    public Sheet() {}
    
    public Sheet(Integer number, String status) {
        this.number = number;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCurrentReservationId() { return currentReservationId; }
    public void setCurrentReservationId(String currentReservationId) { this.currentReservationId = currentReservationId; }
    
    public String getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(String currentPlayers) { this.currentPlayers = currentPlayers; }
    
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }
}