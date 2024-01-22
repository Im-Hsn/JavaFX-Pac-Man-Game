package styles;
import javafx.scene.control.Button;

public class ButtonStyle {
    public static void styleButton(Button button) {
        button.getStyleClass().setAll("btn", "btn-primary");
        button.setStyle("-fx-border-radius: 25px; -fx-background-radius: 25px; -fx-padding: 10px 20px;");
    }
    public static void styleButtonGreen(Button button) {
        button.getStyleClass().setAll("btn");
        button.setStyle("-fx-background-color: #2e802e; -fx-text-fill: white; -fx-border-radius: 25px; -fx-background-radius: 25px; -fx-padding: 10px 20px;");
    }
    public static void styleButtonRed(Button button) {
        button.getStyleClass().setAll("btn", "btn-danger");
        button.setStyle("-fx-border-radius: 25px; -fx-background-radius: 25px; -fx-padding: 10px 20px;");
    }
}
