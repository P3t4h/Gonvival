package com.lab;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

import javafx.geometry.Point2D;
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
                .view("GraveRevenant.png")
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .bbox(new HitBox("Enemy",new Point2D(6, 16),BoundingShape.box(36, 32)))
                .build();
    }

    @Spawns("Boss")
    public Entity boss(SpawnData data) {
        return entityBuilder()
                .type(EntityType.BOSS)
                .at(400,400)
                .bbox(new HitBox("Boss", new Point2D(12, 14), BoundingShape.box(36, 32)))
                .anchorFromCenter()
                .with(new CollidableComponent(true),new AnimationBoss())
                .buildAndAttach();
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

    @Spawns("Spike")
    public Entity spike(SpawnData data) {
        Rectangle wallShape = new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"));
        wallShape.setOpacity(0);

        return entityBuilder(data)
                .type(EntityType.SPIKE)
                .viewWithBBox(wallShape)
                .collidable()
                .build();
    }
}
