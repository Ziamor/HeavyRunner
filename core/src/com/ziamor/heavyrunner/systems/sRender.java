package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.heavyrunner.components.cPosition;
import com.ziamor.heavyrunner.components.cTexture;
import com.ziamor.heavyrunner.components.cTextureRegion;

import java.util.Comparator;

public class sRender extends SortedIteratingSystem {
    @Wire
    SpriteBatch batch;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cTextureRegion> textureRegionComponentMapper;

    public sRender() {
        super(Aspect.all(cPosition.class).one(cTextureRegion.class, cTexture.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition pos = positionComponentMapper.get(entityId);
        cTexture tex = textureComponentMapper.get(entityId);
        cTextureRegion textureRegion = textureRegionComponentMapper.get(entityId);

        if (tex != null)
            batch.draw(tex.texture, pos.x, pos.y);
        else if (textureRegion != null)
            batch.draw(textureRegion.textureRegion, pos.x, pos.y);
    }

    @Override
    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                cPosition e1P = positionComponentMapper.get(e1);
                cPosition e2P = positionComponentMapper.get(e2);
                if (e1P == null)
                    return -1;
                if (e2P == null)
                    return 1;
                return (int) Math.signum(e1P.z - e2P.z);
            }
        };
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
