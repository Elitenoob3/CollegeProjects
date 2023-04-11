package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;

public class MyUtility {

    //Draw a sprite that you get from the animation class
    public static void drawAnimatedSprite(OrthogonalTiledMapRenderer renderer, Body body, TextureRegion textureRegion, float horizontal) {
        Sprite sprite = new Sprite(textureRegion);
        sprite.setScale(Constants.SCALE);
        sprite.setOrigin(0.0F, 0.0F);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation((float) Math.toDegrees((double) body.getAngle()));

        //Flip if moving left
        if (horizontal < 0.0F) {
            sprite.flip(true, false);
        }

        //Draw sprite
        sprite.draw(renderer.getBatch());
    }

}
