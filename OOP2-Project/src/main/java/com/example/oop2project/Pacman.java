package com.example.oop2project;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class Pacman {
    private ImageView pacmanView;
    private double speed = 3.0;
    private double dx = 0, dy = 0;
    private Level level;
    private Game game;
    private AnimationTimer timer;
    private int lives = 3;
    private HBox livesbox = new HBox(10);
    private Label scoreLabel = new Label("Score: ");

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    private Image upImage = new Image("File:./assets/Pacman/up.gif"), downImage = new Image("File:./assets/Pacman/down.gif"),
            leftImage = new Image("File:./assets/Pacman/left.gif"), rightImage = new Image("File:./assets/Pacman/right.gif");

    public Pacman(Level level, Pane pane) {
        this.pacmanView = new ImageView(leftImage);
        pacmanView.setFitHeight(30);
        pacmanView.setFitWidth(30);
        pacmanView.setLayoutX(812);
        pacmanView.setLayoutY(896);
        this.level = level;

        scoreLabel.setLayoutX(50);
        scoreLabel.setLayoutY(900);
        scoreLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: yellow;");

        ImageView pacmanView1 = new ImageView(leftImage);
        pacmanView1.setFitWidth(32);
        pacmanView1.setFitHeight(32);
        ImageView pacmanView2 = new ImageView(leftImage);
        pacmanView2.setFitWidth(32);
        pacmanView2.setFitHeight(32);
        ImageView pacmanView3 = new ImageView(leftImage);
        pacmanView3.setFitWidth(32);
        pacmanView3.setFitHeight(32);
        livesbox.getChildren().addAll(pacmanView1, pacmanView2, pacmanView3);
        livesbox.setLayoutX(50);
        livesbox.setLayoutY(80);
        pane.getChildren().addAll(livesbox, pacmanView, scoreLabel);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                pacmanView.setX(pacmanView.getX() + dx * speed);
                pacmanView.setY(pacmanView.getY() + dy * speed);
                checkCollisionWithPellets(pane);
            }
        };
        timer.start();
    }

    public void handleKey(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.UP) {
            dx = 0;
            dy = -1;
            pacmanView.setImage(upImage);
        } else if (code == KeyCode.DOWN) {
            dx = 0;
            dy = 1;
            pacmanView.setImage(downImage);
        } else if (code == KeyCode.LEFT) {
            dx = -1;
            dy = 0;
            pacmanView.setImage(leftImage);
        } else if (code == KeyCode.RIGHT) {
            dx = 1;
            dy = 0;
            pacmanView.setImage(rightImage);
        }
    }

    public void checkCollisionWithPellets(Pane gamePane) {
//        boolean hitNonTransparentArea = !level.isMostlyTransparentArea((int) pacmanView.getX(), (int) pacmanView.getY(), (int) pacmanView.getBoundsInParent().getWidth());
//        if (hitNonTransparentArea) {
//            game.setScore(game.getScore() - 10);
//            scoreLabel.setText("Score: " + Integer.toString(game.getScore()));
//        }

        for (Node node : gamePane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                Object objectType = imageView.getUserData();

                // Check for collision with pellets or power pellets
                if (objectType != null && (objectType.equals("PowerPellet") || objectType.equals("Pellet")) &&
                        imageView.getBoundsInParent().intersects(pacmanView.getBoundsInParent())) {
                    Media sound = new Media(new File("assets/Audio/credit.wav").toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                    gamePane.getChildren().remove(imageView);
                    if (objectType.equals("Pellet")) {
                        game.setScore(game.getScore() + 1);
                        scoreLabel.setText("Score: " + Integer.toString(game.getScore()));
                    } else if (objectType.equals("PowerPellet")) {
                        game.setScore(game.getScore() + 10);
                        scoreLabel.setText("Score: " + Integer.toString(game.getScore()));
                        pauseGhostsAndPacman(1);
                    }
                    break;
                }

                // Check for collision with ghosts
                if (objectType != null && objectType.equals("Ghost") &&
                        imageView.getBoundsInParent().intersects(pacmanView.getBoundsInParent())) {
                    lives--;

                    timer.stop();

                    if (lives == 0) {
                        if (!livesbox.getChildren().isEmpty() && livesbox.getChildren().get(0) instanceof ImageView) livesbox.getChildren().remove(0);
                        pauseGhostsAndPacman(3);
                        Media sound = new Media(new File("assets/Audio/game-over.mp3").toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(event -> {
                            Stage stage = (Stage) gamePane.getScene().getWindow();
                            stage.close();
                        });
                        pause.play();

                    } else {
                        if (!livesbox.getChildren().isEmpty() && livesbox.getChildren().get(0) instanceof ImageView) livesbox.getChildren().remove(0);
                        pauseGhostsAndPacman(3);
                        Media sound = new Media(new File("assets/Audio/pacman_death.wav").toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    }
                    break;
                }
            }
        }
    }

    private void pauseGhostsAndPacman(int seconds) {
        for (Ghost ghost : level.getGhosts()) {
            ghost.stopAnimation();
        }
        timer.stop();
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(event -> {
            for (Ghost ghost : level.getGhosts()) {
                ghost.startAnimation();
            }
            timer.start();
        });
        pause.play();
    }

    //score ..


    //    private boolean canMove(double x, double y) {
//        double currentX = pacmanView.getX();
//        double currentY = pacmanView.getY();
//
//        double stepX = (x - currentX) / Math.abs(x - currentX);
//        double stepY = (y - currentY) / Math.abs(y - currentY);
//
//        while ((stepX > 0 && currentX < x) || (stepX < 0 && currentX > x) ||
//                (stepY > 0 && currentY < y) || (stepY < 0 && currentY > y)) {
//            if (!level.isTransparentArea((int) currentX, (int) currentY, (int) pacmanView.getFitWidth())) {
//                return false;
//            }
//            currentX += stepX;
//            currentY += stepY;
//        }
//
//        return true;
//    }
    private boolean canMove(double newX, double newY) {
        int size = (int) pacmanView.getFitHeight();
        int x = (int) newX;
        int y = (int) newY;
        return level.isMostlyTransparentArea(x, y, size);
    }

}