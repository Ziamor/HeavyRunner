package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.ziamor.heavyrunner.TimeSavePoint;
import com.ziamor.heavyrunner.components.*;

public class sRewindTime extends BaseEntitySystem {
    @EntityId
    int player = -1;

    ComponentMapper<cStartRewind> startRewindComponentMapper;
    ComponentMapper<cTimeSave> timeSaveComponentMapper;
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;

    ProgressBar timeProgressBar;
    public sRewindTime(ProgressBar timeProgressBar) {
        super(Aspect.all());
        this.timeProgressBar = timeProgressBar;
    }

    @Override
    protected void processSystem() {
        if (player == -1)
            player = world.getSystem(TagManager.class).getEntityId("player");

        // If still -1 then player does not exist
        if (player == -1)
            return;

        cStartRewind startRewind = startRewindComponentMapper.get(player);
        cTimeSave timeSave = timeSaveComponentMapper.get(player);
        cPosition position = positionComponentMapper.get(player);
        cVelocity velocity = velocityComponentMapper.get(player);

        if (startRewind != null && timeSave != null) {
            startRewind.numFrames -= 1;
            if (startRewind.numFrames <= 0)
                startRewindComponentMapper.remove(player);
            else {
                TimeSavePoint savePoint = timeSave.rewind();
                if(savePoint == null){
                    startRewindComponentMapper.remove(player);
                    return;
                }
                position.x = savePoint.x;
                position.y = savePoint.y;
                velocity.x = savePoint.vx;
                velocity.y = savePoint.vy;
                Gdx.app.log("","Rewinding left: " + startRewind.numFrames + " " + savePoint.x + " " + position.x);
            }

            if(timeProgressBar!= null){
                timeProgressBar.setRange(0,1);
                timeProgressBar.setValue(timeSave.getProgress());
            }
        }
    }
}
