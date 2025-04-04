package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class GuideScreen extends VBox {

    public GuideScreen() {
        setSpacing(20);
        setAlignment(javafx.geometry.Pos.CENTER); // จัดให้อยู่ตรงกลาง

        // ขนาดหน้าจอ
        double screenWidth = FXGL.getAppWidth();
        double screenHeight = FXGL.getAppHeight();

        // ตั้งตำแหน่ง X และ Y ให้อยู่ตรงกลาง
        setTranslateX(screenWidth / 2 - 150); // Offset 150 สำหรับข้อความและปุ่ม
        setTranslateY(screenHeight / 2 - 100); // Offset 100 เพื่อความสมดุล

        Text guideTitle = new Text("Game Guide");
        guideTitle.setFont(Font.font("Verdana", 50));
        guideTitle.setFill(Color.DARKBLUE);

        Text guideContent = new Text(
            "1. Use W,A,S,D to move.\n" +
            "2. LeftClick to Shoot.\n" +
            "3. Collect points much as you can.\n" +
            "4. Survive the monster.\n" +
            "5. Don't get near the wall and spike!"
        );

        guideContent.setFont(Font.font("Verdana", 20));
        guideContent.setFill(Color.BLACK);

        Button continueButton = new Button("Continue");
        continueButton.setPrefSize(200, 50);
        continueButton.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        continueButton.setOnAction(e -> {
            FXGL.getGameScene().getContentRoot().getChildren().remove(this); // ลบ Guide Screen
            FXGL.getGameController().resumeEngine();
            FXGL.getGameScene().getContentRoot().getChildren().remove(this);
        });        

        getChildren().addAll(guideTitle, guideContent, continueButton);
    }
}