package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.cDead;
import com.ziamor.heavyrunner.screens.GamePlayScreen;

public class sDead extends IteratingSystem {
    @Wire
    Runner runner;
    sRewindTime rewindTime;
    GamePlayScreen gamePlayScreen;

    public sDead(GamePlayScreen gamePlayScreen) {
        super(Aspect.all(cDead.class));
        this.gamePlayScreen = gamePlayScreen;
    }

    @Override
    protected void process(int entityId) {
        gamePlayScreen.gameover = true;
        gamePlayScreen.finalScore = rewindTime.timeSurvived;
        runner.deadSound.play(0.75f);
    }
}
