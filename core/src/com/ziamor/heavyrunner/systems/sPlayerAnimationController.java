package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziamor.heavyrunner.components.*;

public class sPlayerAnimationController extends IteratingSystem {
    @Wire
    AssetManager assetManager;

    ComponentMapper<cPlayerAnimation> playerAnimationComponentMapper;
    ComponentMapper<cTextureRegion> textureRegionComponentMapper;
    ComponentMapper<cOnGround> onGroundComponentMapper;

    boolean loaded;

    Animation<TextureRegion> walkAnim, jumpAnim;

    float time = 0;

    public sPlayerAnimationController() {
        super(Aspect.all(cPlayerAnimation.class, cTextureRegion.class));
        loaded = false;
    }

    public void init() {
        loaded = true;

        Texture sheet = assetManager.get("jump_BlueBoy.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 3, sheet.getHeight() / 1);

        walkAnim = new Animation<TextureRegion>(0.15f, new TextureRegion[]{tmp[0][0], tmp[0][1], tmp[0][2]});
        jumpAnim = new Animation<TextureRegion>(0.15f, new TextureRegion[]{tmp[0][1]});
    }

    @Override
    protected void begin() {
        if (!loaded) {
            init();
        }
    }

    @Override
    protected void process(int entityId) {
        //cPlayerAnimation playerAnimation = playerAnimationComponentMapper.get(entityId);
        cTextureRegion textureRegion = textureRegionComponentMapper.get(entityId);
        cOnGround onGround = onGroundComponentMapper.get((entityId));

        time += world.getDelta();
        if (onGround != null)
            textureRegion.textureRegion = walkAnim.getKeyFrame(time, true);
        else
            textureRegion.textureRegion = jumpAnim.getKeyFrame(time, true);
    }
}
