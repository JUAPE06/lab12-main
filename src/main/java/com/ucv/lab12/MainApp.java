package com.ucv.lab12;

import com.ucv.lab12.config.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppContext context = AppContext.getInstance();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/ucv/lab12/main-view.fxml")
        );
        loader.setControllerFactory(context::getController);

        Scene scene = new Scene(loader.load(), 1200, 680);
        stage.setTitle("Laboratorio 12 - Videojuegos");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(560);
        stage.show();
    }

    @Override
    public void stop() {
        AppContext.getInstance().destroy();
    }
}
