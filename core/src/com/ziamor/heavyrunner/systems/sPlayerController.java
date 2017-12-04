package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.*;

public class sPlayerController extends IteratingSystem implements InputProcessor {
    public enum Action {
        RIGHT,
        LEFT,
        NOTHING,
        REWIND,
        STOPREWIND
    }

    Action curAction;

    @Wire
    Runner runner;

    ComponentMapper<cAcceleration> accelerationComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cOnGround> onGroundComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;

    boolean doJump;

    public sPlayerController() {
        super(Aspect.all(cPlayerController.class, cPosition.class, cVelocity.class).exclude(cDead.class));
        curAction = Action.NOTHING;
        doJump = false;
    }

    @Override
    protected void process(int entityId) {
        cAcceleration acceleration = accelerationComponentMapper.get(entityId);
        cVelocity velocity = velocityComponentMapper.get(entityId);
        cOnGround onGround = onGroundComponentMapper.get(entityId);
        cStartRewind startRewind = startRewindComponentMapper.get(entityId);

        switch (curAction) {
            case REWIND:
                if (startRewind == null) {
                    startRewindComponentMapper.create(entityId);
                    onGroundComponentMapper.remove(entityId); // Just to be safe, make sure the player cant spam jump at the end to get higher heights
                }
                curAction = Action.NOTHING;
                break;
            case RIGHT:
                if (startRewind == null)
                    acceleration.x = 500;
                break;
            case LEFT:
                if (startRewind == null)
                    acceleration.x = -500;
                break;
            case STOPREWIND:
                if (startRewind != null)
                    startRewindComponentMapper.remove(entityId);
                curAction = Action.NOTHING;
            case NOTHING:
                acceleration.x = 0;
                break;
        }
        if (doJump) {
            if (onGround != null) {
                velocity.y += 500;
                runner.jumpSound.play(0.75f);
            }
            doJump = false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.Q:
                curAction = Action.REWIND;
                return true;
            case Input.Keys.D:
                curAction = Action.RIGHT;
                return true;
            case Input.Keys.A:
                curAction = Action.LEFT;
                return true;
            case Input.Keys.W:
            case Input.Keys.SPACE:
                doJump = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.Q:
                curAction = Action.STOPREWIND;
                break;
            case Input.Keys.D:
                if (curAction == Action.RIGHT)
                    curAction = Action.NOTHING;
                break;
            case Input.Keys.A:
                if (curAction == Action.LEFT)
                    curAction = Action.NOTHING;
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
