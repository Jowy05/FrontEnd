package com.curling.controller;

import com.curling.model.StatsData;
import com.curling.service.ApiService;
import com.curling.service.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML private Label lblTotalUsers;
    @FXML private Label lblTotalReservations;
    @FXML private Label lblPendingReservations;
    @FXML private Label lblOccupancyRate;
    @FXML private Label lblAdminName;
    
    private final ApiService apiService = ApiService.getInstance();
    
    @FXML
    public void initialize() {
        lblAdminName.setText("Admin: " + AuthManager.getInstance().getCurrentUser().getName());
        loadStats();
    }
    
    private void loadStats() {
        try {
            StatsData stats = apiService.getGlobalStats();
            lblTotalUsers.setText(String.valueOf(stats.getTotalUsers()));
            lblTotalReservations.setText(String.valueOf(stats.getTotalReservations()));
            lblPendingReservations.setText(String.valueOf(stats.getPendingReservations()));
            lblOccupancyRate.setText(String.format("%.1f%%", stats.getOccupancyRate()));
        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar las estadísticas: " + e.getMessage());
        }
    }
    
    @FXML
    private void openUsers() { loadView("users.fxml", "Gestión de Usuarios"); }
    
    @FXML
    private void openReservations() { loadView("reservations.fxml", "Gestión de Reservas"); }
    
    @FXML
    private void openResults() { loadView("results.fxml", "Gestión de Resultados"); }
    
    @FXML
    private void openSheets() { loadView("sheets.fxml", "Gestión de Pistas"); }
    
    @FXML
    private void openStats() { loadView("stats.fxml", "Estadísticas Globales"); }
    
    @FXML
    private void logout() {
        AuthManager.getInstance().clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/curling/view/login.fxml"));
            Stage stage = (Stage) lblAdminName.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Curling Admin - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadView(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/curling/view/" + fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la vista: " + title);
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}