package com.lab;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class App extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    private Entity player,enemy;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Gonvival");
        settings.setVersion("beta");
    }

    @Override
    protected void initGame(){
        player = FXGL.entityBuilder()
                    .at(400,400)
                    .view(new Rectangle(25,25,Color.BLUE))
                    .buildAndAttach();
    }

    @Override
    protected void initInput(){
        FXGL.onKey(KeyCode.A, () -> {
            player.translateX(-2);
        });

        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(2);
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-2);
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(2);
        });
    }
}