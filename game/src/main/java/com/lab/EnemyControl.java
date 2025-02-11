package com.lab;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class EnemyControl extends Component{
    private Entity player;
    private static final double SPEED = 2; //Speed ของมอนที่จะวิ่งใส่player

    @Override
    public void onAdded() {
        player = App.getPlayer();
    }

    @Override
    public void onUpdate(double tpf){
        if(player != null){
            Point2D direction = player.getPosition().subtract(getEntity().getPosition()).normalize(); //กำหนดตำแหน่งplayer
            getEntity().translate(direction.multiply(SPEED*tpf*60)); //ส่วนนี้ทำหน้าที่ทำให้มอนเดินตามPlayer
        }
    }
}
