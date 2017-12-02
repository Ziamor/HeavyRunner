package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.ziamor.heavyrunner.components.*;

public class sDirector extends IntervalSystem {
    @Wire
    AssetManager assetManager;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cIgnoreGravity> ignoreGravityComponentMapper;
    ComponentMapper<cObstacle> obstacleComponentMapper;

    Texture wallTexture;

    float xScreenEnd;

    int minPlatformLength = 3;
    int maxPlatformLenth = 7;
    float wallSize = 32;

    public sDirector() {
        super(Aspect.all(), 1);
    }

    @Override
    protected void begin() {
        if (wallTexture == null)
            wallTexture = assetManager.get("wall.png", Texture.class);
        xScreenEnd = Gdx.graphics.getWidth();
    }

    @Override
    protected void processSystem() {
        float y = MathUtils.random(0, Gdx.graphics.getHeight());
        int num = MathUtils.random(minPlatformLength, maxPlatformLenth);
        for (int i = 0; i < num; i++)
            spawnPlatform(xScreenEnd + i * wallSize, y);
    }

    protected void spawnPlatform(float x, float y) {
        int wall = world.create();

        cPosition wallPos = positionComponentMapper.create(wall);
        cTexture tex = textureComponentMapper.create(wall);
        velocityComponentMapper.create(wall);
        ignoreGravityComponentMapper.create(wall);
        obstacleComponentMapper.create(wall);

        wallPos.x = x;
        wallPos.y = y;
        tex.texture = wallTexture;
    }
}
