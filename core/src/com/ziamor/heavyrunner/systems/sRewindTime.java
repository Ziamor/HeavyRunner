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

    ProgressBar timeProgressBar, timeDeSyncProgressBar;

    float deSyncRate = 1f/2f;
    float maxDeSync = 2f;
    public  float curDeSync = 0f;

    sObstacleController obstacleController;

    public sRewindTime(ProgressBar timeProgressBar, ProgressBar timeDeSyncProgressBar) {
        super(Aspect.all());
        this.timeProgressBar = timeProgressBar;
        this.timeDeSyncProgressBar = timeDeSyncProgressBar;
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

        if (timeSave != null) {
            if (startRewind != null) {
                startRewind.numFrames -= 1;
                if (startRewind.numFrames <= 0)
                    stopRewind();
                else {
                    TimeSavePoint savePoint = timeSave.rewind();
                    if (savePoint == null) {
                        stopRewind();
                        return;
                    }
                    position.x = savePoint.x;
                    position.y = savePoint.y;
                    velocity.x = savePoint.vx;
                    velocity.y = savePoint.vy;
                    curDeSync += deSyncRate * 1f/60f;
                    if (curDeSync > maxDeSync)
                        curDeSync = maxDeSync;
                }
            }

            if (timeProgressBar != null) {
                timeProgressBar.setRange(0, 1);
                timeProgressBar.setValue(timeSave.getProgress());
            }

            if (timeDeSyncProgressBar != null) {
                timeDeSyncProgressBar.setRange(0, maxDeSync);
                timeDeSyncProgressBar.setValue(curDeSync);
            }
        }
    }

    protected void stopRewind() {
        startRewindComponentMapper.remove(player);
        obstacleController.speedMul = 1f + curDeSync;
    }
}
