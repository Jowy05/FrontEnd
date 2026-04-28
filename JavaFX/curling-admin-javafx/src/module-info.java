module curling.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    
    opens com.curling.model to com.fasterxml.jackson.databind;
    opens com.curling.controller to javafx.fxml;
    opens com.curling to javafx.graphics;
    
    exports com.curling;
    exports com.curling.controller;
    exports com.curling.model;
    exports com.curling.service;
}