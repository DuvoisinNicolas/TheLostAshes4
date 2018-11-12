package fr.univ_amu.iut;

import fr.univ_amu.iut.DAO.DAOUser;
import fr.univ_amu.iut.Exceptions.BadEntryException;
import fr.univ_amu.iut.Exceptions.InvalidMailException;
import fr.univ_amu.iut.Exceptions.NoUserException;
import fr.univ_amu.iut.beans.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends Application {

    /**
     * Paramètres de l'utilisateur
     */

    private User user = new User();
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
        top.setPadding(new Insets(10,0,0,0));
        top.setAlignment(Pos.CENTER);
        top.getChildren().add(title);
        root.setTop(top);

        // Grille centrale
        GridPane center = new GridPane();

        // Partie de gauche -> Connexion
        VBox login = new VBox();
        login.setSpacing(10);
        login.setAlignment(Pos.CENTER);

        TextField loginField = new TextField();
        loginField.setPromptText("Identifiant");

        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Mot de Passe");

        Button connectButton = new Button("Connexion");

        /**
         * Bouton permettant de vérifier la connexion de l'utilisateur
         */
        connectButton.setOnAction(event -> {
            try {
                DAOUser daoUser = new DAOUser();
                user = daoUser.findByUsernameAndPwd(loginField.getText(),pwdField.getText());
            }
            catch (NoUserException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Mauvais identifiants !");
                alert.showAndWait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        center.setPadding(new Insets(150,0,0,150));
        login.getChildren().addAll(loginField,pwdField,connectButton);

        // Partie de droite -> Inscription
        VBox register = new VBox();
        register.setSpacing(10);
        register.setAlignment(Pos.CENTER);

        TextField registerField = new TextField();
        registerField.setPromptText("Identifiant");

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");

        TextField mailField = new TextField();
        mailField.setPromptText("Mail");

        Button registerButton = new Button("Inscription");

        /**
         * Ajout d'une inscription
         */
        registerButton.setOnAction(event -> {
            try {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
                Matcher matcher = pattern.matcher(mailField.getText());
                if (!matcher.matches()) {
                    throw new InvalidMailException();
                }
                if (registerField.getText().length() <= 4  && mdpField.getText().length() <= 4) {
                    throw new BadEntryException();
                }
                DAOUser daoUser = new DAOUser();
                if (daoUser.checkifNotExistsUsernameAndMail(registerField.getText())) {
                    User user = new User();
                    user.setUsername(registerField.getText());
                    user.setPassword(mdpField.getText());
                    user.setMail(mailField.getText());
                    daoUser.insert(user);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur !");
                    alert.setContentText("Le pseudo ou le mail est déja pris");
                    alert.showAndWait();
                }
            }
            catch (BadEntryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Le pseudo ou le mot de passe est trop court");
                alert.showAndWait();
            }
            catch (InvalidMailException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Le mail est invalide");
                alert.showAndWait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });

        register.setPadding(new Insets(0,0,0,200));

        register.getChildren().addAll(registerField,mdpField,mailField,registerButton);

        center.add(login,0,0);
        center.add(register,1,0);

        root.setCenter(center);
    }
}