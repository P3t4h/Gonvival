package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component{
    private String lastdirection = "DOWN";
    private int speedX = 0;
    private int speedY = 0;

    public void setSpeed(int x, int y){
        this.speedX = x;
        this.speedY = y;
    }

    private AnimatedTexture texture;
    private AnimationChannel animIdleD, animIdleU, animIdleLD, animIdleRD, animIdleLU, animIdleRU;
    private AnimationChannel animWalkRight, animWalkLeft, animWalkUp, animWalkDown;
    private AnimationChannel animWalkUpRight, animWalkUpLeft, animWalkDownLeft, animWalkDownRight;

    public AnimationComponent() {
        animIdleD = new AnimationChannel(FXGL.image("idle_down.png"), 8, 48, 64, Duration.seconds(1), 0, 7);
        animIdleU = new AnimationChannel(FXGL.image("idle_up.png"), 8, 48, 64, Duration.seconds(1), 0, 7);
        animIdleLD = new AnimationChannel(FXGL.image("idle_left_down.png"), 8, 48, 64, Duration.seconds(1), 0, 7);
        animIdleLU = new AnimationChannel(FXGL.image("idle_left_up.png"), 8, 48, 64, Duration.seconds(1), 0, 7);
        animIdleRD = new AnimationChannel(FXGL.image("idle_right_down.png"), 8, 48, 64, Duration.seconds(1), 0, 7);
        animIdleRU = new AnimationChannel(FXGL.image("idle_right_up.png"), 8, 48, 64, Duration.seconds(1), 0, 7);

        animWalkLeft = new AnimationChannel(FXGL.image("walk_left_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkRight = new AnimationChannel(FXGL.image("walk_right_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkUp = new AnimationChannel(FXGL.image("walk_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDown = new AnimationChannel(FXGL.image("walk_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);

        animWalkUpLeft = new AnimationChannel(FXGL.image("walk_left_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkUpRight = new AnimationChannel(FXGL.image("walk_right_up.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDownLeft = new AnimationChannel(FXGL.image("walk_left_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);
        animWalkDownRight = new AnimationChannel(FXGL.image("walk_right_down.png"), 8, 48, 64, Duration.seconds(0.5), 0, 7);


        texture = new AnimatedTexture(animIdleD);
    }

    @Override
    public void onAdded(){
        entity.getTransformComponent().setScaleOrigin(new Point2D(24, 32));
        entity.getTransformComponent().setScaleX(1.5);
        entity.getTransformComponent().setScaleY(1.5);
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdleD);
    }

    private void changeAnimation(AnimationChannel anim){
        if(texture.getAnimationChannel() != anim){
            texture.loopAnimationChannel(anim);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (speedX == 0 && speedY == 0) {
            switch (lastdirection) {
                case "UP":
                    changeAnimation(animIdleU);
                    break;
                case "DOWN":
                    changeAnimation(animIdleD);
                    break;
                case "LEFT_DOWN":
                    changeAnimation(animIdleLD);
                    break;
                case "LEFT_UP":
                    changeAnimation(animIdleLU);
                    break;
                case "RIGHT_DOWN":
                    changeAnimation(animIdleRD);
                    break;
                case "RIGHT_UP":
                    changeAnimation(animIdleRU);
                    break;
            }
            return;
        }

        if (speedX > 0 && speedY < 0) { //เดินเฉียงขึ้นขวา
            changeAnimation(animWalkUpRight);
            lastdirection = "RIGHT_UP";
        } else if (speedX < 0 && speedY < 0) { //เดินเฉียงขึ้นซ้าย
            changeAnimation(animWalkUpLeft);
            lastdirection = "LEFT_UP"; 
        } else if (speedX > 0 && speedY > 0) { //เดินเฉียงลงขวา
            changeAnimation(animWalkDownRight);
            lastdirection = "RIGHT_DOWN";
        } else if (speedX < 0 && speedY > 0) { //เดินเฉียงลงซ้าย
            changeAnimation(animWalkDownLeft);
            lastdirection = "LEFT_DOWN";
        } else if (speedX > 0) { //เดินขวา
            changeAnimation(animWalkRight);
            lastdirection = "RIGHT_DOWN";
        } else if (speedX < 0) { //เดินซ้าย
            changeAnimation(animWalkLeft);
            lastdirection = "LEFT_DOWN";
        } else if (speedY > 0) { //เดินลง
            changeAnimation(animWalkDown);
            lastdirection = "DOWN";
        } else if (speedY < 0) { //เดินขึ้น
            changeAnimation(animWalkUp);
            lastdirection = "UP";
        }
    }
    
}
