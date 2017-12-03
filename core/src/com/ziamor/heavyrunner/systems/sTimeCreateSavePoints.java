package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.ziamor.heavyrunner.components.*;

public class sTimeCreateSavePoints extends IteratingSystem {
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cTimeSave> timeSaveComponentMapper;

    ProgressBar timeProgressBar;
    public sTimeCreateSavePoints(ProgressBar timeProgressBar) {
        super(Aspect.all(cTimeSave.class, cPosition.class, cVelocity.class).exclude(cStartRewind.class));
        this.timeProgressBar = timeProgressBar;
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);
        cTimeSave timeSave = timeSaveComponentMapper.get(entityId);

        timeSave.addSavePoint(position, velocity);

        if(timeProgressBar!= null){
            timeProgressBar.setRange(0,1);
            timeProgressBar.setValue(timeSave.getProgress());
        }
    }
}
