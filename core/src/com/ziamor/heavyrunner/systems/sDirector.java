package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.ziamor.heavyrunner.components.*;

public class sDirector extends BaseEntitySystem {
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
    float wallDepth = 5;
    boolean rewinding;

    float min_x_Gap = wallSize * 2;
    float max_x_Gap = wallSize * 8;

    float max_y_gap = wallSize * 4;
    @EntityId
    int lastPlatform = -1;

    public sDirector() {
        super(Aspect.all());
        doneSetup = false;
    }

    boolean doneSetup;

    @Override
    protected void begin() {
        if (player == -1)
            player = world.getSystem(TagManager.class).getEntityId("player");
        cStartRewind startRewind = startRewindComponentMapper.get(player);
        if (startRewind != null) {
            rewinding = true;

        } else {
            rewinding = false;
        }

        if (wallTexture == null)
            wallTexture = assetManager.get("wall.png", Texture.class);
        xScreenEnd = Gdx.graphics.getWidth();
    }

    public void init() {
        doneSetup = true;
        float y = Gdx.graphics.getHeight() / 2;
        int num = (int) ((Gdx.graphics.getWidth() - min_x_Gap) / wallSize);
        for (int i = 0; i < num; i++)
            spawnPlatform(i * wallSize, y);
        lastPlatform = createCollisionEntity(0, y, num);
    }

    @Override
    protected void processSystem() {
        if (!doneSetup) {
            init();
        }

        if (rewinding)
            return;
        cAABB lastWallAABB = null;
        if (lastPlatform != -1) {
            lastWallAABB = aabbComponentMapper.get(lastPlatform);
            if (lastWallAABB != null && lastWallAABB.aabb != null) {
                float dist = Gdx.graphics.getWidth() - (lastWallAABB.aabb.x + lastWallAABB.aabb.width);
                if (dist < min_x_Gap)
                    return;
                else if (dist < max_x_Gap) {
                    float r = MathUtils.random();
                    Gdx.app.log("", "" + r);
                    if (r < 0.9)
                        return;
                }
            }
        }
        float y = 0;
        if (lastWallAABB == null)
            y = MathUtils.random(0, Gdx.graphics.getHeight() - wallSize);
        else
            y = MathUtils.random(Math.max(0, lastWallAABB.aabb.y - max_y_gap), Math.min(lastWallAABB.aabb.y + max_y_gap, Gdx.graphics.getHeight() - wallSize * 4));
        int num = MathUtils.random(minPlatformLength, maxPlatformLenth);
        for (int i = 0; i < num; i++)
            spawnPlatform(xScreenEnd + i * wallSize, y);
        lastPlatform = createCollisionEntity(xScreenEnd, y, num);
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
        wallPos.z = wallDepth;
        tex.texture = wallTexture;
        size.width = tex.texture.getWidth();
        size.height = tex.texture.getHeight();
    }

    protected int createCollisionEntity(float x, float y, int platformSize) {
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

        return collisionEnt;
    }
}
