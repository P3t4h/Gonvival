package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationBoss extends Component{
    
    private String lastDirection = "BossIdel";
    private int speedX = 0;
    private int speedY = 0;

    public void setSpeed(int x, int y){
        this.speedX = x;
        this.speedY = y;
    }
    
    private AnimatedTexture texture;
    private AnimationChannel idelBoss;
    private AnimationChannel castSpeel;

    public AnimationBoss(){
        idelBoss = new AnimationChannel(FXGL.image("IdleBoss.png"), 8, 250, 250, Duration.seconds(1), 0, 7);

        castSpeel = new AnimationChannel(FXGL.image("BossSkill.png"), 8, 400, 250, Duration.seconds(1), 0, 7);

        texture = new AnimatedTexture(idelBoss);
    }

    @Override
    public void onAdded(){
        entity.getTransformComponent().setScaleOrigin(new Point2D(125, 125));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(idelBoss);
    }

    private void changeAnimation(AnimationChannel anim){
        if(texture.getAnimationChannel() != anim){
            texture.loopAnimationChannel(anim);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (speedX == 0 && speedY == 0) {
            switch (lastDirection) {
                case "BossIdle":
                    changeAnimation(idelBoss);
                    break;
                case "CastSpeel":
                    changeAnimation(castSpeel);
                    break;
            }
            return;
        }

        if (speedX > 0 && speedY < 0) { //เดินเฉียงขึ้นขวา
            changeAnimation(idelBoss);
            lastDirection = "BossIdle";
        } else if (speedX < 0 && speedY < 0) { //เดินเฉียงขึ้นซ้าย
            changeAnimation(castSpeel);
            lastDirection = "CastSpell"; 
        }
    }
}
