import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.app.scene.MenuType;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MainMenu extends FXGLMenu {

    public MainMenu() {
        super(MenuType.MAIN_MENU);
        Texture bgTexture = getAssetLoader().loadTexture("background.png");
        ImageView bg = new ImageView(bgTexture.getImage());
        bg.setFitWidth(getAppWidth());
        bg.setFitHeight(getAppHeight());
        VBox menuBox = new VBox(15);
        menuBox.setTranslateX(getAppWidth() / 2 - 50);
        menuBox.setTranslateY(getAppHeight() / 2 - 50);
        Button btnStart = new Button("Start Game");
        btnStart.setOnAction(e -> getGameController().startNewGame());
        Button btnExit = new Button("Exit");
        btnExit.setOnAction(e -> FXGL.getGameController().exit());

        menuBox.getChildren().addAll(btnStart, btnExit);
        getContentRoot().getChildren().addAll(bg, menuBox);
    }
}