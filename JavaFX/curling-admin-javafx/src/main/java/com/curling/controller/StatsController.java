package com.curling.controller;

import com.curling.model.StatsData;
import com.curling.model.User;
import com.curling.service.ApiService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class StatsController {
    @FXML private PieChart chartOccupancy;
    @FXML private BarChart<String, Number> chartTopUsers;
    @FXML private LineChart<String, Number> chartMonthly;
    @FXML private BarChart<String, Number> chartLevels;
    @FXML private TableView<StatsData.UserStats> tableRanking;
    @FXML private Label lblTotalUsers;
    
    private final ApiService apiService = ApiService.getInstance();
    
    @FXML
    public void initialize() {
        setupCharts();
        setupRankingTable();
        loadStats();
    }
    
    private void setupCharts() {
        chartTopUsers.getXAxis().setLabel("Usuario");
        chartTopUsers.getYAxis().setLabel("Partidos Jugados");
        chartMonthly.getXAxis().setLabel("Mes");
        chartMonthly.getYAxis().setLabel("Reservas");
    }
    
    @SuppressWarnings("unchecked")
    private void setupRankingTable() {
        TableColumn<StatsData.UserStats, String> colName = new TableColumn<>("Usuario");
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        
        TableColumn<StatsData.UserStats, Integer> colPlayed = new TableColumn<>("Jugados");
        colPlayed.setCellValueFactory(new PropertyValueFactory<>("played"));
        
        TableColumn<StatsData.UserStats, Integer> colWins = new TableColumn<>("Ganados");
        colWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        
        TableColumn<StatsData.UserStats, Double> colRate = new TableColumn<>("Win %");
        colRate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getWinRate()).asObject());
        
        tableRanking.getColumns().addAll(colName, colPlayed, colWins, colRate);
    }
    
    private void loadStats() {
        try {
            // Obtener datos básicos
            int totalUsers = apiService.getAllUsers().size();
            lblTotalUsers.setText("Total Usuarios: " + totalUsers);
            
            // Simular datos de estadísticas (en producción vendrían de endpoint específico)
            loadOccupancyChart();
            loadTopUsersChart();
            loadMonthlyChart();
            loadLevelsChart();
            loadRanking();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadOccupancyChart() {
        // Datos simulados - en realidad calcularías esto del backend
        PieChart.Data slice1 = new PieChart.Data("Ocupadas", 65);
        PieChart.Data slice2 = new PieChart.Data("Disponibles", 25);
        PieChart.Data slice3 = new PieChart.Data("Mantenimiento", 10);
        chartOccupancy.setData(FXCollections.observableArrayList(slice1, slice2, slice3));
        chartOccupancy.setTitle("Ocupación de Pistas");
    }
    
    private void loadTopUsersChart() {
        // Simulado - deberías obtener esto de /api/stats/top-users
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Partidos Jugados");
        series.getData().add(new XYChart.Data<>("Juan", 15));
        series.getData().add(new XYChart.Data<>("María", 12));
        series.getData().add(new XYChart.Data<>("Pedro", 10));
        series.getData().add(new XYChart.Data<>("Ana", 8));
        series.getData().add(new XYChart.Data<>("Luis", 5));
        
        chartTopUsers.getData().add(series);
        chartTopUsers.setTitle("Top 5 Usuarios Más Activos");
    }
    
    private void loadMonthlyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Reservas 2026");
        series.getData().add(new XYChart.Data<>("Ene", 45));
        series.getData().add(new XYChart.Data<>("Feb", 50));
        series.getData().add(new XYChart.Data<>("Mar", 65));
        series.getData().add(new XYChart.Data<>("Abr", 40));
        
        chartMonthly.getData().add(series);
        chartMonthly.setTitle("Evolución Mensual de Reservas");
    }
    
    private void loadLevelsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Usuarios por Nivel");
        
        // Calcular distribución real
        try {
            Map<String, Integer> levels = new java.util.HashMap<>();
            for (User u : apiService.getAllUsers()) {
                levels.merge(u.getLevel(), 1, Integer::sum);
            }
            
            levels.forEach((level, count) -> 
                series.getData().add(new XYChart.Data<>(level, count)));
                
        } catch (Exception e) {
            // Datos demo si falla
            series.getData().add(new XYChart.Data<>("BASIC", 10));
            series.getData().add(new XYChart.Data<>("MEDIUM", 5));
            series.getData().add(new XYChart.Data<>("HIGH", 3));
        }
        
        chartLevels.getData().add(series);
        chartLevels.setTitle("Distribución por Niveles");
    }
    
    private void loadRanking() {
        // Simulado - deberías obtener de /api/stats/ranking
        // Calcular basado en resultados individuales si es posible
        try {
            var stats = new StatsData.UserStats("Juan Pérez", 20, 15);
            var stats2 = new StatsData.UserStats("María García", 18, 12);
            var stats3 = new StatsData.UserStats("Carlos López", 15, 8);
            
            tableRanking.getItems().addAll(stats, stats2, stats3);
            tableRanking.getItems().sort((a, b) -> 
                Double.compare(b.getWinRate(), a.getWinRate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}