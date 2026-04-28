package com.curling.controller;

import com.curling.model.User;
import com.curling.service.ApiService;
import com.curling.service.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    private final ApiService apiService = ApiService.getInstance();
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            String token = apiService.login(email, password);
            AuthManager.getInstance().setToken(token);
            
            // Verificar que es admin
            User profile = apiService.getProfile();
            AuthManager.getInstance().setCurrentUser(profile);
            
            if (!"ADMIN".equals(profile.getRole())) {
                showAlert("Acceso Denegado", 
                    "Este sistema es solo para administradores.\nSu rol actual es: " + profile.getRole(), 
                    Alert.AlertType.ERROR);
                AuthManager.getInstance().clear();
                return;
            }
            
            loadDashboard();
            
        } catch (Exception e) {
            showAlert("Error de autenticación", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/curling/view/dashboard.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Curling Admin - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar el dashboard", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}