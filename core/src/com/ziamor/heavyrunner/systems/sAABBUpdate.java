package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sAABBUpdate extends IteratingSystem {
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cSize> sizeComponentMapper;
    ComponentMapper<cAABB> aabbComponentMapper;

    public sAABBUpdate() {
        super(Aspect.all(cPosition.class, cSize.class, cAABB.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cSize size = sizeComponentMapper.get(entityId);
        cAABB aabb = aabbComponentMapper.get(entityId);

        aabb.aabb.set(position.x, position.y, size.width, size.height);
    }
}
