package com.lab;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getFileSystemService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.onBtnDown;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.app.scene.FXGLMenu;

public class App extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    private static Entity player;
    private Rectangle healthBar;
    private Text healthText;
    private Text pointsText; 

    @Override
    protected void initSettings(GameSettings settings) {
    settings.setWidth(800);
    settings.setHeight(800);
    settings.setTitle("Gonvival");
    settings.setVersion("beta");
    settings.setMainMenuEnabled(true);
    settings.setSceneFactory(new SceneFactory() {
        @Override
        public FXGLMenu newMainMenu() {
            return new StartScreen();
        }
    });
}
    
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("enemies", 0);
        vars.put("playerHP", 100);
        vars.put("exp",0);
        vars.put("level", 1);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new UnitFactory());
        FXGL.getGameWorld().addEntities(BackGround.createBackground());

        int mapWidth = 800;
        int mapHeight = 800;
        int wallThickness = 25;

        FXGL.run(() -> {

            int numEnemy = FXGL.getWorldProperties().getInt("enemies");

            if (numEnemy < 5) { // กำหนดสร้างศตรูไม่เกิน 5 ตัว ใน 1 วิ
                FXGL.getGameWorld().spawn("Enemy",
                        FXGLMath.random(0, FXGL.getAppWidth() - 40),
                        FXGLMath.random(0, FXGL.getAppHeight() / 2 - 30));
                FXGL.getWorldProperties().increment("enemies", +1);
            }

        }, Duration.seconds(1));

        new CountdownTimer();

        FXGL.spawn("Wall",new SpawnData(0, 0).put("width",mapWidth).put("height", wallThickness));
        FXGL.spawn("Wall",new SpawnData(0, mapHeight - wallThickness).put("width",mapWidth).put("height", wallThickness));
        FXGL.spawn("Wall",new SpawnData(0, 0).put("width", wallThickness).put("height", mapHeight));
        FXGL.spawn("Wall",new SpawnData(mapWidth - wallThickness, 0).put("width", wallThickness).put("height", mapHeight));

        player = FXGL.entityBuilder()
                .at(400, 400)
                .type(EntityType.PLAYER)
                .with(new CollidableComponent(true))
                .with(new AnimationComponent())
                .bbox(new HitBox("Main",new Point2D(6, 16),BoundingShape.box(36, 32)))
                .buildAndAttach();
        
        FXGL.runOnce(() -> initInput(), Duration.seconds(0.1));
    }

    private void checkLevelUp() {
        int currentExp = FXGL.geti("exp");
        int currentLevel = FXGL.geti("level");
        int nextLevelExp = (int) (20 * Math.pow(currentLevel, 1.5));
    
        if (currentExp >= nextLevelExp) {
            FXGL.getWorldProperties().increment("level", 1);
            FXGL.getWorldProperties().setValue("exp", currentExp - nextLevelExp);
            int newLevel = FXGL.geti("level");
            int hpIncrease = (int) (newLevel * 4);
            FXGL.inc("playerHP", hpIncrease);
    
            if (newLevel % 2 == 0) {
                FXGL.run(() -> {
                    FXGL.getGameWorld().spawn("Enemy",
                            FXGLMath.random(0, FXGL.getAppWidth()),
                            FXGLMath.random(0, FXGL.getAppHeight() / 2));
                    FXGL.getWorldProperties().increment("enemies", 1);
                }, Duration.seconds(0.8));
            }

            checkLevelUp();
        }
    }

    public int getLevel(){
        return FXGL.geti("level");
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
                FXGL.getWorldProperties().increment("exp", 8);

                checkLevelUp();
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity player) {
                FXGL.inc("playerHP", -20);
                enemy.removeFromWorld();

                FXGL.getWorldProperties().increment("enemies", -1);
                if (FXGL.geti("playerHP") <= 0) {
                    FXGL.showMessage("Game Over", () -> { 
                        FXGL.getGameWorld().reset();
                        FXGL.getGameController().gotoMainMenu();
                    });
                }
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOSS) {
            @Override
            protected void onCollisionBegin(Entity player, Entity boss) {
                int damageBoss = -10*getLevel();

                FXGL.inc("playerHP", -(damageBoss));
                System.out.println(damageBoss);

                if (FXGL.geti("playerHP") <= 0) {
                    FXGL.showMessage("Game Over", () -> { 
                        FXGL.getGameWorld().reset();
                        FXGL.getGameController().gotoMainMenu();
                    });
                }
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BOSS) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity boss) {
                double knockbackStr = -2*getLevel();

                boss.translateX(knockbackStr);
                bullet.removeFromWorld();
                
                System.out.println(knockbackStr);
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity wall) {
                bullet.removeFromWorld();
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                FXGL.inc("playerHP", -9999);

                if (FXGL.geti("playerHP") <= 0) {
                    FXGL.showMessage("Game Over", () -> { 
                        FXGL.getGameWorld().reset();
                        FXGL.getGameController().gotoMainMenu();
                    });
                }
            }
        });
    }

    private void shootFollowArrow() {
        Input input = FXGL.getInput();
        Point2D mousePos = new Point2D(input.getMouseXWorld(), input.getMouseYWorld());

        Point2D direction = mousePos.subtract(player.getPosition()).normalize();

        Entity bullet = FXGL.getGameWorld().spawn("Bullet", player.getX(), player.getY());
        bullet.addComponent(new ProjectileComponent(direction, 500));
    }

    private boolean isShootActionBound = false;
    private boolean isMoveLeftActionBound = false;
    private boolean isMoveRightActionBound = false;
    private boolean isMoveUpActionBound = false;
    private boolean isMoveDownActionBound = false;

    @Override
    protected void initInput() { // ตรวจสอบว่า player เป็น null หรือไม่
        if (player == null) {
            return;
    }

    Input input = FXGL.getInput();
    AnimationComponent anim = player.getComponentOptional(AnimationComponent.class).orElse(null);
    if (!isShootActionBound) {
        input.addAction(new UserAction("SHOOT") {
            @Override
            protected void onActionBegin() {
                shootFollowArrow();
                System.out.println("Player Shoots!");
            }
        }, MouseButton.PRIMARY);
        isShootActionBound = true;
    } else {
        System.out.println("Action 'SHOOT' already exists, skipping addAction.");
    }

    if (!isMoveLeftActionBound) {
        input.addAction(new UserAction("MOVE LEFT") {
            @Override
            protected void onAction() {
                player.translateX(-1);
                if (anim != null) anim.setSpeed(-1, 0);
            }

            @Override
            protected void onActionEnd() {
                if (anim != null) anim.setSpeed(0, 0);
            }
        }, KeyCode.A);
        isMoveLeftActionBound = true;
    }

    if (!isMoveRightActionBound) {
        input.addAction(new UserAction("MOVE RIGHT") {
            @Override
            protected void onAction() {
                player.translateX(1);
                if (anim != null) anim.setSpeed(1, 0);
            }

            @Override
            protected void onActionEnd() {
                if (anim != null) anim.setSpeed(0, 0);
            }
        }, KeyCode.D);
        isMoveRightActionBound = true;
    }

    if (!isMoveUpActionBound) {
        input.addAction(new UserAction("MOVE UP") {
            @Override
            protected void onAction() {
                player.translateY(-1);
                if (anim != null) anim.setSpeed(0, -1);
            }

            @Override
            protected void onActionEnd() {
                if (anim != null) anim.setSpeed(0, 0);
            }
        }, KeyCode.W);
        isMoveUpActionBound = true;
    }

    if (!isMoveDownActionBound) {
        input.addAction(new UserAction("MOVE DOWN") {
            @Override
            protected void onAction() {
                player.translateY(1);
                if (anim != null) anim.setSpeed(0, 1);
            }

            @Override
            protected void onActionEnd() {
                if (anim != null) anim.setSpeed(0, 0);
            }
        }, KeyCode.S);
        isMoveDownActionBound = true;
    }
}

    @Override
    protected void initUI() {
    Font uiFont = new Font("Arial", 24);

    // HP
    Text hpLabel = new Text("HP :");
    hpLabel.setTranslateX(100); 
    hpLabel.setTranslateY(25); 
    hpLabel.setFill(Color.RED);
    hpLabel.setFont(uiFont);

    // เลข HP 
    Text hpText = new Text();
    hpText.setTranslateX(150);
    hpText.setTranslateY(25); 
    hpText.setFill(Color.RED);
    hpText.setFont(uiFont);
    hpText.textProperty().bind(FXGL.getWorldProperties().intProperty("playerHP").asString());

    // ป้าย EXP
    Text expLabel = new Text("EXP :");
    expLabel.setTranslateX(650);
    expLabel.setTranslateY(80);
    expLabel.setFill(Color.PURPLE);
    expLabel.setFont(uiFont);

    Text expText = new Text();
    expText.setTranslateX(720); 
    expText.setTranslateY(80); 
    expText.setFill(Color.PURPLE);
    expText.setFont(uiFont);
    expText.textProperty().bind(FXGL.getWorldProperties().intProperty("exp").asString());

    // ป้ายกับเลข Level
    Text levelLabel = new Text("LVL :");
    levelLabel.setTranslateX(650);
    levelLabel.setTranslateY(140);
    levelLabel.setFill(Color.GOLD);
    levelLabel.setFont(uiFont);

    Text levelText = new Text();
    levelText.setTranslateX(720); 
    levelText.setTranslateY(140); 
    levelText.setFill(Color.GOLD);
    levelText.setFont(uiFont);
    levelText.textProperty().bind(FXGL.getWorldProperties().intProperty("level").asString());

    // แถบเลือดพื้นหลัง
    Rectangle healthBarBackground = new Rectangle(321, 17);
    healthBarBackground.setTranslateX(34);
    healthBarBackground.setTranslateY(50);
    healthBarBackground.setFill(Color.GRAY);

    // Health Bar
    Rectangle healthBar = new Rectangle(321, 17);
    healthBar.setTranslateX(34);
    healthBar.setTranslateY(50);
    healthBar.setFill(Color.RED);
    healthBar.widthProperty().bind(
    FXGL.getWorldProperties().intProperty("playerHP").divide(100.0).multiply(321)
    );

    // เพิ่ม UI
    FXGL.getGameScene().addUINode(hpLabel);
    FXGL.getGameScene().addUINode(hpText);
    FXGL.getGameScene().addUINode(expLabel);
    FXGL.getGameScene().addUINode(expText);
    FXGL.getGameScene().addUINode(levelLabel);
    FXGL.getGameScene().addUINode(levelText);
    FXGL.getGameScene().addUINode(healthBarBackground);
    FXGL.getGameScene().addUINode(healthBar);
    }
}