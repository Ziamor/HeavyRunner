package com.ziamor.heavyrunner;

public class TimeSavePoint {
    public float x, y, vx, vy, timeSurvived;

    public void set(float x, float y, float vx, float vy, float timeSurvived) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.timeSurvived = timeSurvived;
    }
}
