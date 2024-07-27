package com.example.oop2project;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.stage.Modality;

import styles.ButtonAnimation;
import styles.ButtonStyle;

import java.io.File;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Application extends javafx.application.Application {
    DB db = new DB();
    Connection connection = db.connect();
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Pac-Man");

        Media sound = new Media(new File("assets/Audio/Pacman-song.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setAlignment(Pos.CENTER);

        Pane pane = new Pane();
        pane.getChildren().add(vbox);
        Scene scene = new Scene(pane, 1000, 600);

        // Button for creating an account
        Button createAccountButton = new Button("Create Account");
        ButtonStyle.styleButton(createAccountButton);
        ButtonAnimation.animateButton(createAccountButton);
        createAccountButton.setOnAction(e -> createAccountAction(primaryStage));

        // Button for logging in
        Button loginButton = new Button("Login");
        ButtonStyle.styleButton(loginButton);
        ButtonAnimation.animateButton(loginButton);
        loginButton.setOnAction(e -> loginAction(primaryStage, vbox, scene, pane));

        vbox.getChildren().addAll(createAccountButton, loginButton);

        Image image = new Image("File:./assets/intro.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1000, 600, false, false, false, true));
        pane.setBackground(new Background(backgroundImage));

        // Set the media to replay when it ends
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createAccountAction(Stage primaryStage) {
        Stage accountCreationStage = new Stage();
        accountCreationStage.setTitle("Create Account");

        VBox accountCreationVbox = new VBox();
        accountCreationVbox.setPadding(new Insets(10));
        accountCreationVbox.setSpacing(8);
        accountCreationVbox.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Enter your email:");
        emailLabel.getStyleClass().addAll("text-primary");

        TextField emailField = new TextField();
        emailField.setPromptText("Your email");
        emailField.setStyle("-fx-background-color: transparent; -fx-border-color: #3b8bff; -fx-text-fill: #ffffff; -fx-border-radius: 10;");

        Label usernameLabel = new Label("Enter your username and password:");
        usernameLabel.getStyleClass().addAll("text-primary");


        TextField usernameField = new TextField();
        usernameField.setPromptText("Your username");
        usernameField.setStyle("-fx-background-color: transparent; -fx-border-color: #3b8bff; -fx-text-fill: #ffffff; -fx-border-radius: 10;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your password");
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: #3b8bff; -fx-text-fill: #ffffff; -fx-border-radius: 10;");

        Label confirmationLabel = new Label();

        Button submit = new Button("Submit");
        ButtonStyle.styleButtonGreen(submit);
        ButtonAnimation.animateButton(submit);
        submit.setOnAction(event -> {
            if (!db.createPlayer(connection, usernameField.getText(), emailField.getText(), passwordField.getText())) {
                confirmationLabel.getStyleClass().addAll("alert", "alert-danger");
                confirmationLabel.setText("Error, username or email already exists!");
            } else {
                confirmationLabel.getStyleClass().clear();
                confirmationLabel.getStyleClass().addAll("alert", "alert-success");
                confirmationLabel.setText("Account created! Welcome!");
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> accountCreationStage.close());
                delay.play();
            }
        });

        // Load GIFs
        Image orange = new Image("File:./assets/Ghost/orange-right.gif");
        ImageView ghost1 = new ImageView(orange);
        ghost1.setFitWidth(50);
        ghost1.setPreserveRatio(true);
        Image red = new Image("File:./assets/Ghost/red-left.gif");
        ImageView ghost2 = new ImageView(red);
        ghost2.setFitWidth(50);
        ghost2.setPreserveRatio(true);

        HBox buttonBox = new HBox(ghost1, submit, ghost2);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        accountCreationVbox.getChildren().addAll(emailLabel, emailField, usernameLabel, usernameField, passwordField, confirmationLabel, buttonBox);

        Image image = new Image("File:./assets/wallpaper.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        accountCreationVbox.setBackground(background);

        Scene accountCreationScene = new Scene(accountCreationVbox, 340, 300);
        accountCreationScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        accountCreationStage.setScene(accountCreationScene);

        accountCreationStage.initOwner(primaryStage);
        accountCreationStage.initModality(Modality.WINDOW_MODAL);
        accountCreationStage.show();
    }

    private void loginAction(Stage primaryStage, VBox vBox, Scene scene, Pane pane) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        VBox loginVBox = new VBox();
        loginVBox.setPadding(new Insets(10));
        loginVBox.setSpacing(8);
        loginVBox.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Enter your username:");
        usernameLabel.getStyleClass().addAll("text-primary");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Your username");
        usernameField.setStyle("-fx-background-color: transparent; -fx-border-color: #3b8bff; -fx-text-fill: #ffffff; -fx-border-radius: 10;");

        Label passwordLabel = new Label("Enter your password:");
        passwordLabel.getStyleClass().addAll("text-primary");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your password");
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: #3b8bff; -fx-text-fill: #ffffff; -fx-border-radius: 10;");

        Label confirmationLabel = new Label();

        Button loginButton = new Button("Login");
        ButtonStyle.styleButtonGreen(loginButton);
        ButtonAnimation.animateButton(loginButton);

        loginButton.setOnAction(event -> {
            Player player = db.authenticatePlayer(connection, usernameField.getText(), passwordField.getText());
            if (player != null) {
                confirmationLabel.getStyleClass().clear();
                confirmationLabel.getStyleClass().addAll("alert", "alert-success");
                confirmationLabel.setText("Welcome back!");
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> loginStage.close());
                delay.play();
                if(Objects.equals(player.getRole(), "Admin")){
                    adminAction(primaryStage, vBox, scene, pane);
                }
                else startGame(primaryStage, player, vBox, scene, pane);
            } else {
                confirmationLabel.getStyleClass().addAll("alert", "alert-danger");
                confirmationLabel.setText("Error, username or password incorrect!");
            }
        });

        Image cyan = new Image("File:./assets/Ghost/cyan-right.gif");
        ImageView ghost1 = new ImageView(cyan);
        ghost1.setFitWidth(50);
        ghost1.setPreserveRatio(true);
        Image pink = new Image("File:./assets/Ghost/pink-left.gif");
        ImageView ghost2 = new ImageView(pink);
        ghost2.setFitWidth(50);
        ghost2.setPreserveRatio(true);

        HBox loginButtonBox = new HBox(ghost1, loginButton, ghost2);
        loginButtonBox.setAlignment(Pos.CENTER);
        loginButtonBox.setSpacing(10);

        loginVBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, confirmationLabel, loginButtonBox);

        Image image = new Image("file:./assets/wallpaper.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        loginVBox.setBackground(background);

        Scene loginScene = new Scene(loginVBox, 340, 300);
        loginScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        loginStage.setScene(loginScene);

        loginStage.initOwner(primaryStage);
        loginStage.initModality(Modality.WINDOW_MODAL);
        loginStage.show();
    }
    private void adminAction(Stage primaryStage, VBox adminVBox, Scene scene, Pane pane) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Admin");
        adminVBox.getChildren().clear();
        adminVBox.setPadding(new Insets(10));
        adminVBox.setSpacing(8);
        adminVBox.setAlignment(Pos.CENTER);

        Button addGhost = createButton("Add Ghost", e -> addGhost(primaryStage));
        ButtonStyle.styleButton(addGhost);
        ButtonAnimation.animateButton(addGhost);

        Button deleteGhost = createButton("Delete Ghost", e -> deleteGhost(primaryStage));
        ButtonStyle.styleButton(deleteGhost);
        ButtonAnimation.animateButton(deleteGhost);

        adminVBox.getChildren().addAll(addGhost, deleteGhost);
    }

    private Button createButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        ButtonStyle.styleButton(button);
        ButtonAnimation.animateButton(button);
        button.setOnAction(handler);
        return button;
    }
    private void addOrDeleteGhost(Stage primaryStage, String title, boolean isAdd) {
        Stage stage = new Stage();
        stage.setTitle(title);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(12);
        vBox.setAlignment(Pos.CENTER);

        AtomicReference<TableView<ObservableList<String>>> tableViewRef = new AtomicReference<>(db.createTableView(connection));
        vBox.getChildren().add(tableViewRef.get());

        Label levelIdLabel = new Label("Choose the level:");
        levelIdLabel.getStyleClass().addAll("text-primary");
        ComboBox<Integer> levelIdComboBox = new ComboBox<>();
        levelIdComboBox.getItems().addAll(1, 2, 3);
        levelIdComboBox.setStyle(
                "-fx-background-color: #87e6ff; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-color: #2888ff; " +
                        "-fx-border-radius: 25;"
        );

        Label colorLabel = new Label("Select the ghost color:");
        colorLabel.getStyleClass().addAll("text-primary");
        ComboBox<String> colorComboBox = new ComboBox<>();
        colorComboBox.getItems().addAll("orange", "red", "blue", "cyan", "pink", "purple");
        colorComboBox.setStyle(
                "-fx-background-color: #87e6ff; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-color: #2888ff; " +
                        "-fx-border-radius: 25;"
        );

        // Map colors to IDs
        HashMap<String, Integer> colorToIdMap = new HashMap<>();
        colorToIdMap.put("orange", 1);
        colorToIdMap.put("red", 2);
        colorToIdMap.put("blue", 3);
        colorToIdMap.put("cyan", 4);
        colorToIdMap.put("pink", 5);
        colorToIdMap.put("purple", 6);

        ImageView ghostView = new ImageView();
        ghostView.setFitHeight(50);
        ghostView.setFitWidth(50);
        addAnimation(ghostView, colorComboBox);

        Label confirmationLabel = new Label();

        Button actionButton = new Button(isAdd ? "Add Ghost" : "Delete Ghost");
        if (isAdd) {
            ButtonStyle.styleButtonGreen(actionButton);
        } else {
            ButtonStyle.styleButtonRed(actionButton);
        }
        ButtonAnimation.animateButton(actionButton);
        actionButton.setOnAction(e -> {
            if (levelIdComboBox.getValue() == null || colorComboBox.getValue() == null) {
                confirmationLabel.getStyleClass().clear();
                confirmationLabel.getStyleClass().addAll("alert", "alert-danger");
                confirmationLabel.setText("Please select both a level ID and a ghost color.");
            } else {
                int colorId = colorToIdMap.get(colorComboBox.getValue());
                boolean resultMessage = isAdd ? db.addGhost(connection, levelIdComboBox.getValue(), colorId)
                        : db.deleteGhost(connection, levelIdComboBox.getValue(), colorId);

                setConfirmationLabel(confirmationLabel, resultMessage);
            }
            // Update the TableView
            vBox.getChildren().remove(tableViewRef.get());
            tableViewRef.set(db.createTableView(connection));
            vBox.getChildren().add(tableViewRef.get());
        });

        vBox.getChildren().addAll(levelIdLabel, levelIdComboBox, colorLabel, colorComboBox, ghostView, actionButton, confirmationLabel);

        Scene scene = new Scene(vBox, 600, 400);
        Image image = new Image("file:./assets/wallpaper.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        vBox.setBackground(background);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);

        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }


    private void setConfirmationLabel(Label confirmationLabel, boolean isSuccess) {
        confirmationLabel.getStyleClass().clear();
        confirmationLabel.getStyleClass().addAll("alert", isSuccess ? "alert-success" : "alert-danger");
        confirmationLabel.setText(isSuccess ? "Operation successful!" : "Operation failed!");
    }

    private void addGhost(Stage primaryStage) {
        addOrDeleteGhost(primaryStage, "Add Ghost", true);
    }
    private void deleteGhost(Stage primaryStage) {
        addOrDeleteGhost(primaryStage, "Delete Ghost", false);
    }
    private void addAnimation(ImageView ghostView, ComboBox<String> colorComboBox) {
        // Create a Timeline to continuously move the ghost to the right
        KeyValue keyValue = new KeyValue(ghostView.translateXProperty(), 250);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(4), keyValue);
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        colorComboBox.valueProperty().addListener((obs, oldColor, newColor) -> {
            Image ghostImage = new Image("file:./assets/Ghost/" + newColor + "-right.gif");
            ghostView.setImage(ghostImage);
            ghostView.setTranslateX(0);
            timeline.stop();
            timeline.playFromStart();
        });
    }
    private void startGame(Stage primaryStage, Player player, VBox gameVBox, Scene scene, Pane pane) {
        gameVBox.getChildren().clear();
        primaryStage.setTitle("Pac-Man Game");

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit Game");
        ButtonStyle.styleButtonGreen(startButton);
        ButtonAnimation.animateButton(startButton);
        ButtonStyle.styleButtonRed(exitButton);
        ButtonAnimation.animateButton(exitButton);

        gameVBox.getChildren().addAll(startButton, exitButton);
        gameVBox.setPadding(new Insets(300, 0, 0, 450));

        startButton.setOnAction(event -> {
            Game game = new Game(player.getId(), 0, 1, LocalDateTime.now());
            Level level = db.loadLevel(connection, game.getLevelReached());

            runGame(primaryStage, game, level);

        });

        exitButton.setOnAction(event -> primaryStage.close());

    }

    private void runGame(Stage stage, Game game, Level level) {
        Pane gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");

        ImageView mapView = level.getMapLayout();
        gamePane.getChildren().add(mapView);

        Button saveExitButton = new Button("Save and Exit");
        ButtonStyle.styleButtonGreen(saveExitButton);
        ButtonAnimation.animateButton(saveExitButton);
        saveExitButton.setLayoutX(50);
        saveExitButton.setLayoutY(10);
        saveExitButton.setOnAction(e -> {
            game.setLevelReached(level.getId());
            db.saveGame(connection, game);
            stage.close();
        });
        gamePane.getChildren().add(saveExitButton);
        saveExitButton.setFocusTraversable(false);

        level.displayGhosts(gamePane);

        Pacman pacman = new Pacman(level, gamePane);
        pacman.setGame(game);

        int mapWidth = 800;
        int mapHeight = 950;
        int pelletSize = 32;
        int mapX = (int) mapView.getLayoutX();
        int mapY = (int) mapView.getLayoutY();
        int pelletsPut = 27;
        // Populate pellets and power pellets
        for (int x = mapX; x < mapX + mapWidth; x += pelletSize) {
            for (int y = mapY; y < mapY + mapHeight; y += pelletSize) {
                if (level.isTransparentArea(x - mapX, y - mapY, pelletSize)) {
                    ImageView pelletView = new ImageView(new Image("File:./assets/Map/pellet.png"));
                    pelletView.setFitWidth(pelletSize);
                    pelletView.setFitHeight(pelletSize);
                    pelletView.setLayoutX(x);
                    pelletView.setLayoutY(y);
                    pelletView.setUserData("Pellet");
                    pelletsPut++;
                    if (pelletsPut == 60) {
                        ImageView powerPelletView = new ImageView(new Image("File:./assets/Map/powerPellet.png"));
                        powerPelletView.setFitWidth(20);
                        powerPelletView.setFitHeight(20);
                        powerPelletView.setLayoutX(x + 7);
                        powerPelletView.setLayoutY(y);
                        powerPelletView.setUserData("PowerPellet");
                        gamePane.getChildren().add(powerPelletView);
                        pelletsPut = 0;

                    } else gamePane.getChildren().add(pelletView);
                }
            }
        }

        captureMouseClicks(gamePane);
        Scene scene = new Scene(gamePane, mapWidth, mapHeight);
        stage.setScene(scene);
        scene.focusOwnerProperty();
        scene.setOnKeyPressed(pacman::handleKey);
        stage.setFullScreen(true);
        stage.show();
    }


    private void captureMouseClicks(Pane gamePane) {
        gamePane.setOnMouseClicked(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            System.out.println("Clicked at X: " + mouseX + ", Y: " + mouseY);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
