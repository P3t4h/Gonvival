package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class PointUI {

    private Text pointText; // คงไว้เฉพาะคะแนนปัจจุบัน
    private ImageView pointIcon;

    public PointUI() {
        
        // ไอคอนคะแนน
        pointIcon = new ImageView(new Image(FXGL.getAssetLoader().loadImage("point.jpg").getUrl()));
        pointIcon.setFitWidth(30);
        pointIcon.setFitHeight(30);
        pointIcon.setTranslateX(20);
        pointIcon.setTranslateY(20);

        // ข้อความแสดงคะแนนปัจจุบัน
        pointText = new Text();
        pointText.setFill(Color.BLACK);
        pointText.setStyle("-fx-font-size: 24px;");
        pointText.setTranslateX(60);
        pointText.setTranslateY(45);
        pointText.textProperty().bind(FXGL.getWorldProperties().intProperty("point").asString());
    }

    public void addToScene() {
        FXGL.getGameScene().addUINode(pointIcon);
        FXGL.getGameScene().addUINode(pointText);
    }

    public void incrementPoint(int amount) {
        FXGL.set("point", FXGL.geti("point") + amount);
    }
}