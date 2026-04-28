package com.curling.controller;

import com.curling.model.User;
import com.curling.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagementController {
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, Integer> colAge;
    @FXML private TableColumn<User, String> colLevel;
    @FXML private TableColumn<User, String> colRole;
    
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private ComboBox<String> comboLevel;
    @FXML private ComboBox<String> comboRole;
    
    private final ApiService apiService = ApiService.getInstance();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private User selectedUser;
    
    @FXML
    public void initialize() {
        setupTable();
        comboLevel.setItems(FXCollections.observableArrayList("BASIC", "MEDIUM", "HIGH"));
        comboRole.setItems(FXCollections.observableArrayList("USER", "ADMIN"));
        loadUsers();
        
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedUser = newVal;
                fillForm(newVal);
            }
        });
    }
    
    private void setupTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tableUsers.setItems(users);
    }
    
    private void loadUsers() {
        try {
            users.clear();
            users.addAll(apiService.getAllUsers());
        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }
    
    private void fillForm(User user) {
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtAge.setText(String.valueOf(user.getAge()));
        comboLevel.setValue(user.getLevel());
        comboRole.setValue(user.getRole());
    }
    
    @FXML
    private void updateUser() {
        if (selectedUser == null) {
            showAlert("Error", "Seleccione un usuario primero");
            return;
        }
        
        try {
            User updated = new User();
            updated.setName(txtName.getText());
            updated.setEmail(txtEmail.getText());
            updated.setAge(Integer.parseInt(txtAge.getText()));
            updated.setLevel(comboLevel.getValue());
            updated.setRole(comboRole.getValue());
            
            apiService.updateUser(selectedUser.getId(), updated);
            showAlert("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
            loadUsers();
        } catch (Exception e) {
            showAlert("Error", "No se pudo actualizar: " + e.getMessage());
        }
    }
    
    @FXML
    private void deleteUser() {
        if (selectedUser == null) {
            showAlert("Error", "Seleccione un usuario primero");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setContentText("¿Eliminar a " + selectedUser.getName() + "?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    apiService.deleteUser(selectedUser.getId());
                    showAlert("Éxito", "Usuario eliminado", Alert.AlertType.INFORMATION);
                    loadUsers();
                    clearForm();
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void refresh() {
        loadUsers();
    }
    
    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtAge.clear();
        comboLevel.setValue(null);
        comboRole.setValue(null);
        selectedUser = null;
    }
    
    private void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}