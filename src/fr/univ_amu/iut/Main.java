package fr.univ_amu.iut;

import fr.univ_amu.iut.DAO.*;
import fr.univ_amu.iut.Exceptions.*;
import fr.univ_amu.iut.beans.*;
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
import javafx.util.Pair;

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
    private Font fontTitle = new Font("DejaVu Sans", 20);
    private Font fontSubTitle = new Font("DejaVu Sans",16);
    private Font fontText = new Font("DejaVu Sans", 12);
    private Font fontSubText = new Font("DejaVu Sans", 10);

    /**
     * Caractere de separation et fichier de base
     */
    private static char caractereSeparation;

    /**
     * Valeur de HP soignée au repos
     */
    private static int HP_REPOS = DAOCara.VALHPMAX / 2;


    /**
     * Inventaire
     */
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            caractereSeparation = '\\';
        } else if (os.contains("mac")) {
            caractereSeparation = '/';
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            caractereSeparation = '/';
        }

        launch(args);
    }

    private void initGame() throws NoConnectionException, SQLException {
        // Ajout des armes , armures , maps
        DAOMap daoMap = new DAOMap();
        DAOWeapon daoWeapon = new DAOWeapon();
        DAOArmor daoArmor = new DAOArmor();
        DAOSpell daoSpell = new DAOSpell();
        DAOItem daoItem = new DAOItem();
        DAOEnnemi daoEnnemi = new DAOEnnemi();

        Map.setAllMaps(daoMap.findAll());
        Weapon.setAllWeapons(daoWeapon.findAll());
        Armor.setAllArmors(daoArmor.findAll());
        Spell.setAllSpells(daoSpell.findAll());
        Item.setAllItems(daoItem.findAll());
        Ennemi.setAllEnnemis(daoEnnemi.findAll());

    }

    private void initCaracter() throws NoConnectionException, SQLException, NoItemFoundException, NoMapFoundException {
        DAOWeapon daoWeapon = new DAOWeapon();
        DAOSpell daoSpell = new DAOSpell();
        DAOArmor daoArmor = new DAOArmor();
        DAOCara daoCara = new DAOCara();
        DAOItem daoItem = new DAOItem();

        cara = daoCara.getMyCara(user);
        cara.setArmor(daoArmor.getEquipedArmor(cara));
        cara.setArmors(daoArmor.getMyArmors(cara));
        cara.setWeapons(daoWeapon.getMyWeapons(cara));
        cara.setWeapon(daoWeapon.getEquipedWeapon(cara));
        cara.setSpells(daoSpell.findByCara(cara));
        cara.setItems(daoItem.findCaraItems(cara));
    }

    @Override
    public void start(Stage primaryStage) throws NoConnectionException, SQLException {

        initGame();
        primaryStage.setTitle("The Lost Ashes");
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(width);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.setMinHeight(height);
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        connectionInterface();
        primaryStage.show();
    }

    /**
     * Interface de connexion
     */
    private void connectionInterface() {
        root.getChildren().clear();
        root.setBackground(new Background(new BackgroundImage(new Image("0.png", 800, 600, false, false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        // Titre en haut de page
        HBox top = new HBox();
        Label title = new Label("The Lost Ashes");
        title.setFont(fontTitle);
        top.setPadding(new Insets(50, 0, 0, 0));
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
                DAOCara daoCara = new DAOCara();
                DAOUser daoUser = new DAOUser();
                user = daoUser.findByUsernameAndPwd(loginField.getText(), pwdField.getText());
                if (!daoCara.hasACara(user)) {
                    createCaraInterface();
                } else {
                    initCaracter();
                    gameInterface(Map.getAllMaps().get(0));
                }
            } catch (NoConnectionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aucune connexion à internet");
                alert.setContentText("Merci de vous connecter à internet");
                alert.showAndWait();
            } catch (NoUserException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Mauvais identifiants !");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        center.setPadding(new Insets(150, 0, 0, 150));
        login.getChildren().addAll(loginField, pwdField, connectButton);

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
                if (registerField.getText().length() <= 4 && mdpField.getText().length() <= 4) {
                    throw new BadEntryException();
                }
                DAOUser daoUser = new DAOUser();
                if (daoUser.checkifNotExistsUsernameAndMail(registerField.getText())) {
                    User user = new User();
                    user.setUsername(registerField.getText());
                    user.setPassword(mdpField.getText());
                    user.setMail(mailField.getText());
                    daoUser.insert(user);
                    this.user = daoUser.findByUsernameAndPwd(registerField.getText(), mdpField.getText());
                    createCaraInterface();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur !");
                    alert.setContentText("Le pseudo ou le mail est déja pris");
                    alert.showAndWait();
                }
            } catch (NoConnectionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aucune connexion à internet");
                alert.setContentText("Merci de vous connecter à internet");
                alert.showAndWait();
            } catch (BadEntryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Le pseudo ou le mot de passe est trop court");
                alert.showAndWait();
            } catch (InvalidMailException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur !");
                alert.setContentText("Le mail est invalide");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        register.setPadding(new Insets(0, 0, 0, 200));

        register.getChildren().addAll(registerField, mdpField, mailField, registerButton);

        center.add(login, 0, 0);
        center.add(register, 1, 0);

        root.setCenter(center);
    }

    /**
     * Interface de création de perso et de répartition des stats
     */
    private void createCaraInterface() {

        root.getChildren().clear();
        root.setBackground(new Background(new BackgroundImage(new Image("0.png", 800, 600, false, false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

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
        choixDeNom.getChildren().addAll(nameText, nameField);

        HBox nbRestants = new HBox();
        Label nombreDePointsAAllouer = new Label("Nombre de points restants : ");
        Label nombreDePtsRestants = new Label();
        nombreDePtsRestants.textProperty().bind(nombreRestant.asString());
        nbRestants.setAlignment(Pos.CENTER);
        nbRestants.getChildren().addAll(nombreDePointsAAllouer, nombreDePtsRestants);

        HBox choixDeForce = new HBox();
        choixDeForce.setSpacing(40);
        Button moinsForce = new Button("-");
        Label forceText = new Label("Force");
        Label force = new Label();
        force.textProperty().bind(forceProperty.asString());
        Button plusForce = new Button("+");
        moinsForce.setOnAction(event -> {
            if (forceProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                forceProperty.set(forceProperty.get() - 1);
                nombreRestant.set(nombreRestant.get() + 1);
            }
        });
        plusForce.setOnAction(event -> {

            if (nombreRestant.get() > 0 && forceProperty.get() < 10) {
                forceProperty.set(forceProperty.get() + 1);
                nombreRestant.set(nombreRestant.get() - 1);
            }
        });
        choixDeForce.setAlignment(Pos.CENTER);
        choixDeForce.getChildren().addAll(moinsForce, force, plusForce);


        HBox choixDeDexterite = new HBox();
        choixDeDexterite.setSpacing(40);
        Button moinsDexterite = new Button("-");
        Label dexteriteText = new Label("Dextérité");
        Label dexterite = new Label();
        dexterite.textProperty().bind(agiliteProperty.asString());
        Button plusDexterite = new Button("+");
        moinsDexterite.setOnAction(event -> {
            if (agiliteProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                agiliteProperty.set(agiliteProperty.get() - 1);
                nombreRestant.set(nombreRestant.get() + 1);
            }
        });
        plusDexterite.setOnAction(event -> {

            if (nombreRestant.get() > 0 && agiliteProperty.get() < 10) {
                agiliteProperty.set(agiliteProperty.get() + 1);
                nombreRestant.set(nombreRestant.get() - 1);
            }
        });
        choixDeDexterite.setAlignment(Pos.CENTER);
        choixDeDexterite.getChildren().addAll(moinsDexterite, dexterite, plusDexterite);


        HBox choixDeEndurance = new HBox();
        choixDeEndurance.setSpacing(40);
        Button moinsEndurance = new Button("-");
        Label enduranceText = new Label("Endurance");
        Label endurance = new Label();
        endurance.textProperty().bind(endProperty.asString());
        Button plusEndurance = new Button("+");
        moinsEndurance.setOnAction(event -> {
            if (endProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                endProperty.set(endProperty.get() - 1);
                nombreRestant.set(nombreRestant.get() + 1);
            }
        });
        plusEndurance.setOnAction(event -> {

            if (nombreRestant.get() > 0 && endProperty.get() < 10) {
                endProperty.set(endProperty.get() + 1);
                nombreRestant.set(nombreRestant.get() - 1);
            }
        });
        choixDeEndurance.setAlignment(Pos.CENTER);
        choixDeEndurance.getChildren().addAll(moinsEndurance, endurance, plusEndurance);


        HBox choixDeMagie = new HBox();
        choixDeMagie.setSpacing(40);
        Button moinsMagie = new Button("-");
        Label magieText = new Label("Magie");
        Label magie = new Label();
        magie.textProperty().bind(magieProperty.asString());
        Button plusMagie = new Button("+");
        moinsMagie.setOnAction(event -> {
            if (magieProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                magieProperty.set(magieProperty.get() - 1);
                nombreRestant.set(nombreRestant.get() + 1);
            }
        });
        plusMagie.setOnAction(event -> {

            if (nombreRestant.get() > 0 && magieProperty.get() < 10) {
                magieProperty.set(magieProperty.get() + 1);
                nombreRestant.set(nombreRestant.get() - 1);
            }
        });
        choixDeMagie.setAlignment(Pos.CENTER);
        choixDeMagie.getChildren().addAll(moinsMagie, magie, plusMagie);

        HBox choixDeCharisme = new HBox();
        choixDeCharisme.setSpacing(40);
        Button moinsCharisme = new Button("-");
        Label charismeText = new Label("Charisme");
        Label charisme = new Label();
        charisme.textProperty().bind(charismeProperty.asString());
        Button plusCharisme = new Button("+");
        moinsCharisme.setOnAction(event -> {
            if (charismeProperty.get() > 1 && MAXPTSALLOUER > nombreRestant.get()) {
                charismeProperty.set(charismeProperty.get() - 1);
                nombreRestant.set(nombreRestant.get() + 1);
            }
        });
        plusCharisme.setOnAction(event -> {
            if (nombreRestant.get() > 0 && charismeProperty.get() < 10) {
                charismeProperty.set(charismeProperty.get() + 1);
                nombreRestant.set(nombreRestant.get() - 1);
            }
        });
        choixDeCharisme.setAlignment(Pos.CENTER);
        choixDeCharisme.getChildren().addAll(moinsCharisme, charisme, plusCharisme);

        nbRestants.setPadding(new Insets(30, 0, 0, 0));
        forceText.setPadding(new Insets(30, 0, 0, 0));
        dexteriteText.setPadding(new Insets(30, 0, 0, 0));
        magieText.setPadding(new Insets(30, 0, 0, 0));
        enduranceText.setPadding(new Insets(30, 0, 0, 0));
        charismeText.setPadding(new Insets(30, 0, 0, 0));

        HBox stats = new HBox();
        stats.setAlignment(Pos.CENTER);
        stats.setSpacing(100);
        VBox left = new VBox();
        left.setAlignment(Pos.CENTER);
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER);
        stats.getChildren().addAll(left, right);


        left.getChildren().addAll(forceText, choixDeForce, dexteriteText, choixDeDexterite);
        right.getChildren().addAll(enduranceText, choixDeEndurance, magieText, choixDeMagie);
        choixDeCharisme.setPadding(new Insets(0, 0, 50, 0));

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText("Encore des points !");
        alert.setContentText("Il reste des points à allouer :)");
        Button choixArmes = new Button("Suivant");
        choixArmes.setOnAction(event -> {
            if (nombreRestant.get() == 0) {
                if (nameField.getText().length() >= 4) {
                    cara.setName(nameField.getText());
                    cara.setFCE(forceProperty.get());
                    cara.setAGI(agiliteProperty.get());
                    cara.setCHARI(charismeProperty.get());
                    cara.setEND(endProperty.get());
                    cara.setMAG(magieProperty.get());
                    interfaceChoixSorts();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Erreur");
                    alert2.setContentText("Le pseudo est trop court");
                    alert2.showAndWait();
                }
            } else
                alert.showAndWait();
        });

        center.getChildren().addAll(choixDeNom, nbRestants, stats, charismeText, choixDeCharisme, choixArmes);
        root.setCenter(center);
    }

    /**
     * Interface de choix des sorts
     */
    private void interfaceChoixSorts() {
        try {

            DAOCara daoCara = new DAOCara();

            root.getChildren().clear();
            root.setBackground(new Background(new BackgroundImage(new Image("0.png", 800, 600, false, false),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

            // Titre en haut de page
            HBox top = new HBox();
            Label title = new Label("Choix des sorts");
            title.setPadding(new Insets(10, 0, 20, 0));
            title.setFont(fontTitle);
            VBox sorts = new VBox();
            sorts.setAlignment(Pos.CENTER);
            Label nbPoints = new Label("Nombre de sorts à choisir :");
            nbPoints.setFont(fontText);
            nbPoints.setPadding(new Insets(0, 5, 0, 0));
            Label valPoints = new Label();
            valPoints.setFont(fontText);

            IntegerProperty nbSpellPoints = new SimpleIntegerProperty(5);

            valPoints.textProperty().bind(nbSpellPoints.asString());

            top.setPadding(new Insets(10, 0, 0, 0));
            top.setAlignment(Pos.CENTER);
            top.getChildren().addAll(nbPoints, valPoints);
            sorts.getChildren().addAll(title, top);
            root.setTop(sorts);

            GridPane spells = new GridPane();
            int column = 0;
            int row = 0;
            List<Spell> chosenSpells = new ArrayList<>();
            for (Spell spell : Spell.getAllSpells()) {
                VBox spellBox = new VBox();
                spellBox.setPadding(new Insets(0, 0, 0, width / 9.0));
                spellBox.setAlignment(Pos.CENTER);
                Label spellLabel = new Label(spell.getName());
                spellLabel.setFont(fontText);
                Label spellDesc = new Label(spell.getDescr());
                spellDesc.setPadding(new Insets(0, 5, 20, 0));
                spellDesc.setFont(fontSubText);
                CheckBox checkBox = new CheckBox();
                checkBox.setOnAction(event -> {
                    System.out.println(spell.getName());
                    if (checkBox.selectedProperty().get()) {
                        chosenSpells.add(spell);
                        if (nbSpellPoints.get() <= 0) {
                            chosenSpells.remove(spell);
                            checkBox.setSelected(false);
                            nbSpellPoints.set(nbSpellPoints.get() + 1);
                        }
                        nbSpellPoints.set(nbSpellPoints.get() - 1);
                    } else {
                        chosenSpells.remove(spell);
                        checkBox.setSelected(false);
                        nbSpellPoints.set(nbSpellPoints.get() + 1);
                    }
                });

                spellBox.getChildren().addAll(spellLabel, spellDesc);
                spells.add(spellBox, column, row);
                spells.add(checkBox, column + 1, row);
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
                    cara.setSpells(chosenSpells);
                    cara.setWeapon(Weapon.findWeaponById(1));
                    cara.setArmor(Armor.findArmorById(1));
                    cara.setCurrentMap(Map.getAllMaps().get(0));
                    cara = daoCara.insert(cara,user);
                    cara = daoCara.getMyCara(user);

                    gameInterface(cara.getCurrentMap());

                } catch (SQLException | NoWeaponFoundException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Aucune connexion à internet");
                    alert.setContentText("Merci de vous connecter à internet");
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(spells, benjamin);
            vBox.setPadding(new Insets(0, 0, 0, 0));
            root.setCenter(vBox);
        } catch (NoConnectionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune connexion à internet");
            alert.setContentText("Merci de vous connecter à internet");
            alert.showAndWait();
            interfaceChoixSorts();
        }
    }

    /**
     * Edite le texte de la map pour remplacer les mot clefs
     *
     * @param map Map actuelle
     * @return String
     */
    private String editTextMap(Map map) throws NoWeaponFoundException, NoArmorFoundException {
        String betterStr = map.getText().replace("#NOM", cara.getName());
        if (map.getIdWeapon() != 0) {
            betterStr = betterStr.replace("#ARME",Weapon.findWeaponById(map.getIdWeapon()).getName());
        }
        if (map.getIdArmor() != 0) {
            betterStr = betterStr.replace("#ARMURE",Armor.findArmorById(map.getIdArmor()).getName());
        }
        /*
         * TODO:Replacer le texte encore
         */
        return betterStr;
    }

    /**
     * Effectue les changements à faire
     * @param map Map actuelle
     */
    private void modifsStatsAndGolds(Map map) {
        if (map.getFCE() != 0) {
            cara.setFCE(cara.getFCE().get() + map.getFCE());
            if (cara.getFCE().get() > 10)
                cara.setFCE(10);
        }
        if (map.getAGI() != 0) {
            cara.setAGI(cara.getAGI().get() + map.getAGI());
            if (cara.getAGI().get() > 10)
                cara.setAGI(10);
        }
        if (map.getINT() != 0) {
            cara.setMAG(cara.getMAG().get() + map.getINT());
            if (cara.getMAG().get() > 10)
                cara.setMAG(10);
        }
        if (map.getEND() != 0) {
            cara.setEND(cara.getEND().get() + map.getEND());
            if (cara.getEND().get() > 10)
                cara.setEND(10);
        }
        if (map.getCHARI() != 0) {
            cara.setCHARI(cara.getCHARI().get() + map.getCHARI());
            if (cara.getCHARI().get() > 10)
                cara.setCHARI(10);
        }
        if (map.getGolds() != 0) {
            cara.setGolds(cara.getGolds().get() + map.getGolds());
        }
    }


    private boolean checkItem(Map map) {
        for (Pair<Item,Integer> item : cara.getItems()) {
            if (item.getKey().getIdItem() == map.getIdRequiredItem()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSpell(Map map) {
        for (Spell spell : cara.getSpells()) {
            if (spell.getIdSpell() == map.getIdRequiredSpell()) {
                return true;
            }
        }
        return false;
    }


    private boolean checkIfEnoughtMoney(Map map, int numero) throws NoMapFoundException {
        switch (numero) {
            case 1:
                Map map1 = Map.findMapById(map.getMap1());
                if (cara.getGolds().get() + map1.getGolds() < 0) {
                    return false;
                }
                break;
            case 2:
                if (map.getMap2() != 0) {
                    Map map2 = Map.findMapById(map.getMap2());
                    if (cara.getGolds().get() + map2.getGolds() < 0) {
                        return false;
                    }
                }
                break;
            case 3:
                if (map.getMap3() != 0) {
                    Map map3 = Map.findMapById(map.getMap3());
                    if (cara.getGolds().get() + map3.getGolds() < 0) {
                        return false;
                    }
                }
                break;
            case 4:
                if (map.getMap4() != 0) {
                    Map map4 = Map.findMapById(map.getMap4());
                    if (cara.getGolds().get() + map4.getGolds() < 0) {
                        return false;
                    }
                    break;
                }
        }
        return true;
    }

    private void obtenirArmeArmureItem(Map map) throws NoWeaponFoundException, NoArmorFoundException, NoItemFoundException {
        if (map.getIdWeapon() != 0) {
            cara.getWeapons().add(Weapon.findWeaponById(map.getIdWeapon()));
        }
        if (map.getIdArmor() != 0) {
            cara.getArmors().add(Armor.findArmorById(map.getIdArmor()));
        }
        if (map.getIdItem() != 0) {
            Pair<Item,Integer> pair = new Pair<>(Item.findItemById(map.getIdItem()),map.getQuantiteItem());
            cara.getItems().add(pair);
            /*
             * TODO : Si l'objet existe déja faut juste augmenter sa quantité
             */
        }

    }

    /**
     * Interface du jeu
     *
     * @param map Map actuelle
     */
    private void gameInterface(Map map) {
        try {

            if (map.isCheckpoint()) {
                DAOCara daoCara = new DAOCara();
                daoCara.save(cara);
            }

            /*
             *  TODO : Les combats , une fonction qui renvoie vrai si tu gagne et faux si tu pers :)
             *  TODO : Si l'ennemi n'est pas léthal , mettre les HP à 1 , sinon rediriger vers l'écran en mode t'as perdu go au checkpoint
             *  TODO : Fait le dans le if connard au fait
             */
            //A faire qu'une fois
            if (!cara.getVisitedMap().contains(map)) {
                modifsStatsAndGolds(map);
                obtenirArmeArmureItem(map);
            }

            // Ajout du statut de visité à la map
            cara.getVisitedMap().add(map);
            cara.setCurrentMap(map);

            // Toutes les modifs à faire vis à vis de la map
            map.setText(editTextMap(map));

            DAOUser daoUser = new DAOUser();
            root.getChildren().clear();

            //Permet de choisir le fond d'écran qu'il faut

            int cpt = 0;

            /*
             * Vérification des items , spell et golds requis
             */
            boolean itemUnlocking = checkItem(map);
            boolean spellUnlocking = checkSpell(map);
            boolean enoughtGolds1 = checkIfEnoughtMoney(map, 1);
            boolean enoughtGolds2 = checkIfEnoughtMoney(map, 2);
            boolean enoughtGolds3 = checkIfEnoughtMoney(map, 3);
            boolean enoughtGolds4 = checkIfEnoughtMoney(map, 4);

            String filePath;

            if (map.getChoix1() != null) {
                ++cpt;
            }
            if (map.getChoix2() != null) {
                    ++cpt;
            }
            if (map.getChoix3() != null) {
                if (map.getIdRequiredItem() != 0) {
                    if (itemUnlocking) {
                        ++cpt;
                    }
                }
                else
                    ++cpt;
            }
            if (map.getChoix4() != null) {
                if (map.getIdRequiredSpell() != 0) {
                    if (spellUnlocking) {
                        ++cpt;
                    }
                }
                else
                    ++cpt;
            }

            switch (cpt) {
                case 1:
                    filePath = "1.png";
                    break;
                case 2:
                    if (spellUnlocking) {
                        filePath = "1-4A.png";
                    } else if (itemUnlocking) {
                        filePath = "1-3A.png";
                    } else {
                        filePath = "2.png";
                    }
                    break;
                case 3:
                    if (spellUnlocking && itemUnlocking) {
                        filePath = "1-3A-4A.png";
                    } else if (spellUnlocking) {
                        filePath = "1-2-4A.png";
                    } else if (itemUnlocking) {
                        filePath = "1-2-3A.png";
                    } else {
                        filePath = "3.png";
                    }
                    break;
                case 4:
                    if (spellUnlocking && itemUnlocking) {
                        filePath = "1-2-3A-4A.png";
                    } else if (spellUnlocking) {
                        filePath = "1-2-3-4A.png";
                    } else if (itemUnlocking) {
                        filePath = "1-2-3A-4.png";
                    } else {
                        filePath = "4.png";
                    }
                    break;
                default:
                    throw new MapErrorException();
            }



            // Initialisation du fond d'écran
            root.setBackground(new Background(new BackgroundImage(new Image(filePath, 800, 600, false, false),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

            // Gauche
            VBox left = buildLeft();

            // Haut
            VBox top = buildTop(map);

            // Centre
            VBox center = buildCenter(map);

            root.setCenter(center);
            root.setTop(top);
            root.setLeft(left);

            // Bottom
            VBox bottom = buildBottom(map, cpt, spellUnlocking, itemUnlocking, enoughtGolds1, enoughtGolds2, enoughtGolds3, enoughtGolds4);

            root.setBottom(bottom);

        } catch (NoConnectionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune connexion à internet");
            alert.setContentText("Merci de vous connecter à internet");
            alert.showAndWait();
            gameInterface(map);
        } catch (MapErrorException | NoMapFoundException  | NoWeaponFoundException | NoArmorFoundException | NoItemFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Le développeur est une tâche");
            alert.setContentText("J'ai mal initialisé la map, sorry :'(");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private VBox buildBottom(Map map, int cpt, boolean spellUnlocking, boolean itemUnlocking, boolean enoughtGolds1, boolean enoughtGolds2, boolean enoughtGolds3, boolean enoughtGolds4) {
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

        button1.getChildren().addAll(label1, rectangle1);


        if (map.getTestStat() == null) {
            initMapIfNoTest(map, cpt, button2, button3, button4, rectangle1, rectangle2, rectangle3, rectangle4, label2, label3, label4, spellUnlocking, itemUnlocking, enoughtGolds1, enoughtGolds2, enoughtGolds3, enoughtGolds4);
        } else {
            initMapWithTest(map, button2, button3, button4, rectangle1, rectangle2, rectangle3, rectangle4);
        }

        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("win")) {
            HBox ligneDuHaut = new HBox();
            ligneDuHaut.setPadding(new Insets(0, 0, 21, 0));
            ligneDuHaut.getChildren().addAll(button1, button2);
            HBox ligneDuBas = new HBox();
            button2.setPadding(new Insets(0, 0, 0, 96));
            button4.setPadding(new Insets(0, 0, 0, 96));
            ligneDuBas.setPadding(new Insets(0, 0, 24, 0));
            ligneDuBas.getChildren().addAll(button3, button4);
            VBox bottom = new VBox();
            bottom.setPadding(new Insets(0, 0, 0, 189));
            bottom.getChildren().addAll(ligneDuHaut, ligneDuBas);
            return bottom;
        } else {
            HBox ligneDuHaut = new HBox();
            ligneDuHaut.setPadding(new Insets(0, 0, 21, 0));
            ligneDuHaut.getChildren().addAll(button1, button2);
            HBox ligneDuBas = new HBox();
            button2.setPadding(new Insets(0, 0, 0, 96));
            button4.setPadding(new Insets(0, 0, 0, 96));
            ligneDuBas.setPadding(new Insets(0, 0, 13, 0));
            ligneDuBas.getChildren().addAll(button3, button4);
            VBox bottom = new VBox();
            bottom.setPadding(new Insets(0, 0, 0, 185));
            bottom.getChildren().addAll(ligneDuHaut, ligneDuBas);
            return bottom;
        }


    }

    private VBox buildCenter(Map map) {
        VBox center = new VBox();
        center.setAlignment(Pos.CENTER);
        Text text = new Text(map.getText());
        text.setFont(fontText);
        text.setLineSpacing(3);
        text.setWrappingWidth(500);
        center.getChildren().add(text);
        center.setPadding(new Insets(0, 0, 0, 20));
        return center;
    }

    private VBox buildTop(Map map) throws SQLException, NoConnectionException {
        VBox top = new VBox();
        Label topText = new Label(map.getName());
        topText.setFont(fontTitle);
        top.getChildren().add(topText);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20, 0, 0, width / 4.9));
        if (map.isCheckpoint()) {
            Label checkpoint = new Label("<Checkpoint>");
            checkpoint.setFont(fontSubText);
            top.getChildren().add(checkpoint);
        }

        if (map.isRest()) {
            DAOSpell daoSpell = new DAOSpell();
            daoSpell.resetSpells(cara);
            cara.setCURRHP(cara.getCURRHP().get() + HP_REPOS);
            if (cara.getCURRHP().get() > DAOCara.VALHPMAX) {
                cara.setCURRHP(DAOCara.VALHPMAX);
            }
            Label checkpoint = new Label("<Repos>");
            checkpoint.setFont(fontSubText);
            top.getChildren().add(checkpoint);
        }
        return top;
    }

    private VBox buildLeft() {
        VBox left = new VBox();
        left.setSpacing(2);
        left.setAlignment(Pos.CENTER_LEFT);
        left.setPadding(new Insets(2, 0, 0, 20));

        //Pseudo
        Label usernameText = new Label(cara.getName());

        // HP
        HBox hp = new HBox();
        ImageView hpImage = new ImageView(new Image("HP.png"));
        hpImage.setFitHeight(20);
        hpImage.setFitWidth(20);

        Label hpText = new Label();
        hpText.setFont(fontText);
        hpText.setPadding(new Insets(2, 0, 0, 5));
        hpText.textProperty().bind(cara.getCURRHP().asString());

        Label slash = new Label(" / ");

        Label hpMaxText = new Label();
        hpMaxText.setFont(fontText);
        hpMaxText.setPadding(new Insets(2, 0, 0, 0));
        hpMaxText.textProperty().bind(cara.getHP().asString());

        hp.setPadding(new Insets(0, 0, 10, 0));
        hp.getChildren().addAll(hpImage, hpText, slash, hpMaxText);

        // Force
        HBox force = new HBox();
        ImageView forceImage = new ImageView(new Image("FCE.png"));
        forceImage.setFitHeight(20);
        forceImage.setFitWidth(20);

        Label forceText = new Label();
        forceText.setFont(fontText);
        forceText.setPadding(new Insets(2, 0, 0, 5));
        forceText.textProperty().bind(cara.getFCE().asString());
        force.getChildren().addAll(forceImage, forceText);

        // Agilité
        HBox agilite = new HBox();
        ImageView agiliteImage = new ImageView(new Image("AGI.png"));
        agiliteImage.setFitHeight(20);
        agiliteImage.setFitWidth(20);

        Label agiliteText = new Label();
        agiliteText.setPadding(new Insets(2, 0, 0, 5));
        agiliteText.setFont(fontText);
        agiliteText.textProperty().bind(cara.getAGI().asString());
        agilite.getChildren().addAll(agiliteImage, agiliteText);

        // Intelligence
        HBox intel = new HBox();
        ImageView intelImage = new ImageView(new Image("INT.png"));
        intelImage.setFitHeight(20);
        intelImage.setFitWidth(20);

        Label intelText = new Label();
        intelText.setFont(fontText);
        intelText.setPadding(new Insets(2, 0, 0, 5));
        intelText.textProperty().bind(cara.getMAG().asString());
        intel.getChildren().addAll(intelImage, intelText);

        // Endurance
        HBox end = new HBox();
        ImageView endImage = new ImageView(new Image("END.png"));
        endImage.setFitHeight(20);
        endImage.setFitWidth(20);

        Label endText = new Label();
        endText.setPadding(new Insets(2, 0, 0, 5));
        endText.setFont(fontText);
        endText.textProperty().bind(cara.getEND().asString());
        end.getChildren().addAll(endImage, endText);

        // Charisme
        HBox chari = new HBox();
        ImageView chariImage = new ImageView(new Image("CHARI.png"));
        chariImage.setFitHeight(20);
        chariImage.setFitWidth(20);

        Label chariText = new Label();
        chariText.setFont(fontText);
        chariText.setPadding(new Insets(2, 0, 0, 5));
        chariText.textProperty().bind(cara.getCHARI().asString());
        chari.getChildren().addAll(chariImage, chariText);

        // Charisme
        HBox golds = new HBox();
        ImageView goldsImage = new ImageView(new Image("GOLDS.png"));
        goldsImage.setFitHeight(20);
        goldsImage.setFitWidth(20);

        Label goldsText = new Label();
        goldsText.setFont(fontText);
        goldsText.setPadding(new Insets(2, 0, 50, 5));
        goldsText.textProperty().bind(cara.getGolds().asString());
        golds.getChildren().addAll(goldsImage, goldsText);


        /*
        // Arme

        Label armeNom = new Label(cara.getWeapon().getName());
        armeNom.setPadding(new Insets(10, 0, 0, 0));

        // Armure

        Label armureNom = new Label(cara.getArmor().getName());
        armureNom.setPadding(new Insets(10, 0, 20, 0));

        */

        // Bouton Inventaire
        Button armurerie = new Button("Armurerie");
        armurerie.setOnAction(event -> interfaceArmurerie());

        // Bouton Sorts
        Button sorts = new Button("Sorts");
        sorts.setOnAction(event -> interfaceSorts());

        // Bouton Consommables
        Button inventaire = new Button("Inventaire");
        inventaire.setOnAction(event -> interfaceInventaire());


        left.getChildren().addAll(usernameText, hp, force, agilite, intel, end, chari, golds,/* armeNom, armureNom,*/ armurerie, inventaire, sorts);
        return left;
    }


    private void initMapWithTest(Map map, StackPane button2, StackPane button3, StackPane button4, Rectangle rectangle1, Rectangle rectangle2, Rectangle rectangle3, Rectangle rectangle4) {
        int random = (int) (Math.random() * 10);
        boolean sucess = false;

        switch (map.getTestStat()) {
            case "FCE":
                if (cara.getFCE().get() >= random) {
                    sucess = true;
                }
                break;
            case "AGI":
                if (cara.getAGI().get() >= random) {
                    sucess = true;
                }
                break;
            case "INT":
                if (cara.getMAG().get() >= random) {
                    sucess = true;
                }
                break;
            case "END":
                if (cara.getEND().get() >= random) {
                    sucess = true;
                }
                break;
            case "CHARI":
                if (cara.getCHARI().get() >= random) {
                    sucess = true;
                }
                break;
        }


        boolean finalSucess = sucess;
        rectangle1.setOnMouseClicked(event -> {
            try {
                if (finalSucess) {
                    gameInterface(Map.findMapById(map.getMap1()));
                } else {
                    gameInterface(Map.findMapById(map.getMap2()));
                }
            } catch (NoMapFoundException e) {
                e.printStackTrace();
            }
        });

        rectangle1.setOnMouseEntered(event -> rectangle1.setCursor(Cursor.HAND));

        Label label2 = new Label("");
        Label label3 = new Label("");
        Label label4 = new Label("");


        button2.getChildren().addAll(label2, rectangle2);
        button3.getChildren().addAll(label3, rectangle3);
        button4.getChildren().addAll(label4, rectangle4);
    }

    private void initMapIfNoTest(Map map, int cpt, StackPane button2, StackPane button3, StackPane button4, Rectangle rectangle1, Rectangle rectangle2, Rectangle rectangle3, Rectangle rectangle4, Label label2, Label label3, Label label4, boolean spellUnlocking, boolean itemUnlocking, boolean enoughtGolds1, boolean enoughtGolds2, boolean enoughtGolds3, boolean enoughtGolds4) {

        // Verification de l'argent
        if (enoughtGolds1) {
            rectangle1.setOnMouseClicked(event -> {
                try {
                    gameInterface(Map.findMapById(map.getMap1()));
                } catch (NoMapFoundException e) {
                    e.printStackTrace();
                }
            });

            rectangle1.setOnMouseEntered(event -> rectangle1.setCursor(Cursor.HAND));

        }

        if (cpt == 2) {
            if (itemUnlocking) {
                label3 = new Label(map.getChoix3());
                rectangle3.setOnMouseEntered(event -> rectangle3.setCursor(Cursor.HAND));
                rectangle3.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap3()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (spellUnlocking) {
                label4 = new Label(map.getChoix4());
                rectangle4.setOnMouseEntered(event -> rectangle4.setCursor(Cursor.HAND));
                rectangle4.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap4()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            } else  {
                label2 = new Label(map.getChoix2());
                buildLabel2(map, rectangle2, enoughtGolds2);
            }
        }
        else if (cpt == 3) {
            if (itemUnlocking && spellUnlocking) {
                label3 = new Label(map.getChoix3());
                rectangle3.setOnMouseEntered(event -> rectangle3.setCursor(Cursor.HAND));
                rectangle3.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap3()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
                label4 = new Label(map.getChoix4());
                rectangle4.setOnMouseEntered(event -> rectangle4.setCursor(Cursor.HAND));
                rectangle4.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap4()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            } else if (itemUnlocking) {
                label2 = new Label(map.getChoix2());
                label3 = new Label(map.getChoix3());
                rectangle3.setOnMouseEntered(event -> rectangle3.setCursor(Cursor.HAND));
                rectangle3.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap3()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            } else if (spellUnlocking) {
                label2 = new Label(map.getChoix2());
                label4 = new Label(map.getChoix4());
                rectangle4.setOnMouseEntered(event -> rectangle4.setCursor(Cursor.HAND));
                rectangle4.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap4()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                label2 = new Label(map.getChoix2());
                label3 = new Label(map.getChoix3());
                if (enoughtGolds2) {
                    rectangle2.setOnMouseEntered(event -> rectangle2.setCursor(Cursor.HAND));
                    rectangle2.setOnMouseClicked(event -> {
                        try {
                            gameInterface(Map.findMapById(map.getMap2()));
                        } catch (NoMapFoundException e) {
                            e.printStackTrace();
                        }
                    });

                    buildLabel3(map, rectangle3, enoughtGolds3);
                }
            }
        } else if (cpt == 4) {
            label2 = new Label(map.getChoix2());
            label3 = new Label(map.getChoix3());
            label4 = new Label(map.getChoix4());

            buildLabel2(map, rectangle2, enoughtGolds2);

            buildLabel3(map, rectangle3, enoughtGolds3);

            if (enoughtGolds4) {
                rectangle4.setOnMouseEntered(event -> rectangle4.setCursor(Cursor.HAND));
                rectangle4.setOnMouseClicked(event -> {
                    try {
                        gameInterface(Map.findMapById(map.getMap4()));
                    } catch (NoMapFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        }


        button2.getChildren().addAll(label2,rectangle2);
        button3.getChildren().addAll(label3,rectangle3);
        button4.getChildren().addAll(label4,rectangle4);
    }

    private void buildLabel3(Map map, Rectangle rectangle3, boolean enoughtGolds3) {
        if (enoughtGolds3) {
            rectangle3.setOnMouseEntered(event -> rectangle3.setCursor(Cursor.HAND));
            rectangle3.setOnMouseClicked(event -> {
                try {
                    gameInterface(Map.findMapById(map.getMap3()));
                } catch (NoMapFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void buildLabel2(Map map, Rectangle rectangle2, boolean enoughtGolds2) {
        if (enoughtGolds2) {
            rectangle2.setOnMouseEntered(event -> rectangle2.setCursor(Cursor.HAND));
            rectangle2.setOnMouseClicked(event -> {
                try {
                    gameInterface(Map.findMapById(map.getMap2()));
                } catch (NoMapFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void interfaceInventaire() {
        root.getChildren().clear();
    }

    private void interfaceSorts() {
        root.getChildren().clear();
        root.setBackground(new Background(new BackgroundImage(new Image("0.png", 800, 600, false, false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        HBox top = new HBox();
        Label labeltop = new Label("Sorts");
        labeltop.setFont(fontTitle);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(30,0,0,0));
        top.getChildren().add(labeltop);
        root.setTop(top);

        VBox center = new VBox();
        center.setPadding(new Insets(80,0,0,169));
        for (Spell spell : cara.getSpells()) {
            HBox spellBox = new HBox();
            Label label = new Label(spell.getName());
            label.setFont(fontSubTitle);
            label.setPadding(new Insets(20,0,0,0));
            Label labelSpellEffect = new Label(spell.getDescr());
            labelSpellEffect.setPadding(new Insets(5,0,0,0));
            labelSpellEffect.setFont(fontText);
            HBox javafxcapue = new HBox();
            javafxcapue.setPadding(new Insets(25,0,0,0));
            Button useSpell = new Button("Lancer");
            useSpell.setOnAction(event -> {
                /*
                 * TODO: Gérer les sorts
                 */
            });
            javafxcapue.getChildren().add(useSpell);
            VBox spellBoxFinal = new VBox();
            spellBoxFinal.setMinWidth(400);
            spellBoxFinal.getChildren().addAll(label,labelSpellEffect);
            spellBox.getChildren().addAll(spellBoxFinal,javafxcapue);
            center.getChildren().addAll(spellBox);
        }
        Button retour = new Button("Retour");
        HBox javafxcapue = new HBox();
        javafxcapue.getChildren().add(retour);
        javafxcapue.setPadding(new Insets(50,0,0,200));
        center.getChildren().add(javafxcapue);

        retour.setOnAction(event -> {
            gameInterface(cara.getCurrentMap());
        });
        root.setCenter(center);
        /*
         * TODO : Continuer à aligner les trucs
         */
    }

    private void interfaceArmurerie() {
        root.getChildren().clear();
    }
    
}