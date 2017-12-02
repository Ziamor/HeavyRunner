package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.heavyrunner.components.*;

public class sGroundColliderUpdate extends IteratingSystem {
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cSize> sizeComponentMapper;
    ComponentMapper<cGroundCollider> groundColliderComponentMapper;

    public sGroundColliderUpdate() {
        super(Aspect.all(cPosition.class, cGroundCollider.class, cSize.class));
    }

    @Override
    protected void process(int entityId) {
        cPosition position = positionComponentMapper.get(entityId);
        cSize size = sizeComponentMapper.get(entityId);
        cGroundCollider groundCollider = groundColliderComponentMapper.get(entityId);

        groundCollider.aabb.set(position.x, position.y - 1, size.width, 1);
    }
}
