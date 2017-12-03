package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.sun.deploy.util.Waiter;
import com.ziamor.heavyrunner.components.*;

public class sDirector extends IntervalSystem {
    @EntityId
    int player = -1;

    @Wire
    AssetManager assetManager;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cIgnoreGravity> ignoreGravityComponentMapper;
    ComponentMapper<cObstacle> obstacleComponentMapper;
    ComponentMapper<cSize> sizeComponentMapper;
    ComponentMapper<cAABB> aabbComponentMapper;
    ComponentMapper<cWall> wallComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;

    Texture wallTexture;

    float xScreenEnd;

    int minPlatformLength = 3;
    int maxPlatformLenth = 7;
    float wallSize = 32;

    boolean rewinding;
    public sDirector() {
        super(Aspect.all(), 1);
    }

    @Override
    protected void begin() {
        if (player == -1)
            player = world.getSystem(TagManager.class).getEntityId("player");
        cStartRewind startRewind = startRewindComponentMapper.get(player);
        if (startRewind != null)
            rewinding = true;
        else
            rewinding = false;

        if (wallTexture == null)
            wallTexture = assetManager.get("wall.png", Texture.class);
        xScreenEnd = Gdx.graphics.getWidth();
    }

    @Override
    protected void processSystem() {
        if(rewinding)
            return;
        float y = MathUtils.random(0, Gdx.graphics.getHeight() - wallSize);
        int num = MathUtils.random(minPlatformLength, maxPlatformLenth);
        for (int i = 0; i < num; i++)
            spawnPlatform(xScreenEnd + i * wallSize, y);
        createCollisionEntity(xScreenEnd, y, num);
    }

    protected void spawnPlatform(float x, float y) {
        int wall = world.create();

        cPosition wallPos = positionComponentMapper.create(wall);
        cTexture tex = textureComponentMapper.create(wall);
        cSize size = sizeComponentMapper.create(wall);
        velocityComponentMapper.create(wall);

        wallComponentMapper.create(wall);
        ignoreGravityComponentMapper.create(wall);
        obstacleComponentMapper.create(wall);

        wallPos.x = x;
        wallPos.y = y;
        tex.texture = wallTexture;
        size.width = tex.texture.getWidth();
        size.height = tex.texture.getHeight();
    }

    protected void createCollisionEntity(float x, float y, int platformSize) {
        int collisionEnt = world.create();
        cPosition wallPos = positionComponentMapper.create(collisionEnt);
        cSize size = sizeComponentMapper.create(collisionEnt);

        velocityComponentMapper.create(collisionEnt);
        wallComponentMapper.create(collisionEnt);
        ignoreGravityComponentMapper.create(collisionEnt);
        aabbComponentMapper.create(collisionEnt);
        obstacleComponentMapper.create(collisionEnt);

        wallPos.x = x;
        wallPos.y = y;

        size.width = platformSize * wallSize;
        size.height = wallSize;
    }
}
