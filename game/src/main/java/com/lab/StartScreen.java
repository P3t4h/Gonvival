package com.lab;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

public class StartScreen extends FXGLMenu {
    public StartScreen() {
        super(MenuType.MAIN_MENU);
        
        ImageView background = new ImageView(new Image("assets/textures/background.png"));
        background.setFitWidth(FXGL.getAppWidth());
        background.setFitHeight(FXGL.getAppHeight());
        background.setPreserveRatio(false);

        Text title = new Text("Gonvival!");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Verdana", 100));
        title.setTranslateX(100);
        title.setTranslateY(150);

        Button startButton = new Button("Start Game");
        startButton.setPrefSize(200, 70);
        startButton.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        startButton.setOnAction(e -> {
            GuideScreen guideScreen = new GuideScreen();
            FXGL.getGameController().pauseEngine();
            FXGL.getGameScene().getContentRoot().getChildren().add(guideScreen);
            fireNewGame();
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            FXGL.getGameController().exit();
        });
        exitButton.setPrefSize(200, 70);
        exitButton.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        VBox menuBox = new VBox(15, startButton, exitButton);
        menuBox.setTranslateX(150);
        menuBox.setTranslateY(300);

        AnchorPane layout = new AnchorPane();
        layout.getChildren().addAll(title, menuBox);
        getContentRoot().getChildren().add(background);
        getContentRoot().getChildren().add(layout);
        FXGL.getSettings().setGlobalMusicVolume(1.0);
        FXGL.getSettings().setGlobalSoundVolume(1.0);
    }
}