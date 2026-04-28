package com.curling.controller;

import com.curling.model.Reservation;
import com.curling.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class ReservationManagementController {
    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, String> colDate;
    @FXML private TableColumn<Reservation, Integer> colSheet;
    @FXML private TableColumn<Reservation, String> colPlayer1;
    @FXML private TableColumn<Reservation, String> colPlayer2;
    @FXML private TableColumn<Reservation, String> colStatus;
    
    @FXML private DatePicker filterDate;
    @FXML private ComboBox<String> filterStatus;
    
    private final ApiService apiService = ApiService.getInstance();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @FXML
    public void initialize() {
        setupTable();
        filterStatus.setItems(FXCollections.observableArrayList(
            "TODOS", "PENDIENTE", "CONFIRMADA", "FINALIZADA", "CANCELADA"
        ));
        filterStatus.setValue("TODOS");
        loadReservations();
    }
    
    private void setupTable() {
        colDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDate().format(formatter)
            ));
        colSheet.setCellValueFactory(new PropertyValueFactory<>("sheetNumber"));
        colPlayer1.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlayer1Name()));
        colPlayer2.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlayer2Name()));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Colores según estado
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "PENDIENTE" -> setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                        case "CONFIRMADA" -> setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        case "FINALIZADA" -> setStyle("-fx-text-fill: gray;");
                        case "CANCELADA" -> setStyle("-fx-text-fill: red;");
                        default -> setStyle("");
                    }
                }
            }
        });
        
        tableReservations.setItems(reservations);
    }
    
    private void loadReservations() {
        try {
            reservations.clear();
            reservations.addAll(apiService.getAllReservations());
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }
    
    @FXML
    private void deleteReservation() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Seleccione una reserva");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar");
        confirm.setContentText("¿Cancelar esta reserva?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    apiService.deleteReservation(selected.getId());
                    showAlert("Éxito", "Reserva cancelada", Alert.AlertType.INFORMATION);
                    loadReservations();
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void applyFilter() {
        loadReservations(); // Recargar y luego filtrar en memoria para simplificar
        String status = filterStatus.getValue();
        if (!"TODOS".equals(status)) {
            reservations.removeIf(r -> !status.equals(r.getStatus()));
        }
    }
    
    @FXML
    private void refresh() {
        loadReservations();
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