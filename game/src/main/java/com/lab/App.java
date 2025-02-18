package com.lab;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class App extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    private static Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Gonvival");
        settings.setVersion("beta");
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("enemies", 0);
        vars.put("playerHP", 100);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new UnitFactory());
        FXGL.getGameWorld().addEntities(BackGround.createBackground());

        FXGL.run(() -> {

            int numEnemy = FXGL.getWorldProperties().getInt("enemies");

            if (numEnemy < 5) { // กำหนดสร้างศตรูไม่เกิน 5 ตัว ใน 1 วิ
                FXGL.getGameWorld().spawn("Enemy",
                        FXGLMath.random(0, FXGL.getAppWidth() - 40),
                        FXGLMath.random(0, FXGL.getAppHeight() / 2 - 30));
                FXGL.getWorldProperties().increment("enemies", +1);
            }

        }, Duration.seconds(1));

        player = FXGL.entityBuilder()
                .at(400, 400)
                .type(EntityType.PLAYER)
                .viewWithBBox("g.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    public static Entity getPlayer() {
        return player;
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsworld = FXGL.getPhysicsWorld();

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.ENEMY) { // ลบศัตรูเมือกระสุนโดนศัตรู
            @Override
            protected void onCollisionBegin(Entity bullet, Entity enemy) {
                bullet.removeFromWorld();
                enemy.removeFromWorld();

                FXGL.getWorldProperties().increment("enemies", -1);
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity player) {
                FXGL.inc("playerHP", -20);
                enemy.removeFromWorld();
                FXGL.getWorldProperties().increment("enemies", -1);
                if (FXGL.geti("playerHP") <= 0) {
                    FXGL.showMessage("Game Over", () -> FXGL.getGameController().gotoMainMenu());
                }

            }
        });
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();

        input.addAction(new UserAction("SHOOT") {
            @Override
            protected void onActionBegin() {
                Point2D mousePos = new Point2D(input.getMouseXWorld(), input.getMouseYWorld());
                Point2D direction = mousePos.subtract(player.getPosition()).normalize();

                Entity bullet = FXGL.getGameWorld().spawn("Bullet", player.getX(), player.getY());
                bullet.addComponent(new ProjectileComponent(direction, 500));
            }
        }, MouseButton.PRIMARY);

        FXGL.onKey(KeyCode.A, "MOVE LEFT", () -> {
            player.translateX(-2);
        });

        FXGL.onKey(KeyCode.D, "MOVE RIGHT", () -> {
            player.translateX(2);
        });

        FXGL.onKey(KeyCode.W, "MOVE UP", () -> {
            player.translateY(-2);
        });

        FXGL.onKey(KeyCode.S, "MOVE DOWN", () -> {
            player.translateY(2);
        });
    }

    @Override
    protected void initUI() {
        Text hpLabel = new Text("HP :");
        Text hpText = new Text();

        hpLabel.setTranslateX(50);
        hpLabel.setTranslateY(100);
        hpText.setTranslateX(75);
        hpText.setTranslateY(100);

        FXGL.getGameScene().addUINode(hpLabel);
        FXGL.getGameScene().addUINode(hpText);

        hpText.textProperty().bind(FXGL.getWorldProperties().intProperty("playerHP").asString());
    }
}