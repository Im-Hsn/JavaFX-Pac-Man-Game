package styles;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

public class ButtonAnimation {
    public static void animateButton(Button button) {
        // Add a glowing effect to the button
        Glow glow = new Glow();
        glow.setLevel(0.0);
        button.setEffect(glow);

        // Create a timeline to animate the glow effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(glow.levelProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.7)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glow.levelProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
