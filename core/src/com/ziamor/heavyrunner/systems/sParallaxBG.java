package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sParallaxBG extends IteratingSystem {
    @EntityId
    int player = -1;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cParallaxBG> parallaxBGComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;
    ComponentMapper<cDead> deadComponentMapper;

    sObstacleController obstacleController;
    float dir = 1f;
    cDead dead = null;

    public sParallaxBG() {
        super(Aspect.all(cPosition.class, cParallaxBG.class, cTexture.class));
    }

    @Override
    protected void begin() {
        if (player == -1)
            player = world.getSystem(TagManager.class).getEntityId("player");
        cStartRewind startRewind = startRewindComponentMapper.get(player);
        if (startRewind != null)
            dir = -1f;
        else
            dir = 1f;

        dead = deadComponentMapper.get(player);
    }

    @Override
    protected void process(int entityId) {
        if(dead != null)
            return;
        cPosition position = positionComponentMapper.get(entityId);
        cParallaxBG parallaxBG = parallaxBGComponentMapper.get(entityId);
        cTexture texture = textureComponentMapper.get(entityId);

        position.x -= dir * obstacleController.speed * parallaxBG.scrollFactor * world.getDelta();

        float offset = position.x + texture.texture.getWidth();
        if (offset <= -texture.texture.getWidth())
            position.x = texture.texture.getWidth() * 2 + offset;
    }
}
