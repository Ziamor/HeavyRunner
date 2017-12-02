package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ziamor.heavyrunner.components.*;

public class sDirector extends BaseEntitySystem {
    @Wire
    AssetManager assetManager;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cIgnoreGravity> ignoreGravityComponentMapper;
    ComponentMapper<cObstacle> obstacleComponentMapper;

    public sDirector() {
        super(Aspect.all());
    }

    @Override
    protected void processSystem() {
        spawnPlatform();
    }

    protected void spawnPlatform() {
        int wall = world.create();

        cPosition wallPos = positionComponentMapper.create(wall);
        cTexture wallTexture = textureComponentMapper.create(wall);
        velocityComponentMapper.create(wall);
        ignoreGravityComponentMapper.create(wall);
        obstacleComponentMapper.create(wall);

        wallPos.x = 500;
        wallPos.y = 250;
        wallTexture.texture = assetManager.get("wall.png", Texture.class);
    }
}
