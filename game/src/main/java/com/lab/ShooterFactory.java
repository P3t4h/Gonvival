package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import javafx.geometry.Point2D;

public class ShooterFactory implements EntityFactory{

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(ShooterType.BULLET)
                .viewWithBBox("arrow.png")
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(0, -1), 300))
                .build();
    }

    @Spawns("Enemy")
    public Entity enemy(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(ShooterType.ENEMY)
                .viewWithBBox("evil.png")
                .with(new CollidableComponent(true))
                .build();
    }
}
