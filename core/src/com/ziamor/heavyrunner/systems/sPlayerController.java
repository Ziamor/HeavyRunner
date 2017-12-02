package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.ziamor.heavyrunner.components.cOnGround;
import com.ziamor.heavyrunner.components.cPlayerController;
import com.ziamor.heavyrunner.components.cPosition;
import com.ziamor.heavyrunner.components.cVelocity;

public class sPlayerController extends IteratingSystem implements InputProcessor {
    public enum Action {
        RIGHT,
        LEFT,
        NOTHING
    }

    Action curAction;

    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cOnGround> onGroundComponentMapper;

    boolean doJump;

    public sPlayerController() {
        super(Aspect.all(cPlayerController.class, cPosition.class, cVelocity.class));
        curAction = Action.NOTHING;
        doJump = false;
    }

    @Override
    protected void process(int entityId) {
        cVelocity velocity = velocityComponentMapper.get(entityId);
        cOnGround onGround = onGroundComponentMapper.get(entityId);

        switch (curAction) {
            case RIGHT:
                velocity.x = 500;
                break;
            case LEFT:
                velocity.x = -500;
                break;
            case NOTHING:
                velocity.x = 0;
                break;
        }
        if (doJump) {
            if (onGround != null)
                velocity.y += 500;
            else
                Gdx.app.log("","Not on ground");
            doJump = false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
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
