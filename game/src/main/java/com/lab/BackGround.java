package com.lab;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class BackGround {
    
    public static Entity createBackground(){
        return FXGL.entityBuilder()
                .view(FXGL.texture("newmap2.png"))
                .at(0, 0)
                .build();
    }
}
