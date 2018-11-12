package fr.univ_amu.iut;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
    private BorderPane root = new BorderPane();
    /**
     * Hauteur et largeur de la fenètre
     */
    private int height = 600;
    private int width = 800;


    /**
     * Taille et police des titres et des textes
     */
    private Font fontTitle = new Font("Arial",20);
    private Font fontText = new Font("Arial",12);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("The Lost Ashes 4");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root,width,height);
        primaryStage.setScene(scene);
        connectionInterface();
        primaryStage.show();
    }

    /**
     * Interface de connexion
     */
    private void connectionInterface () {
        root.getChildren().clear();

        // Titre en haut de page
        HBox top = new HBox();
        Label title = new Label("The Lost Ashes 4");
        title.setFont(fontTitle);
        top.setAlignment(Pos.CENTER);
        top.getChildren().add(title);
        root.setTop(top);

        // Grille centrale
        GridPane center = new GridPane();

        // Partie de gauche -> Connexion
        VBox login = new VBox();
        login.setAlignment(Pos.CENTER);
        Label loginText = new Label("Connexion");
        loginText.setFont(fontText);
        TextField loginField = new TextField();
        loginField.setPromptText("Identifiant");
        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Mot de Passe");

        login.getChildren().addAll(loginText,loginField,pwdField);

        // Partie de droite -> Inscription
        VBox register = new VBox();

        center.add(login,0,0);

        root.setCenter(center);


    }
}