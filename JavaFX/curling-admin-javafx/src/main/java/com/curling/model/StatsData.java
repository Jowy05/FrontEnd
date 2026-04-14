package com.curling.model;

import java.util.Map;
import java.util.List;

public class StatsData {
    private int totalUsers;
    private int totalReservations;
    private int pendingReservations;
    private double occupancyRate; // 0-100
    private Map<String, Integer> reservationsByMonth; // "2026-04" -> 15
    private List<UserStats> topUsers;
    private Map<String, Integer> levelsDistribution; // BASIC -> 5, MEDIUM -> 3...
    
    public static class UserStats {
        private String userName;
        private int played;
        private int wins;
        
        public UserStats() {}
        public UserStats(String userName, int played, int wins) {
            this.userName = userName;
            this.played = played;
            this.wins = wins;
        }
        
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        
        public int getPlayed() { return played; }
        public void setPlayed(int played) { this.played = played; }
        
        public int getWins() { return wins; }
        public void setWins(int wins) { this.wins = wins; }
        
        public double getWinRate() {
            return played > 0 ? (wins * 100.0 / played) : 0;
        }
    }

    // Getters y Setters
    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
    
    public int getTotalReservations() { return totalReservations; }
    public void setTotalReservations(int totalReservations) { this.totalReservations = totalReservations; }
    
    public int getPendingReservations() { return pendingReservations; }
    public void setPendingReservations(int pendingReservations) { this.pendingReservations = pendingReservations; }
    
    public double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }
    
    public Map<String, Integer> getReservationsByMonth() { return reservationsByMonth; }
    public void setReservationsByMonth(Map<String, Integer> reservationsByMonth) { this.reservationsByMonth = reservationsByMonth; }
    
    public List<UserStats> getTopUsers() { return topUsers; }
    public void setTopUsers(List<UserStats> topUsers) { this.topUsers = topUsers; }
    
    public Map<String, Integer> getLevelsDistribution() { return levelsDistribution; }
    public void setLevelsDistribution(Map<String, Integer> levelsDistribution) { this.levelsDistribution = levelsDistribution; }
}