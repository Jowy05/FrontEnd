package com.curling.controller;

import com.curling.model.Result;
import com.curling.service.ApiService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class ResultManagementController {
    @FXML private TableView<Result> tableResults;
    @FXML private TableColumn<Result, String> colDate;
    @FXML private TableColumn<Result, String> colScore;
    @FXML private TableColumn<Result, String> colWinner;
    
    @FXML private TextField txtScore;
    @FXML private TextField txtComments;
    
    private final ApiService apiService = ApiService.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FXML
    public void initialize() {
        setupTable();
        loadResults();
    }
    
    private void setupTable() {
        colDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRecordedAt().format(formatter)
            ));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colWinner.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getWinnerName()));
        
        tableResults.setItems(FXCollections.observableArrayList());
    }
    
    private void loadResults() {
        try {
            tableResults.getItems().clear();
            tableResults.getItems().addAll(apiService.getAllResults());
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }
    
    @FXML
    private void updateResult() {
        Result selected = tableResults.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Seleccione un resultado");
            return;
        }
        
        try {
            selected.setScore(txtScore.getText());
            selected.setComments(txtComments.getText());
            apiService.updateResult(selected.getId(), selected);
            showAlert("Éxito", "Resultado actualizado", Alert.AlertType.INFORMATION);
            loadResults();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }
    
    @FXML
    private void refresh() {
        loadResults();
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