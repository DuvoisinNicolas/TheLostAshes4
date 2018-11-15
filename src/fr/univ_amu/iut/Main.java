package fr.univ_amu.iut;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import fr.univ_amu.iut.DAO.DAOCara;
import fr.univ_amu.iut.DAO.DAOSpell;
import fr.univ_amu.iut.DAO.DAOUser;
import fr.univ_amu.iut.Exceptions.BadEntryException;
import fr.univ_amu.iut.Exceptions.InvalidMailException;
import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Exceptions.NoUserException;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Spell;
import fr.univ_amu.iut.beans.User;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends Application {

    /**
     * Paramètres de l'utilisateur
     */

    private User user = new User();
    private BorderPane root = new BorderPane();

    /**
     * Répartition des stats
     */
    private static int MAXPTSALLOUER = 10;
    private static int PTSDEBASE = 1;

    /**
     * Hauteur et largeur de la fenètre
     */
    private int height = 600;
    private int width = 800;


    /**
     * Caracter
     */
    private Caracter cara = new Caracter();

    /**
     * Taille et police des titres et des textes
     */
    private Font fontTitle = new Font("Arial",20);
    private Font fontText = new Font("Arial",12);
    private Font fontSubText = new Font("Arial",10);

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

        /*
         * Bouton permettant de vérifier la connexion de l'utilisateur
         */
        connectButton.setOnAction(event -> {
            try {
                DAOUser daoUser = new DAOUser();
                user = daoUser.findByUsernameAndPwd(loginField.getText(),pwdField.getText());
                if (user.getIdCara() == 0) {
                    createCaraInterface();
                }
                else {
                    gameInterface();
                }
            }
            catch (NoConnectionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aucune connexion à internet");
                alert.setContentText("Merci de vous connecter à internet");
                alert.showAndWait();
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

        /*
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
                    createCaraInterface();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur !");
                    alert.setContentText("Le pseudo ou le mail est déja pris");
                    alert.showAndWait();
                }
            }
            catch (NoConnectionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aucune connexion à internet");
                alert.setContentText("Merci de vous connecter à internet" );
                alert.showAndWait();
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

    /**
     * Interface de création de perso et de répartition des stats
     */
    private void createCaraInterface () {

        IntegerProperty nombreRestant = new SimpleIntegerProperty(MAXPTSALLOUER);
        IntegerProperty forceProperty = new SimpleIntegerProperty(PTSDEBASE);
        IntegerProperty agiliteProperty = new SimpleIntegerProperty(PTSDEBASE);
        IntegerProperty endProperty = new SimpleIntegerProperty(PTSDEBASE);
        IntegerProperty charismeProperty = new SimpleIntegerProperty(PTSDEBASE);
        IntegerProperty magieProperty = new SimpleIntegerProperty(PTSDEBASE);
        root.getChildren().clear();

        VBox center = new VBox();
        center.setSpacing(5);
        center.setAlignment(Pos.CENTER);

        HBox choixDeNom = new HBox();
        choixDeNom.setAlignment(Pos.CENTER);
        Label nameText = new Label("Entrez votre nom :  ");
        TextField nameField = new TextField();
        choixDeNom.getChildren().addAll(nameText,nameField);

        HBox nbRestants = new HBox();
        Label nombreDePointsAAllouer = new Label("Nombre de points restants : ");
        Label nombreDePtsRestants = new Label();
        nombreDePtsRestants.textProperty().bind(nombreRestant.asString());
        nbRestants.setAlignment(Pos.CENTER);
        nbRestants.getChildren().addAll(nombreDePointsAAllouer,nombreDePtsRestants);

        HBox choixDeForce = new HBox();
        choixDeForce.setSpacing(40);
        Button moinsForce = new Button("-");
        Label forceText = new Label("Force");
        Label force = new Label();
        force.textProperty().bind(forceProperty.asString());
        Button plusForce = new Button("+");
        moinsForce.setOnAction(event -> {
            if (forceProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                forceProperty.set(forceProperty.get()-1);
                nombreRestant.set(nombreRestant.get()+1);
            }
        });
        plusForce.setOnAction(event -> {

            if (nombreRestant.get() > 0 && forceProperty.get() < 10)
            {
                forceProperty.set(forceProperty.get() + 1);
                nombreRestant.set(nombreRestant.get()-1);
            }
        });
        choixDeForce.setAlignment(Pos.CENTER);
        choixDeForce.getChildren().addAll(moinsForce,force,plusForce);


        HBox choixDeDexterite = new HBox();
        choixDeDexterite.setSpacing(40);
        Button moinsDexterite = new Button("-");
        Label dexteriteText = new Label("Dextérité");
        Label dexterite = new Label();
        dexterite.textProperty().bind(agiliteProperty.asString());
        Button plusDexterite = new Button("+");
        moinsDexterite.setOnAction(event -> {
            if (agiliteProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get())
            {
                agiliteProperty.set(agiliteProperty.get()-1);
                nombreRestant.set(nombreRestant.get()+1);
            }
        });
        plusDexterite.setOnAction(event -> {

            if (nombreRestant.get() > 0 && agiliteProperty.get() < 10)
            {
                agiliteProperty.set(agiliteProperty.get() + 1);
                nombreRestant.set(nombreRestant.get()-1);
            }
        });
        choixDeDexterite.setAlignment(Pos.CENTER);
        choixDeDexterite.getChildren().addAll(moinsDexterite,dexterite,plusDexterite);



        HBox choixDeEndurance = new HBox();
        choixDeEndurance.setSpacing(40);
        Button moinsEndurance = new Button("-");
        Label enduranceText = new Label("Endurance");
        Label endurance = new Label();
        endurance.textProperty().bind(endProperty.asString());
        Button plusEndurance = new Button("+");
        moinsEndurance.setOnAction(event -> {
            if (endProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get())
            {
                endProperty.set(endProperty.get()-1);
                nombreRestant.set(nombreRestant.get()+1);
            }
        });
        plusEndurance.setOnAction(event -> {

            if (nombreRestant.get() > 0 && endProperty.get() < 10)
            {
                endProperty.set(endProperty.get() + 1);
                nombreRestant.set(nombreRestant.get()-1);
            }
        });
        choixDeEndurance.setAlignment(Pos.CENTER);
        choixDeEndurance.getChildren().addAll(moinsEndurance,endurance,plusEndurance);


        HBox choixDeMagie = new HBox();
        choixDeMagie.setSpacing(40);
        Button moinsMagie = new Button("-");
        Label magieText = new Label("Magie");
        Label magie = new Label();
        magie.textProperty().bind(magieProperty.asString());
        Button plusMagie = new Button("+");
        moinsMagie.setOnAction(event -> {
            if (magieProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get())
            {
                magieProperty.set(magieProperty.get()-1);
                nombreRestant.set(nombreRestant.get()+1);
            }
        });
        plusMagie.setOnAction(event -> {

            if (nombreRestant.get() > 0 && magieProperty.get() < 10)
            {
                magieProperty.set(magieProperty.get() + 1);
                nombreRestant.set(nombreRestant.get()-1);
            }
        });
        choixDeMagie.setAlignment(Pos.CENTER);
        choixDeMagie.getChildren().addAll(moinsMagie,magie,plusMagie);

        HBox choixDeCharisme = new HBox();
        choixDeCharisme.setSpacing(40);
        Button moinsCharisme = new Button("-");
        Label charismeText = new Label("Charisme");
        Label charisme = new Label();
        charisme.textProperty().bind(charismeProperty.asString());
        Button plusCharisme = new Button("+");
        moinsCharisme.setOnAction(event -> {
            if (charismeProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get())
            {
                charismeProperty.set(charismeProperty.get()-1);
                nombreRestant.set(nombreRestant.get()+1);
            }
        });
        plusCharisme.setOnAction(event -> {
            if (nombreRestant.get() > 0 && charismeProperty.get() < 10)
            {
                charismeProperty.set(charismeProperty.get() + 1);
                nombreRestant.set(nombreRestant.get()-1);
            }
        });
        choixDeCharisme.setAlignment(Pos.CENTER);
        choixDeCharisme.getChildren().addAll(moinsCharisme,charisme,plusCharisme);

        nbRestants.setPadding(new Insets(30,0,0,0));
        forceText.setPadding(new Insets(30,0,0,0));
        dexteriteText.setPadding(new Insets(30,0,0,0));
        magieText.setPadding(new Insets(30,0,0,0));
        enduranceText.setPadding(new Insets(30,0,0,0));
        charismeText.setPadding(new Insets(30,0,0,0));

        HBox stats = new HBox();
        stats.setAlignment(Pos.CENTER);
        stats.setSpacing(100);
        VBox left = new VBox();
        left.setAlignment(Pos.CENTER);
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER);
        stats.getChildren().addAll(left,right);


        left.getChildren().addAll(forceText,choixDeForce,dexteriteText,choixDeDexterite);
        right.getChildren().addAll(enduranceText,choixDeEndurance,magieText,choixDeMagie);
        choixDeCharisme.setPadding(new Insets(0,0,50,0));

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText("Encore des points !");
        alert.setContentText("Il reste des points à allouer :)");
        Button choixArmes = new Button("Suivant");
        choixArmes.setOnAction(event -> {
            if (nombreRestant.get() == 0)
            {
                if (nameField.getText().length() >= 4){
                    cara.setName(nameField.getText());
                    cara.setFCE(forceProperty.get());
                    cara.setAGI(agiliteProperty.get());
                    cara.setCHARI(charismeProperty.get());
                    cara.setEND(endProperty.get());
                    cara.setMAG(magieProperty.get());
                    try {
                        interfaceChoixSorts();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Erreur");
                    alert2.setContentText("Le pseudo est trop court");
                    alert2.showAndWait();
                }
            }
            else
                alert.showAndWait();
        });

        center.getChildren().addAll(choixDeNom,nbRestants,stats,charismeText,choixDeCharisme,choixArmes);
        root.setCenter(center);
    }

    private void interfaceChoixSorts () throws SQLException {
        root.getChildren().clear();
        try {

            // Titre en haut de page
        HBox top = new HBox();
        Label title = new Label("Choix des sorts");
        title.setPadding(new Insets(0,0,20,0));
        title.setFont(fontTitle);
        VBox sorts = new VBox();
        sorts.setAlignment(Pos.CENTER);
        Label nbPoints = new Label("Nombre de sorts à choisir :");
        nbPoints.setFont(fontText);
        Label valPoints = new Label();
        valPoints.setFont(fontText);

        IntegerProperty nbSpellPoints = new SimpleIntegerProperty(5);

        valPoints.textProperty().bind(nbSpellPoints.asString());

        top.setPadding(new Insets(10,0,0,0));
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(nbPoints,valPoints);
        sorts.getChildren().addAll(title,top);
        root.setTop(sorts);

        GridPane spells = new GridPane();
        spells.setPadding(new Insets(20,0,0,0));
        int column = 0;
        int row = 0;
        List<Spell> spellNames = new ArrayList<>();
            DAOSpell daoSpell = new DAOSpell();
            List<Spell> spellList = daoSpell.findAll();
            for (Spell spell : spellList) {
                VBox spellBox = new VBox();
                spellBox.setPadding(new Insets(20,0,0,100));
                Label spellLabel = new Label(spell.getName());
                spellLabel.setFont(fontText);
                Label spellDesc = new Label(spell.getDescr());
                spellDesc.setFont(fontSubText);
                CheckBox checkBox = new CheckBox();
                checkBox.setOnAction(event -> {
                    if (checkBox.selectedProperty().get()) {
                        spellNames.add(spell);
                        if (nbSpellPoints.get() <= 0) {
                            spellNames.remove(spell);
                            checkBox.setSelected(false);
                            nbSpellPoints.set(nbSpellPoints.get()+1);
                        }
                        nbSpellPoints.set(nbSpellPoints.get()-1);
                    }
                    else {
                        spellNames.remove(spell);
                        checkBox.setSelected(false);
                        nbSpellPoints.set(nbSpellPoints.get()+1);
                    }
                });

                spellBox.getChildren().addAll(spellLabel,spellDesc);
                spells.add(spellBox,column,row);
                spells.add(checkBox,column+1,row);
                ++column;
                ++column;
                if (column == 4) {
                    column = 0;
                    ++row;
                }
            }
            Button benjamin = new Button("C'est parti !");
            benjamin.setOnAction(event -> {
                try {
                    DAOCara daoCara = new DAOCara();
                    daoCara.insert(cara,user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Spell spell : spellNames) {
                    try {
                        daoSpell.learnSpell(spell,cara,user);
                    } catch (SQLException e) {
                        System.out.println("Euuuuh c'est cassé");
                    }
                }
            });
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(spells,benjamin);
            root.setCenter(vBox);
        }
        catch (NoConnectionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune connexion à internet");
            alert.setContentText("Merci de vous connecter à internet");
            alert.showAndWait();
        }
    }
    private void gameInterface () {

    }
}