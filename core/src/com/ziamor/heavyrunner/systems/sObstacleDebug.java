package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.heavyrunner.components.cObstacle;

public class sObstacleDebug extends BaseEntitySystem {

    public sObstacleDebug() {
        super(Aspect.all(cObstacle.class));
    }

    @Override
    protected void processSystem() {
        Gdx.app.debug("Obstacle Debug", "Number of active obstacles: " + this.getEntityIds().size());
    }
}
