package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CountdownTimer {
    private IntegerProperty timeLeft = new SimpleIntegerProperty(60);
    private Text timerText = new Text();

    public CountdownTimer() {
        timerText.setStyle("-fx-font-size: 36px; -fx-fill: red;");
        timerText.textProperty().bind(timeLeft.asString("Time Left: %d"));
        timerText.setTranslateX(350);
        timerText.setTranslateY(50);

        FXGL.getGameScene().addUINode(timerText);

        FXGL.getGameTimer().runAtInterval(() -> {
            if (timeLeft.get() > 0) {
                timeLeft.set(timeLeft.get() - 1);
            } else {
                FXGL.showMessage("YOU WIN!", () -> FXGL.getGameController().gotoMainMenu());
            }
        }, Duration.seconds(1));
    }
}