package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.heavyrunner.components.*;

public class sPlayerMovement extends IteratingSystem {

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cAcceleration> accelerationComponentMapper;

    public sPlayerMovement() {
        super(Aspect.all(cPosition.class, cVelocity.class, cAcceleration.class).exclude(cStartRewind.class, cDead.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);
        cAcceleration acceleration = accelerationComponentMapper.get(entityId);

        float xTarget = acceleration.x;

        velocity.x = velocity.x * (1 - world.getDelta() * 6) + xTarget * (world.getDelta() * 6);
        Gdx.app.log("", velocity.x + "");
        position.x += velocity.x * world.getDelta();
    }
}
