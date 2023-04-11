package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationController {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;

    public void update(float dt) {
        currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            ++frame;
            currentFrameTime = 0.0F;
        }

        if (frame >= frameCount) {
            frame = 0;
        }

    }

    public TextureRegion getFrame() {
        return (TextureRegion)frames.get(frame);
    }

    public void setNewTexture(Texture AnimationTexture, int frameCount, float cycleTime) {
        TextureRegion region = new TextureRegion(AnimationTexture);
        if (frames != null) {
            frames.clear();
            frames = null;
        }

        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / frameCount;

        for(int i = 0; i < frameCount; ++i) {
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }

        this.frameCount = frameCount;
        maxFrameTime = cycleTime / (float)frameCount;
        frame = 0;
    }

    public void dispose()
    {
        if(frames!=null)
        {
            frames.clear();
            frames = null;
        }
    }
}
