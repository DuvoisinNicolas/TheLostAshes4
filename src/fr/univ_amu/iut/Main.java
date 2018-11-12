package fr.univ_amu.iut;

import fr.univ_amu.iut.DAO.DAOUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("The Lost Ashes 4");
        StackPane root = new StackPane();
        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}