package com.example.oop2project;

import javafx.animation.KeyValue;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Ghost {
    private int id, speed;
    private String color;
    private ImageView gifLeft, gifRight, ghostView;
    private Level level;
    private Timeline timeline;

    public Ghost(int id, int speed, String color, ImageView gifLeft, ImageView gifRight, Level level) {
        this.id = id;
        this.speed = 3;
        this.color = color;
        this.gifLeft = gifLeft;
        this.gifRight = gifRight;
        this.level = level;
        this.setGhostView(gifRight);
        ghostView.setFitHeight(40);
        ghostView.setFitWidth(40);
        ghostView.setUserData("Ghost");
        this.timeline = createTimeline();
    }

    public void setGhostView(ImageView ghostView) {
        this.ghostView = ghostView;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public String getColor() {
        return color;
    }

    public ImageView getGifRight() {
        return gifRight;
    }

    public ImageView getGifLeft() {
        return gifLeft;
    }

    public ImageView getGhostView() {
        return ghostView;
    }

    public Level getLevel() {
        return level;
    }


    private Timeline createTimeline() {
        Timeline gifTimeline = new Timeline();
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(speed), e -> ghostView.setImage(gifRight.getImage()));
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(speed * 2), e -> ghostView.setImage(gifLeft.getImage()));

        gifTimeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        gifTimeline.setCycleCount(Timeline.INDEFINITE);

        return gifTimeline;
    }

    public void startAnimation() {
        timeline.play();
    }

    public void stopAnimation() {
        timeline.stop();
    }

    public void moveSmoothly(int startX, int endX) {
        // Create a separate timeline for movement
        Timeline movementTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(ghostView.xProperty(), startX)),
                new KeyFrame(Duration.seconds(speed), new KeyValue(ghostView.xProperty(), endX))
        );

        // Set the timeline to auto-reverse and loop indefinitely
        movementTimeline.setAutoReverse(true);
        movementTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the movement timeline
        movementTimeline.play();
    }



//    private boolean isTransPath(int X, int Y, int speed, boolean onX) {
//        int step = (speed >= 0) ? 1 : -1;
//
//        if (onX) {
//            for (int i = 0; i != speed; i += step) {
//                if (!level.isTransparentArea(X + i, Y, (int) ghostView.getFitHeight())) return false;
//            }
//        } else {
//            for (int i = 0; i != speed; i += step) {
//                if (!level.isTransparentArea(X, Y + i, (int) ghostView.getFitHeight())) return false;
//            }
//        }
//        return true;
//    }
//
//    public void moveRandomly() {
//        AtomicInteger lastDirection = new AtomicInteger(-1);
//
//        this.timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
//            double newX = ghostView.getX();
//            double newY = ghostView.getY();
//
//            switch (direction.get()) {
//                case 0: // Up
//                    if (isTransPath((int) newX, (int) newY - speed, -speed, false)) {
//                        newY -= speed;
//                    } else {
//                        direction.set(getNewDirection(lastDirection.get()));
//                    }
//                    break;
//                case 1: // Down
//                    if (isTransPath((int) newX, (int) newY + speed, speed, false)) {
//                        newY += speed;
//                    } else {
//                        direction.set(getNewDirection(lastDirection.get()));
//                    }
//                    break;
//                case 2: // Left
//                    if (isTransPath((int) newX - speed, (int) newY, -speed, true)) {
//                        newX -= speed;
//                        ghostView.setImage(gifLeft.getImage());
//                    } else {
//                        direction.set(getNewDirection(lastDirection.get()));
//                    }
//                    break;
//                case 3: // Right
//                    if (isTransPath((int) newX + speed, (int) newY, speed, true)) {
//                        newX += speed;
//                        ghostView.setImage(gifRight.getImage());
//                    } else {
//                        direction.set(getNewDirection(lastDirection.get()));
//                    }
//                    break;
//            }
//
//            ghostView.setX(newX);
//            ghostView.setY(newY);
//            lastDirection.set(direction.get());
//        }));
//
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
//    }
//
//    private int getNewDirection(int lastDirection) {
//        int newDirection;
//        do {
//            newDirection = random.nextInt(4);
//        } while (newDirection == lastDirection || newDirection == (lastDirection + 2) % 4);
//        return newDirection;
//    }








}



