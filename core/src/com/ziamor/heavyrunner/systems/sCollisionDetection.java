package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.ziamor.heavyrunner.components.*;

public class sCollisionDetection extends BaseEntitySystem {

    ComponentMapper<cAABB> aabbComponentMapper;
    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cOnGround> onGroundComponentMapper;
    ComponentMapper<cGroundCollider> groundColliderComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;

    public sCollisionDetection() {
        super(Aspect.all());
    }

    @Override
    protected void processSystem() {
        int player = world.getSystem(TagManager.class).getEntityId("player");

        cStartRewind startRewind = startRewindComponentMapper.get(player);

        if(startRewind != null){
            // Time is being rewinded, do not check of collisions
            return;
        }
        cAABB playerAABB = aabbComponentMapper.get(player);
        cGroundCollider playerGroundCollider = groundColliderComponentMapper.get(player);
        cPosition playerPos = positionComponentMapper.get(player);
        cVelocity playerVel = velocityComponentMapper.get(player);

        IntBag walls = world.getAspectSubscriptionManager().get(Aspect.all(cWall.class, cAABB.class)).getEntities();
        //Check if player is on the ground

        onGroundComponentMapper.remove(player);
        if (playerGroundCollider != null)
            for (int i = 0; i < walls.size(); i++) {
                cAABB wallAABB = aabbComponentMapper.get(walls.get(i));

                if (playerGroundCollider.aabb.overlaps(wallAABB.aabb)) {
                    Rectangle intersection = getIntersection(playerGroundCollider.aabb, wallAABB.aabb);
                    if (playerAABB.aabb.y > wallAABB.aabb.y) {
                        onGroundComponentMapper.create(player);
                        break;
                    }
                }
            }

        if (playerAABB != null)
            for (int i = 0; i < walls.size(); i++) {
                cAABB wallAABB = aabbComponentMapper.get(walls.get(i));

                if (playerAABB.aabb.overlaps(wallAABB.aabb)) {

                    Rectangle intersection = getIntersection(playerAABB.aabb, wallAABB.aabb);
                    if (intersection.width <= intersection.height) {
                        if (playerAABB.aabb.x > wallAABB.aabb.x)
                            playerPos.x += intersection.width;
                        else
                            playerPos.x -= intersection.width;

                        playerAABB.aabb.x = playerPos.x;
                        playerVel.x = 0;
                    } else {
                        if (playerAABB.aabb.y > wallAABB.aabb.y) {
                            playerPos.y += intersection.height;
                        } else {
                            playerPos.y -= intersection.height;
                        }
                        playerVel.y = 0;
                        playerAABB.aabb.y = playerPos.y;
                    }
                    return;
                }
            }
    }

    protected Rectangle getIntersection(Rectangle r1, Rectangle r2) {
        Rectangle intersection = new Rectangle();
        intersection.x = Math.max(r1.x, r2.x);
        intersection.width = Math.min(r1.x + r1.width, r2.x + r2.width) - intersection.x;
        intersection.y = Math.max(r1.y, r2.y);
        intersection.height = Math.min(r1.y + r1.height, r2.y + r2.height) - intersection.y;
        return intersection;
    }
}
