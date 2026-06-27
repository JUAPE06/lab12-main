module com.ucv.lab12 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens com.ucv.lab12 to javafx.fxml;
    opens com.ucv.lab12.controller to javafx.fxml;
    opens com.ucv.lab12.model to javafx.base;

    exports com.ucv.lab12;
    exports com.ucv.lab12.controller;
    exports com.ucv.lab12.model;
    exports com.ucv.lab12.service;
    exports com.ucv.lab12.repository;
    exports com.ucv.lab12.config;
}
