package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sObstacleController extends IteratingSystem {
    ComponentMapper<cVelocity> velocityComponentMapper;

    public sObstacleController() {
        super(Aspect.all(cVelocity.class, cObstacle.class));
    }

    @Override
    protected void process(int entityId) {
        cVelocity velocity = velocityComponentMapper.get(entityId);
    }
}
