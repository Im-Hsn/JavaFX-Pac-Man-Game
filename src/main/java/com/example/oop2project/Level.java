package com.example.oop2project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    private int id;
    private ImageView mapLayout;
    private ArrayList<Ghost> ghosts = new ArrayList<>();

    public Level(int id, ImageView mapLayout, ArrayList<Ghost> ghosts) {
        this.id = id;
        this.mapLayout = mapLayout;
        this.ghosts = ghosts;
    }

    public void setMapLayout(ImageView mapLayout) {
        this.mapLayout = mapLayout;
    }

    public void setGhosts(ArrayList<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public Ghost getGhost(int i) {
        return ghosts.get(i);
    }

    public int getId() {
        return id;
    }

    public ImageView getMapLayout() {
        return mapLayout;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public void displayGhosts(Pane gamePane) {
        int[] coordinates = {
                625, 30,
                1015, 30,
                830, 160,
                840, 340,
                830, 705,
                1160, 890
        };

        int[][] movingCoordinates = {
                {495, 800},    // Ghost 1 start and end X coordinates
                {885, 1200},   // Ghost 2 start and end X coordinates
                {730, 980},    // Ghost 3 start and end X coordinates (approximated)
                {730, 950},    // Ghost 4 start and end X coordinates (approximated)
                {645, 1065},   // Ghost 5 start and end X coordinates (approximated)
                {1200, 495},   // Ghost 6 start and end X coordinates (approximated)
        };

        int index = 0;
        int movingIndex = 0;

        for (Ghost ghost : ghosts) {
            // Set the position of the ghost
            ghost.getGhostView().setX(coordinates[index]);
            ghost.getGhostView().setY(coordinates[index + 1]);

            // Add the ghost to the game pane
            gamePane.getChildren().add(ghost.getGhostView());

            // Set up gif change timeline for the ghost
            ghost.startAnimation();

            // Move the ghost left and right within the specified range
            int startX = movingCoordinates[movingIndex][0];
            int endX = movingCoordinates[movingIndex][1];
            ghost.moveSmoothly(startX, endX);

            // Increment the index for the next ghost coordinates
            index += 2;
            movingIndex++;
        }
    }

    public boolean isTransparentArea(int x, int y, int size) {
        Image map = mapLayout.getImage();
        PixelReader reader = map.getPixelReader();

        double scaleX = map.getWidth() / mapLayout.getFitWidth();
        double scaleY = map.getHeight() / mapLayout.getFitHeight();

        int scaledX = (int) (x * scaleX);
        int scaledY = (int) (y * scaleY);
        int scaledSize = (int) (size * Math.max(scaleX, scaleY));

        for (int i = scaledX; i < Math.min(scaledX + scaledSize, map.getWidth()); i++) {
            for (int j = scaledY; j < Math.min(scaledY + scaledSize, map.getHeight()); j++) {
                if (i >= 0 && j >= 0 && i < map.getWidth() && j < map.getHeight()) {
                    Color color = reader.getColor(i, j);
                    if (color.getOpacity() != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean isMostlyTransparentArea(int x, int y, int size) {
        Image map = mapLayout.getImage();
        PixelReader reader = map.getPixelReader();

        double scaleX = map.getWidth() / mapLayout.getFitWidth();
        double scaleY = map.getHeight() / mapLayout.getFitHeight();

        int scaledX = (int) (x * scaleX);
        int scaledY = (int) (y * scaleY);
        int scaledSize = (int) (size * Math.max(scaleX, scaleY));

        int totalPixels = 0;
        int transparentPixels = 0;

        for (int i = scaledX; i < Math.min(scaledX + scaledSize, map.getWidth()); i++) {
            for (int j = scaledY; j < Math.min(scaledY + scaledSize, map.getHeight()); j++) {
                if (i >= 0 && j >= 0 && i < map.getWidth() && j < map.getHeight()) {
                    totalPixels++;
                    Color color = reader.getColor(i, j);
                    if (color.getOpacity() == 0) {
                        transparentPixels++;
                    }
                }
            }
        }

        double transparencyRatio = (double) transparentPixels / totalPixels;

        return transparencyRatio <= 0.99;
    }



}
