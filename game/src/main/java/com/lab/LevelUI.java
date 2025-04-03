package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LevelUI {

    private int levelCount = 1;
    private int highLevel; // เก็บ high level
    private Text levelText;
    private Text highLevelText;
    private ImageView levelIcon;

    public LevelUI() {
        // อ่าน high level จากไฟล์
        highLevel = HighLevelandScoreManager.readHighLevelandScore();

        levelIcon = new ImageView(new Image(FXGL.getAssetLoader().loadImage("Level.png").getUrl()));
        levelIcon.setFitWidth(30);
        levelIcon.setFitHeight(30);
        levelIcon.setTranslateX(20);
        levelIcon.setTranslateY(20);

        levelText = new Text(": " + levelCount);
        levelText.setFill(Color.BLACK);
        levelText.setStyle("-fx-font-size: 24px;");
        levelText.setTranslateX(60);
        levelText.setTranslateY(45);

        highLevelText = new Text("Highest Level: " + highLevel);
        highLevelText.setFill(Color.BLACK);
        highLevelText.setStyle("-fx-font-size: 24px;");
        highLevelText.setTranslateX(25);
        highLevelText.setTranslateY(75);

    }

    public void addToScene() {
        FXGL.getGameScene().addUINode(levelIcon);
        FXGL.getGameScene().addUINode(levelText);
        FXGL.getGameScene().addUINode(highLevelText);
    }

    public void incrementLevelCount() {
        levelCount++;
        levelText.setText(": " + levelCount);
    }

    public void updateLevelCount(int newLevelCount) {
        levelCount = newLevelCount;
        levelText.setText(": " + levelCount);
    }

    public int getLevelCount() {
        return levelCount;
    }

    // ฟังก์ชันการอัปเดต high level
    public void checkAndUpdateHighLevel() {
        if (levelCount > highLevel) {
            highLevel = levelCount;  // อัปเดต high level
            highLevelText.setText("Highest Level: " + highLevel);  // อัปเดตข้อความใน UI
            HighLevelandScoreManager.writeHighLevel(highLevel); // บันทึก high level ใหม่ลงในไฟล์
        }
    }

    public int getHighScore() {
        return highLevel;
    }
}