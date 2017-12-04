package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.heavyrunner.components.*;

public class sObstacleController extends IteratingSystem {
    @EntityId
    int player = -1;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;
    ComponentMapper<cDead> deadComponentMapper;

    float speed = 250;
    float dir = 1f;

    float speedMul = 1f;

    sRewindTime rewindTime;

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
        speedMul = rewindTime.curDeSync + 1.0f;
        Gdx.app.debug("", "Speed: " + speed * speedMul + "Mul: " + speedMul);
    }

    @Override
    protected void process(int entityId) {
        cDead dead = deadComponentMapper.get(player);
        if (dead != null)
            return;

        cPosition position = positionComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);
        velocity.x = -speed * dir * speedMul;

        position.x += velocity.x * world.getDelta();
    }
}
