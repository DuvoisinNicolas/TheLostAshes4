package fr.univ_amu.iut;

import fr.univ_amu.iut.DAO.DAOCara;
import fr.univ_amu.iut.DAO.DAOMap;
import fr.univ_amu.iut.DAO.DAOSpell;
import fr.univ_amu.iut.DAO.DAOUser;
import fr.univ_amu.iut.Exceptions.*;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Map;
import fr.univ_amu.iut.beans.Spell;
import fr.univ_amu.iut.beans.User;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
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
    private static int MAXPTSALLOUER = 4;
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
    private Font fontTitle = new Font("DejaVu Sans",20);
    private Font fontText = new Font("DejaVu Sans",12);
    private Font fontSubText = new Font("DejaVu Sans",10);

    /**
     *  Caractere de separation et fichier de base
     */
    private static char caractereSeparation;
    private static String filepath = "file:" + new File("").getAbsolutePath();
    private static String imagePath;

    private static ArrayList<Map> allMaps = new ArrayList<>();

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            caractereSeparation = '\\';
        }
        else if (os.contains("mac")) {
            caractereSeparation = '/';
        }
        else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            caractereSeparation = '/';
        }

        filepath +=  caractereSeparation + "src" + caractereSeparation +  "fr" + caractereSeparation + "univ_amu" + caractereSeparation + "iut" + caractereSeparation;
        imagePath = filepath + "images" + caractereSeparation;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws NoConnectionException, SQLException {
        DAOMap daoMap = new DAOMap();
        allMaps = (ArrayList<Map>) daoMap.findAll();
        primaryStage.setTitle("The Lost Ashes");
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(width);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.setMinHeight(height);
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
        root.setBackground(new Background(new BackgroundImage(new Image(imagePath + "0.png",800,600,false,false),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));

        // Titre en haut de page
        HBox top = new HBox();
        Label title = new Label("The Lost Ashes");
        title.setFont(fontTitle);
        top.setPadding(new Insets(50,0,0,0));
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
        loginField.setText("totoo");

        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Mot de Passe");
        pwdField.setText("totoo");

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
                    DAOCara daoCara = new DAOCara();
                    cara = daoCara.getMyCara(user);
                    /**
                     * CHANGE ICI OMFG !
                     */
                    gameInterface(allMaps.get(0));
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

        root.getChildren().clear();
        root.setBackground(new Background(new BackgroundImage(new Image(imagePath + "0.png",800,600,false,false),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));

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
        try {

            root.getChildren().clear();
            root.setBackground(new Background(new BackgroundImage(new Image(imagePath + "0.png",800,600,false,false),
                    BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));

            root.getChildren().clear();

            // Titre en haut de page
            HBox top = new HBox();
            Label title = new Label("Choix des sorts");
            title.setPadding(new Insets(0,0,20,0));
            title.setFont(fontTitle);
            VBox sorts = new VBox();
            sorts.setAlignment(Pos.CENTER);
            Label nbPoints = new Label("Nombre de sorts à choisir :");
            nbPoints.setFont(fontText);
            nbPoints.setPadding(new Insets(0,5,0,0));
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
            int column = 0;
            int row = 0;
            List<Spell> spellNames = new ArrayList<>();
            DAOSpell daoSpell = new DAOSpell();
            List<Spell> spellList = daoSpell.findAll();
            for (Spell spell : spellList) {
                VBox spellBox = new VBox();
                spellBox.setPadding(new Insets(0,0,0,width/9.0));
                spellBox.setAlignment(Pos.CENTER);
                Label spellLabel = new Label(spell.getName());
                spellLabel.setFont(fontText);
                Label spellDesc = new Label(spell.getDescr());
                spellDesc.setPadding(new Insets(0,5,20,0));
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
                    cara = daoCara.insert(cara,user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Spell spell : spellNames) {
                    try {
                        DAOCara daoCara = new DAOCara();
                        daoSpell.learnSpell(spell,cara.getIdCara());
                        daoSpell.learnSpell(spell,daoCara.getSaveUser(cara).getIdCara());
                    } catch (SQLException e) {
                        System.out.println("Euuuuh c'est cassé");
                    } catch (NoUserException e) {
                        e.printStackTrace();
                    } catch (NoConnectionException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Aucune connexion à internet");
                        alert.setContentText("Merci de vous connecter à internet");
                        alert.showAndWait();
                        try {
                            interfaceChoixSorts();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    gameInterface(allMaps.get(0));
                }
            });
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(spells,benjamin);
            vBox.setPadding(new Insets(0,0,0,0));
            root.setCenter(vBox);
        }
        catch (NoConnectionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune connexion à internet");
            alert.setContentText("Merci de vous connecter à internet");
            alert.showAndWait();
            interfaceChoixSorts();
        }
    }

    private void gameInterface (Map map) {
        try {
            DAOUser daoUser = new DAOUser();
            root.getChildren().clear();

            //Permet de choisir le fond d'écran qu'il faut

            int cpt = 0;

            if (map.getChoix1() != null) {
                ++cpt;
            }
            if (map.getChoix2() != null) {
                ++cpt;
            }
            if (map.getChoix3() != null) {
                ++cpt;
            }
            if (map.getChoix4() != null) {
                ++cpt;
            }

            String filePath;
            switch (cpt) {
                case 1:
                    filePath = "1.png";
                    break;
                case 2:
                    filePath = "2.png";
                    break;
                case 3:
                    filePath = "3.png";
                    break;
                case 4:
                    filePath = "4.png";
                    break;
                 default:
                     throw new MapErrorException();
            }

            // Initialisation du fond d'écran
            root.setBackground(new Background(new BackgroundImage(new Image(imagePath + filePath,800,600,false,false),
                    BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));

            // Gauche
            VBox left = new VBox();
            left.setSpacing(20);
            left.setAlignment(Pos.CENTER_LEFT);
            left.setPadding(new Insets(2,0,0,20));

                //Pseudo
            Label usernameText = new Label(cara.getName());
            usernameText.setFont(fontText);

                // HP
            HBox hp = new HBox();
            ImageView hpImage = new ImageView(new Image(imagePath + "HP.png"));
            hpImage.setFitHeight(20);
            hpImage.setFitWidth(20);

            Label hpText = new Label();
            hpText.setFont(fontText);
            hpText.setPadding(new Insets(2,0,0,5));
            hpText.textProperty().bind(cara.getCURRHP().asString());

            Label slash = new Label(" / ");

            Label hpMaxText = new Label();
            hpMaxText.setFont(fontText);
            hpMaxText.setPadding(new Insets(2,0,0,5));
            hpMaxText.textProperty().bind(cara.getHP().asString());
            hp.getChildren().addAll(hpImage,hpText,slash,hpMaxText);

                // Force
            HBox force = new HBox();
            ImageView forceImage = new ImageView(new Image(imagePath + "FCE.png"));
            forceImage.setFitHeight(20);
            forceImage.setFitWidth(20);

            Label forceText = new Label();
            forceText.setFont(fontText);
            forceText.setPadding(new Insets(2,0,0,5));
            forceText.textProperty().bind(cara.getFCE().asString());
            force.getChildren().addAll(forceImage,forceText);

               // Agilité
            HBox agilite = new HBox();
            ImageView agiliteImage = new ImageView(new Image(imagePath + "AGI.png"));
            agiliteImage.setFitHeight(20);
            agiliteImage.setFitWidth(20);

            Label agiliteText = new Label();
            agiliteText.setPadding(new Insets(2,0,0,5));
            agiliteText.setFont(fontText);
            agiliteText.textProperty().bind(cara.getAGI().asString());
            agilite.getChildren().addAll(agiliteImage,agiliteText);

                // Intelligence
            HBox intel = new HBox();
            ImageView intelImage = new ImageView(new Image(imagePath + "INT.png"));
            intelImage.setFitHeight(20);
            intelImage.setFitWidth(20);

            Label intelText = new Label();
            intelText.setFont(fontText);
            intelText.setPadding(new Insets(2,0,0,5));
            intelText.textProperty().bind(cara.getMAG().asString());
            intel.getChildren().addAll(intelImage,intelText);

                // Endurance
            HBox end = new HBox();
            ImageView endImage = new ImageView(new Image(imagePath + "END.png"));
            endImage.setFitHeight(20);
            endImage.setFitWidth(20);

            Label endText = new Label();
            endText.setPadding(new Insets(2,0,0,5));
            endText.setFont(fontText);
            endText.textProperty().bind(cara.getEND().asString());
            end.getChildren().addAll(endImage,endText);

                // Charisme
            HBox chari = new HBox();
            ImageView chariImage = new ImageView(new Image(imagePath + "CHARI.png"));
            chariImage.setFitHeight(20);
            chariImage.setFitWidth(20);

            Label chariText = new Label();
            chariText.setFont(fontText);
            chariText.setPadding(new Insets(2,0,0,5));
            chariText.textProperty().bind(cara.getCHARI().asString());
            chari.getChildren().addAll(chariImage,chariText);

            left.getChildren().addAll(usernameText,hp,force,agilite,intel,end,chari);


            // Haut
            VBox top = new VBox();
            Label topText = new Label(map.getName());
            topText.setFont(fontTitle);
            top.getChildren().add(topText);
            top.setAlignment(Pos.CENTER);
            top.setPadding(new Insets(20,0,0,width/4.9));
            if (map.isCheckpoint()) {
                Label checkpoint = new Label("<Checkpoint>");
                checkpoint.setFont(fontSubText);
                top.getChildren().add(checkpoint);
            }


            // Centre
            VBox center = new VBox();
            center.setAlignment(Pos.CENTER);
            Text text = new Text(map.getText());
            text.setFont(fontText);
            text.setLineSpacing(3);
            text.setWrappingWidth(500);
            center.getChildren().add(text);
            center.setPadding(new Insets(0,0,0,60));

            root.setCenter(center);
            root.setTop(top);
            root.setLeft(left);

            // Bottom
            StackPane button1 = new StackPane();
            StackPane button2 = new StackPane();
            StackPane button3 = new StackPane();
            StackPane button4 = new StackPane();

            Rectangle rectangle1 = new Rectangle();
            Rectangle rectangle2 = new Rectangle();
            Rectangle rectangle3 = new Rectangle();
            Rectangle rectangle4 = new Rectangle();

            rectangle1.setHeight(50);
            rectangle1.setWidth(240);
            rectangle1.setFill(Color.TRANSPARENT);
            rectangle2.setHeight(50);
            rectangle2.setWidth(240);
            rectangle2.setFill(Color.TRANSPARENT);
            rectangle3.setHeight(50);
            rectangle3.setWidth(240);
            rectangle3.setFill(Color.TRANSPARENT);
            rectangle4.setHeight(50);
            rectangle4.setWidth(240);
            rectangle4.setFill(Color.TRANSPARENT);

            button1.setAlignment(Pos.CENTER);
            button2.setAlignment(Pos.CENTER);
            button3.setAlignment(Pos.CENTER);
            button4.setAlignment(Pos.CENTER);

            Label label1 = new Label(map.getChoix1());
            Label label2 = new Label("");
            Label label3 = new Label("");
            Label label4 = new Label("");

            button1.getChildren().addAll(label1,rectangle1);

            rectangle1.setOnMouseClicked(event -> {
                /**
                 * TODO : Rediriger vers la map correspondante
                 */
            });

            rectangle1.setOnMouseEntered(event -> {
                rectangle1.setCursor(Cursor.HAND);
            });


            if (cpt == 2) {
                label2 = new Label(map.getChoix2());
                rectangle2.setOnMouseEntered(event -> {
                    rectangle2.setCursor(Cursor.HAND);
                });
            }
            else if (cpt == 3){
                label2 = new Label(map.getChoix2());
                label3 = new Label(map.getChoix3());

                rectangle2.setOnMouseEntered(event -> {
                    rectangle2.setCursor(Cursor.HAND);
                });

                rectangle3.setOnMouseEntered(event -> {
                    rectangle3.setCursor(Cursor.HAND);
                });
            }
            else if (cpt != 1){
                label2 = new Label(map.getChoix2());
                label3 = new Label(map.getChoix3());
                label4 = new Label(map.getChoix4());
                rectangle2.setOnMouseEntered(event -> {
                    rectangle2.setCursor(Cursor.HAND);
                });

                rectangle3.setOnMouseEntered(event -> {
                    rectangle3.setCursor(Cursor.HAND);
                });

                rectangle4.setOnMouseEntered(event -> {
                    rectangle4.setCursor(Cursor.HAND);
                });
            }

            button2.getChildren().addAll(label2,rectangle2);
            button3.getChildren().addAll(label3,rectangle3);
            button4.getChildren().addAll(label4,rectangle4);

            HBox ligneDuHaut = new HBox();
            ligneDuHaut.setPadding(new Insets(0,0,21,0));
            ligneDuHaut.getChildren().addAll(button1,button2);
            HBox ligneDuBas = new HBox();
            button2.setPadding(new Insets(0,0,0,96));
            button4.setPadding(new Insets(0,0,0,96));
            ligneDuBas.setPadding(new Insets(0,0,24,0));
            ligneDuBas.getChildren().addAll(button3,button4);
            VBox bottom = new VBox();
            bottom.setPadding(new Insets(0,0,0,189));
            bottom.getChildren().addAll(ligneDuHaut,ligneDuBas);


            root.setBottom(bottom);
        }
        catch (NoConnectionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune connexion à internet");
            alert.setContentText("Merci de vous connecter à internet");
            alert.showAndWait();
            gameInterface(map);
        } catch (MapErrorException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Les développeurs sont des tâches");
            alert.setContentText("On as mal initialisé la map, sorry :'(");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}