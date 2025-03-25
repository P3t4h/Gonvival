package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CountdownTimer {
    private IntegerProperty timeLeft = new SimpleIntegerProperty(1);
    private Text timerText = new Text();
    private boolean isTimeUp = false;

    public CountdownTimer() {
        timerText.setTranslateX(FXGL.getAppWidth() / 2.0 - 75);
        timerText.setTranslateY(50);
        timerText.setStyle("-fx-font-size: 36px; -fx-fill: YELLOW;");
        timerText.textProperty().bind(timeLeft.asString("Time Left: %d"));

        FXGL.getGameScene().addUINode(timerText);

        FXGL.getGameTimer().runAtInterval(() -> {
            if (timeLeft.get() > 0) {
                timeLeft.set(timeLeft.get() - 1);
            } else if (!isTimeUp) {
                isTimeUp = true;
                FXGL.getGameWorld().spawn("Boss",100,400);
                FXGL.showMessage("Boss Time", () -> FXGL.getGameController());
            }
        }, Duration.seconds(1));
    }
}