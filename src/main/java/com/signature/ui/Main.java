package com.signature.ui;

import com.signature.model.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Controller controller = null;
    public static boolean isWindows = false;

    @Override
    public void init() throws Exception {
        super.init();
        DataSource.getInstance().open();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.constructResponsiveUI();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Contact");
            primaryStage.getIcons().add(new Image(getClass().getResource("/icons/logo.png").toString()));
            primaryStage.setMaximized(true);
            primaryStage.setMinWidth(500.0);
            primaryStage.setMinHeight(500.0);
            if (System.getProperty("os.name").startsWith("Windows")) {
                isWindows = true;
            }
            primaryStage.show();
        } catch (IOException | NullPointerException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
