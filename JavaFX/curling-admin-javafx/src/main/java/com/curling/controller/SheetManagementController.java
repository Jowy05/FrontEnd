package com.curling.controller;

import com.curling.model.Reservation;
import com.curling.model.Sheet;
import com.curling.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.List;

public class SheetManagementController {
    @FXML private TableView<Sheet> tableSheets;
    @FXML private TableColumn<Sheet, Integer> colNumber;
    @FXML private TableColumn<Sheet, String> colStatus;
    @FXML private TableColumn<Sheet, String> colCurrent;

    @FXML private ComboBox<String> comboStatus;
    @FXML private Label lblInfo;

    private final ObservableList<Sheet> sheets = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        comboStatus.setItems(FXCollections.observableArrayList(
            "AVAILABLE", "OCCUPIED", "MAINTENANCE"
        ));
        for (int i = 1; i <= 4; i++) {
            sheets.add(new Sheet(i, "AVAILABLE"));
        }
        loadSheetStatus();
    }

    private void setupTable() {
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
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
                        case "AVAILABLE" -> setTextFill(Color.GREEN);
                        case "OCCUPIED" -> setTextFill(Color.RED);
                        case "MAINTENANCE" -> setTextFill(Color.ORANGE);
                    }
                }
            }
        });
        colCurrent.setCellValueFactory(new PropertyValueFactory<>("currentPlayers"));
        tableSheets.setItems(sheets);
    }
    
    private void loadSheetStatus() {
        try {
            // Obtener reservas de hoy para marcar pistas ocupadas
            List<Reservation> reservations = ApiService.getInstance().getAllReservations();
            LocalDateTime now = LocalDateTime.now();
            
            // Reset todas a disponible primero
            sheets.forEach(s -> {
                s.setStatus("AVAILABLE");
                s.setCurrentPlayers("-");
            });
            
            // Marcar ocupadas según reservas activas
            for (Reservation r : reservations) {
                if (r.getDate().toLocalDate().equals(now.toLocalDate()) &&
                    ("CONFIRMADA".equals(r.getStatus()) || "PENDIENTE".equals(r.getStatus()))) {
                    
                    int sheetNum = r.getSheetNumber();
                    if (sheetNum >= 1 && sheetNum <= sheets.size()) {
                        Sheet sheet = sheets.get(sheetNum - 1);
                        sheet.setStatus("OCCUPIED");
                        sheet.setCurrentPlayers(r.getPlayer1Name() + " vs " + r.getPlayer2Name());
                    }
                }
            }
            
            tableSheets.refresh();
        } catch (Exception e) {
            lblInfo.setText("Error cargando estado: " + e.getMessage());
        }
    }
    
    @FXML
    private void changeStatus() {
        Sheet selected = tableSheets.getSelectionModel().getSelectedItem();
        String newStatus = comboStatus.getValue();
        
        if (selected == null || newStatus == null) {
            showAlert("Error", "Seleccione pista y estado");
            return;
        }
        
        selected.setStatus(newStatus);
        if ("AVAILABLE".equals(newStatus)) {
            selected.setCurrentPlayers("-");
        }
        tableSheets.refresh();
        lblInfo.setText("Estado actualizado (simulado - persistir en backend)");
    }
    
    @FXML
    private void refresh() {
        loadSheetStatus();
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}