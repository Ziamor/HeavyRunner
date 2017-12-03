package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sObstacleCleaner extends IteratingSystem {
    ComponentMapper<cPosition> positionComponentMapper;

    public sObstacleCleaner() {
        super(Aspect.all(cPosition.class, cObstacle.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        if (position.x < -2000)
            world.delete(entityId);
    }
}
