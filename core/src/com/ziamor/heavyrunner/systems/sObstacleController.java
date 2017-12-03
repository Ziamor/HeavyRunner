package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sObstacleController extends IteratingSystem {
    @EntityId
    int player = -1;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;
    ComponentMapper<cDead> deadComponentMapper;

    float speed = 500;
    float dir = 1f;

    float speedMul = 1f;

    public sObstacleController() {
        super(Aspect.all(cVelocity.class, cObstacle.class));
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
    }

    @Override
    protected void process(int entityId) {
        cDead dead = deadComponentMapper.get(player);
        if(dead != null)
            return;

        cVelocity velocity = velocityComponentMapper.get(entityId);
        velocity.x = -speed * dir * speedMul;
    }
}
