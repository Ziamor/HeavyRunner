package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.heavyrunner.components.cPosition;
import com.ziamor.heavyrunner.components.cTexture;

public class RenderSystem extends IteratingSystem {
    @Wire
    SpriteBatch batch;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;

    public RenderSystem() {
        super(Aspect.all(cPosition.class, cTexture.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition pos = positionComponentMapper.get(entityId);
        cTexture tex = textureComponentMapper.get(entityId);
        batch.draw(tex.texture, pos.x, pos.y);
    }

    @Override
    protected void begin() {
        super.begin();
        batch.begin();
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }
}
