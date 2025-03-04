package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CountdownTimer {
    private IntegerProperty timeLeft = new SimpleIntegerProperty(360);
    private final Text timerText = new Text();

    public CountdownTimer() {
        timerText.setStyle("-fx-font-size: 36px; -fx-fill: white;");
        timerText.textProperty().bind(timeLeft.asString());
        timerText.setTranslateX(50);
        timerText.setTranslateY(50);

        FXGL.getGameScene().addUINode(timerText);

        FXGL.getGameTimer().runAtInterval(() -> {
            if (timeLeft.get() > 0) {
                timeLeft.set(timeLeft.get() - 1);
            } else {
                FXGL.showMessage("Time's up!", () -> FXGL.getGameController().startNewGame());
            }
        }, Duration.seconds(1));
    }
}
