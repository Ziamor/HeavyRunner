package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziamor.heavyrunner.components.cAABB;
import com.ziamor.heavyrunner.components.cGroundCollider;

public class sDrawAABB extends IteratingSystem {
    @Wire
    ShapeRenderer shape;

    ComponentMapper<cAABB> aabbComponentMapper;
    ComponentMapper<cGroundCollider> groundColliderComponentMapper;

    public sDrawAABB() {
        super(Aspect.one(cAABB.class, cGroundCollider.class));
    }

    @Override
    protected void process(int entityId) {
        cAABB aabb = aabbComponentMapper.get(entityId);
        cGroundCollider groundCollider = groundColliderComponentMapper.get(entityId);
        if(aabb!= null) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.RED);
            shape.rect(aabb.aabb.x, aabb.aabb.y, aabb.aabb.width, aabb.aabb.height);
            shape.end();
        }

        if(groundCollider!= null) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.GREEN);
            shape.rect(groundCollider.aabb.x, groundCollider.aabb.y, groundCollider.aabb.width, groundCollider.aabb.height);
            shape.end();
        }
    }
}
