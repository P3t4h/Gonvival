package com.lab;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UnitFactory implements EntityFactory{
    @Spawns("Bullet")
    public Entity newBullet(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox("arrow.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("Enemy")
    public Entity enemy(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.ENEMY)
                .viewWithBBox("evil.png")
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .build();
    }

    @Spawns("Boss")
    public Entity boss(SpawnData data) {
        return entityBuilder()
                .type(EntityType.BOSS)
                .viewWithBBox(new Rectangle(25,25,Color.RED))
                .with(new CollidableComponent(true), new EnemyControl())
                .build();
    }

    @Spawns("Wall")
    public Entity newWall(SpawnData data) {
        Rectangle wallShape = new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"));
        wallShape.setOpacity(0);

        return entityBuilder(data)
                .type(EntityType.WALL)
                .viewWithBBox(wallShape)
                .collidable()
                .build();
    }
}
