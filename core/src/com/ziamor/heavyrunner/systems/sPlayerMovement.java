package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sPlayerMovement extends IteratingSystem {

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;

    public sPlayerMovement() {
        super(Aspect.all(cPosition.class, cVelocity.class).exclude(cStartRewind.class, cDead.class));
    }

    float maxX = 0.15f;

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);

        float dir = 1;
        if (velocity.x < 0)
            dir = -1;

        float xTarget = dir * maxX;
        velocity.x = velocity.x * (1 - world.getDelta() * 6) + xTarget * (world.getDelta() * 6);

        position.x += velocity.x * world.getDelta();

    }
}
