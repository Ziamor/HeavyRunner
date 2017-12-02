package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.ziamor.heavyrunner.components.cAABB;
import com.ziamor.heavyrunner.components.cPosition;
import com.ziamor.heavyrunner.components.cVelocity;
import com.ziamor.heavyrunner.components.cWall;

public class sCollisionDetection extends BaseEntitySystem {

    ComponentMapper<cAABB> aabbComponentMapper;
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;

    public sCollisionDetection() {
        super(Aspect.all());
    }

    @Override
    protected void processSystem() {
        int player = world.getSystem(TagManager.class).getEntityId("player");

        cAABB playerAABB = aabbComponentMapper.get(player);
        cPosition playerPos = positionComponentMapper.get(player);
        cVelocity playerVel = velocityComponentMapper.get(player);

        if (playerAABB == null)
            return;

        IntBag walls = world.getAspectSubscriptionManager().get(Aspect.all(cWall.class, cAABB.class)).getEntities();
        for (int i = 0; i < walls.size(); i++) {
            cAABB wallAABB = aabbComponentMapper.get(walls.get(i));

            if (playerAABB.aabb.overlaps(wallAABB.aabb)) {
                Rectangle intersection = new Rectangle();
                intersection.x = Math.max(playerAABB.aabb.x, wallAABB.aabb.x);
                intersection.width = Math.min(playerAABB.aabb.x + playerAABB.aabb.width, wallAABB.aabb.x + wallAABB.aabb.width) - intersection.x;
                intersection.y = Math.max(playerAABB.aabb.y, wallAABB.aabb.y);
                intersection.height = Math.min(playerAABB.aabb.y + playerAABB.aabb.height, wallAABB.aabb.y + wallAABB.aabb.height) - intersection.y;

                if (intersection.width <= intersection.height) {
                    if (playerAABB.aabb.x > wallAABB.aabb.x)
                        playerPos.x += intersection.width;
                    else
                        playerPos.x -= intersection.width;

                    playerAABB.aabb.x = playerPos.x;
                    playerVel.x = 0;
                } else {
                    if (playerAABB.aabb.y > wallAABB.aabb.y)
                        playerPos.y += intersection.height;
                    else
                        playerPos.y -= intersection.height;
                    playerVel.y = 0;
                    playerAABB.aabb.y = playerPos.y;
                }
                return;
            }
        }
    }
}
