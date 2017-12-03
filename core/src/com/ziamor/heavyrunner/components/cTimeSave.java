package com.ziamor.heavyrunner.components;

import com.artemis.Component;
import com.ziamor.heavyrunner.TimeSavePoint;

public class cTimeSave extends Component {
    int index;
    TimeSavePoint[] savepoints;

    public void init(int size, cPosition pos, cVelocity vel) {
        index = 0;
        savepoints = new TimeSavePoint[size];
        for (int i = 0; i < savepoints.length; i++) {
            savepoints[i] = new TimeSavePoint();
            savepoints[i].set(pos.x, pos.y, vel.x, vel.y);
        }
    }

    public void addSavePoint(cPosition pos, cVelocity vel) {
        savepoints[index++].set(pos.x, pos.y, vel.x, vel.y);
        index %= savepoints.length;
    }

    public TimeSavePoint rewind() {
        index--;
        if (index < 0)
            index = savepoints.length-1;
        TimeSavePoint savePoint = savepoints[index];
        return savePoint;
    }
}
