package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sGravity extends IteratingSystem {

    float gravity = 800; // TODO pass this from somewhere

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cOnGround> onGroundComponentMapper;
    ComponentMapper<cDead> deadComponentMapper;

    public sGravity() {
        super(Aspect.all(cPosition.class, cVelocity.class).exclude(cIgnoreGravity.class, cStartRewind.class, cDead.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);
        cOnGround onGround = onGroundComponentMapper.get(entityId);

        if (velocity.y < 0 && onGround != null)
            velocity.y = 0;

        position.y += velocity.y * world.getDelta();

        //Apply gravity
        velocity.y -= gravity * world.getDelta();

        if (position.y <= 0) {
            position.y = 0;
            velocity.y = 0;
            deadComponentMapper.create(entityId);
        }
    }
}
