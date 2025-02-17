package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

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
}
