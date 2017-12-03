package com.ziamor.heavyrunner.components;

import com.artemis.Component;
import com.ziamor.heavyrunner.TimeSavePoint;

public class cTimeSave extends Component {
    int index;
    int count;
    TimeSavePoint[] savepoints;

    public void init(int size, cPosition pos, cVelocity vel) {
        index = 0;
        count = 0;
        savepoints = new TimeSavePoint[size];
        for (int i = 0; i < savepoints.length; i++) {
            savepoints[i] = new TimeSavePoint();
            savepoints[i].set(pos.x, pos.y, vel.x, vel.y);
        }
    }

    public void addSavePoint(cPosition pos, cVelocity vel) {
        savepoints[index++].set(pos.x, pos.y, vel.x, vel.y);
        count++;
        if (count > savepoints.length)
            count = savepoints.length;
        index %= savepoints.length;
    }

    public TimeSavePoint rewind() {
        if (count < 0)
            return null;
        count--;
        index--;
        if (index < 0)
            index = savepoints.length - 1;
        TimeSavePoint savePoint = savepoints[index];
        return savePoint;
    }

    public float getProgress() {
        if (count >= savepoints.length)
            return 1f;
        return (float) count / (float) savepoints.length;
    }

    public int size() {
        return savepoints.length;
    }
}
