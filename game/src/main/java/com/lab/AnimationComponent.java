package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component{
    private int speedX = 0;
    private int speedY = 0;

    public void setSpeed(int x, int y){
        this.speedX = x;
        this.speedY = y;
    }

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkRight, animWalkLeft, animWalkUp, animWalkDown;
    private AnimationChannel animWalkUpRight, animWalkUpLeft, animWalkDownLeft, animWalkDownRight;

    public AnimationComponent() {
        animIdle = new AnimationChannel(FXGL.image("idle.png"), 9, 48, 64, Duration.seconds(1), 0, 8);

        animWalkLeft = new AnimationChannel(FXGL.image("walk_left_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkRight = new AnimationChannel(FXGL.image("walk_right_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkUp = new AnimationChannel(FXGL.image("walk_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDown = new AnimationChannel(FXGL.image("walk_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);

        animWalkUpLeft = new AnimationChannel(FXGL.image("walk_left_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkUpRight = new AnimationChannel(FXGL.image("walk_right_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDownLeft = new AnimationChannel(FXGL.image("walk_left_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDownRight = new AnimationChannel(FXGL.image("walk_right_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);


        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded(){
        entity.getTransformComponent().setScaleOrigin(new Point2D(24, 32));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdle);
    }

    private void changeAnimation(AnimationChannel anim){
        if(texture.getAnimationChannel() != anim){
            texture.loopAnimationChannel(anim);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (speedX == 0 && speedY == 0) {
            changeAnimation(animIdle);
            return;
        }

        if (speedX > 0 && speedY < 0) { //เดินเฉียงขึ้นขวา
            changeAnimation(animWalkUpRight);
        } else if (speedX < 0 && speedY < 0) { //เดินเฉียงขึ้นซ้าย
            changeAnimation(animWalkUpLeft); 
        } else if (speedX > 0 && speedY > 0) { //เดินเฉียงลงขวา
            changeAnimation(animWalkDownRight);
        } else if (speedX < 0 && speedY > 0) { //เดินเฉียงลงซ้าย
            changeAnimation(animWalkDownLeft);
        } else if (speedX > 0) { //เดินขวา
            changeAnimation(animWalkRight);
        } else if (speedX < 0) { //เดินซ้าย
            changeAnimation(animWalkLeft);
        } else if (speedY > 0) { //เดินลง
            changeAnimation(animWalkDown);
        } else if (speedY < 0) { //เดินขึ้น
            changeAnimation(animWalkUp);
        }
    }
    
}
