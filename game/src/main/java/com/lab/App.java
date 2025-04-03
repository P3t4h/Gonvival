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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.FXGLMenu;

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
        vars.put("point",0);
        vars.put("level", 1);
        vars.put("highScore",0);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new UnitFactory());
        FXGL.getGameWorld().addEntities(BackGround.createBackground());

        int mapWidth = 800;
        int mapHeight = 800;
        int wallThickness = 25;

        levelUI = new LevelUI(); // กำหนดค่าเริ่มต้นของ UI
        levelUI.addToScene(); // เพิ่ม UI เข้าไปในเกม

        FXGL.run(() -> {

            int numEnemy = FXGL.getWorldProperties().getInt("enemies");

            if (numEnemy < 5) { // กำหนดสร้างศตรูไม่เกิน 5 ตัว ใน 1 วิ
                FXGL.getGameWorld().spawn("Enemy",
                        FXGLMath.random(0, FXGL.getAppWidth() - 40),
                        FXGLMath.random(0, FXGL.getAppHeight() / 2 - 30));
                FXGL.getWorldProperties().increment("enemies", +1);
            }

        }, Duration.seconds(1));

        new CountdownTimer(); //จับเวลา

        FXGL.spawn("Wall",new SpawnData(0, 0).put("width",mapWidth).put("height", wallThickness)); // กำหนด ตำแหน่ง/ความกว้าง/ความสูง ของ Wall บน
        FXGL.spawn("Wall",new SpawnData(0, mapHeight - wallThickness).put("width",mapWidth).put("height", wallThickness)); // Wall ล่าง
        FXGL.spawn("Wall",new SpawnData(0, 0).put("width", wallThickness).put("height", mapHeight)); // Wall ซ้าย
        FXGL.spawn("Wall",new SpawnData(mapWidth - wallThickness, 0).put("width", wallThickness).put("height", mapHeight)); // Wall ขวา

        FXGL.spawn("Spike",new SpawnData(112, 208).put("width", 112).put("height", 16)); // กำหนด ตำแหน่ง/ความกว้าง/ความสูง ของ Spike แนวนอน ฝั่งซ้ายบน
        FXGL.spawn("Spike",new SpawnData(160, 160).put("width", 16).put("height", 112)); // spike แนวตั้ง ฝั่งซ้ายบน

        FXGL.spawn("Spike",new SpawnData(112, 592).put("width", 112).put("height", 16)); // spike แนวนอน ฝั่งซ้ายล่าง
        FXGL.spawn("Spike",new SpawnData(160, 544).put("width", 16).put("height", 112)); // spike แนวตั้ง ฝั่งซ้ายล่าง

        FXGL.spawn("Spike",new SpawnData(578, 208).put("width", 112).put("height", 16)); // spike แนวนอน ฝั่งขวาบน
        FXGL.spawn("Spike",new SpawnData(626, 160).put("width", 16).put("height", 112)); // spike แนวตั้ง ฝั่งขวาบน

        FXGL.spawn("Spike",new SpawnData(578, 592).put("width", 112).put("height", 16)); // spike แนวนอน ฝั่งขวาล่าง
        FXGL.spawn("Spike",new SpawnData(626, 544).put("width", 16).put("height", 112)); // spike แนวตั้ง ฝั่งขวาล่าง

        FXGL.spawn("Spike",new SpawnData(368, 208).put("width", 80).put("height", 16)); // spike แนวนอน ตรงกลาง

        player = FXGL.entityBuilder()
                .at(400, 550)
                .type(EntityType.PLAYER)
                .with(new CollidableComponent(true))
                .with(new AnimationComponent())
                .bbox(new HitBox("Player", new Point2D(12, 14), BoundingShape.box(14, 24)))
                .anchorFromCenter()
                .buildAndAttach();
        
        FXGL.runOnce(() -> initInput(), Duration.seconds(0.1));
    }

    private void increasePoint(int amount) {
        int currentPoint = FXGL.geti("point");
        FXGL.set("point", currentPoint + amount); // เพิ่มคะแนน Point
    }

    private LevelUI levelUI;

    private void checkLevelUp() {  // ระบบ Level
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

        levelUI.updateLevelCount(newLevel); // อัปเดต UI ของ Level
        levelUI.checkAndUpdateHighLevel(); // ตรวจสอบและอัปเดต High Level

        checkLevelUp();
        }
    }

    public int getLevel(){
        return FXGL.geti("level");
    }
    
    public static Entity getPlayer() { // Update ตำแหน่ง player
        return player;
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsworld = FXGL.getPhysicsWorld();

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.ENEMY) { // ลบศัตรูเมือกระสุนโดนศัตรู
            @Override
            protected void onCollisionBegin(Entity bullet, Entity enemy) {
                increasePoint(10);
                bullet.removeFromWorld();
                enemy.removeFromWorld();

                FXGL.getWorldProperties().increment("enemies", -1);
                FXGL.getWorldProperties().increment("exp", 8);
                FXGL.getWorldProperties().increment("point", 10);

                checkLevelUp();
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.PLAYER) { // ลดเลือด Player เมือโดน Enemy และ Enemy หายไป
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

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BOSS, EntityType.PLAYER) { // Player ตายเมือโดน Boss
            @Override
            protected void onCollisionBegin(Entity boss, Entity player) {
                FXGL.inc("playerHP", -9999);

                if (FXGL.geti("playerHP") <= 0) {
                    FXGL.showMessage("Game Over\nHighest Level:" , () -> { 
                        FXGL.getGameWorld().reset();
                        FXGL.getGameController().gotoMainMenu();
                    });
                }
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BOSS) { // ลบกระสุนโดน Boss และเพิ่ม EXP
            @Override
            protected void onCollisionBegin(Entity bullet, Entity boss) {
                bullet.removeFromWorld();
                FXGL.getWorldProperties().increment("exp", 10);
                FXGL.getWorldProperties().increment("point", 10);
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.WALL) { // ลบกระสุนโดนกำแพง
            @Override
            protected void onCollisionBegin(Entity bullet, Entity wall) {
                bullet.removeFromWorld();
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALL) { // Player ตายเมือโดน Wall
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

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.SPIKE) { // ลบกระสุนเมือโดนหนาม
            @Override
            protected void onCollisionBegin(Entity bullet, Entity spike) {
                bullet.removeFromWorld();
            }
        });

        physicsworld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.SPIKE) { // Player ตายเมือโดนหนาม
            @Override
            protected void onCollisionBegin(Entity player, Entity spike) {
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

    private void shootFollowArrow() { // ยิงตามเมาส์
        Input input = FXGL.getInput();
        Point2D mousePos = new Point2D(input.getMouseXWorld(), input.getMouseYWorld());

        Point2D direction = mousePos.subtract(player.getPosition()).normalize();

        Entity bullet = FXGL.getGameWorld().spawn("Bullet", player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
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
        input.addAction(new UserAction("SHOOT") { // ยิง
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

    if (!isMoveLeftActionBound) { // 
        input.addAction(new UserAction("MOVE LEFT") { // เดินซ้าย
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
        input.addAction(new UserAction("MOVE RIGHT") { // เดินขวา
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
        input.addAction(new UserAction("MOVE UP") { // เดินบน
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
        input.addAction(new UserAction("MOVE DOWN") { // เดินลง
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

    Rectangle levelBackground = new Rectangle(120, 40); // กล่อง LVL
    levelBackground.setTranslateX(640);
    levelBackground.setTranslateY(105);
    levelBackground.setFill(Color.WHITE);
    levelBackground.setStroke(Color.BLACK);
    levelBackground.setStrokeWidth(2);

    Rectangle background = new Rectangle(400, 200);
    background.setTranslateX(200); // ตำแหน่ง X
    background.setTranslateY(150); // ตำแหน่ง Y
    background.setFill(Color.LIGHTGRAY);
    background.setStroke(Color.BLACK);
    background.setStrokeWidth(2);

    // Lv Label
    Text levelLabel = new Text("Lv :");
    levelLabel.setTranslateX(650);
    levelLabel.setTranslateY(130);
    levelLabel.setFill(Color.GOLD);
    levelLabel.setFont(uiFont);

    // เลข Level
    Text levelText = new Text();
    levelText.setTranslateX(700);
    levelText.setTranslateY(130);
    levelText.setFill(Color.GOLD);
    levelText.setFont(uiFont);
    levelText.textProperty().bind(FXGL.getWorldProperties().intProperty("level").asString());

    Rectangle hpBackground = new Rectangle(120, 40);
    hpBackground.setTranslateX(640);
    hpBackground.setTranslateY(65);
    hpBackground.setFill(Color.WHITE);
    hpBackground.setStroke(Color.BLACK);
    hpBackground.setStrokeWidth(2);

    // HP Label
    Text hpLabel = new Text("HP :");
    hpLabel.setTranslateX(650); 
    hpLabel.setTranslateY(90); 
    hpLabel.setFill(Color.RED);
    hpLabel.setFont(uiFont);

    // เลข HP
    Text hpText = new Text();
    hpText.setTranslateX(700);
    hpText.setTranslateY(90); 
    hpText.setFill(Color.RED);
    hpText.setFont(uiFont);
    hpText.textProperty().bind(FXGL.getWorldProperties().intProperty("playerHP").asString());

    //เพิ่ม UI
    FXGL.getGameScene().addUINode(hpBackground);
    FXGL.getGameScene().addUINode(hpLabel);
    FXGL.getGameScene().addUINode(hpText);
    FXGL.getGameScene().addUINode(levelBackground);
    FXGL.getGameScene().addUINode(levelLabel);
    FXGL.getGameScene().addUINode(levelText);
    }
}