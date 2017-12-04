package com.ziamor.heavyrunner;

import com.ziamor.heavyrunner.components.cPlayerAnimation;

public class TimeSavePoint {
    public float x, y, vx, vy, timeSurvived;
    public cPlayerAnimation.AnimState animState;

    public void set(float x, float y, float vx, float vy, float timeSurvived, cPlayerAnimation.AnimState animState) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.timeSurvived = timeSurvived;
        this.animState = animState;
    }
}
