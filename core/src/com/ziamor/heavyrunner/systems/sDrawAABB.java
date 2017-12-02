package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziamor.heavyrunner.components.cAABB;

public class sDrawAABB extends IteratingSystem {
    @Wire
    ShapeRenderer shape;

    ComponentMapper<cAABB> aabbComponentMapper;

    public sDrawAABB() {
        super(Aspect.all(cAABB.class));
    }

    @Override
    protected void process(int entityId) {
        cAABB aabb = aabbComponentMapper.get(entityId);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);
        shape.rect(aabb.aabb.x, aabb.aabb.y, aabb.aabb.width, aabb.aabb.height);
        shape.end();
    }
}
