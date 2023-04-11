package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.screens.Instance;

import java.util.HashMap;

public class Player extends Jumper{

    boolean once = true;
    HashMap<String, Texture> myAnimations;

    Preferences prefs = Gdx.app.getPreferences("Prefs");

    //Jump sound from https://mixkit.co/free-sound-effects/jump/
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("jump_bit.wav"));


    public Player(Body body, Instance instance, HashMap<String, Texture> myAnimations)
    {
    super(body,instance);
    this.myAnimations = myAnimations;

    state = State.Idle;
    lastState = State.Idle;
    }

    @Override
    public void performActions(float dt) {

        if(once)
        {
            prepareTextures();
            once = false;
        }

        lastState = state;
        movement(dt);

         if(lastState!=state) prepareTextures();
    }

    public void dispose()
    {
        myAnimations = null;
        sound.dispose();
        super.dispose();
    }

    private void movement(float dt)
    {
        float AV = body.getAngularVelocity();
        float yVel = body.getLinearVelocity().y;
        float xVel = body.getLinearVelocity().x;

        horizontal = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            horizontal = -1;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            horizontal = 1;

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && AV == 0 && !jumping)
            charging = true; else charging = false;

        if(!charging && !jumping)
        {
            body.setLinearVelocity(new Vector2(horizontal*moveSpeed, yVel));
            if(horizontal!=0) state = State.Moving; else state = State.Idle;
        }

        if(charging && !jumping)
        {
            if(xVel != 0 && yVel == 0) body.setLinearVelocity(new Vector2(0, yVel));
            jumpStrength += dt;
            state = State.Charging;
        }

        if((!charging && jumpStrength!=0) || jumpStrength>= jumpStrengthMax)
        {
            charging = false;
            jumpStrength *= 750;
            body.applyForceToCenter(new Vector2(horizontal * jumpStrength / 1.15f, jumpStrength), true);
            jumpStrength = 0;
            if(prefs.getBoolean("soundOn"))
            sound.play(prefs.getFloat("soundLevel"));
        }

        if(yVel > 0.2f) state = State.Jumping;
        if(yVel < -0.2f) state = State.Falling;

        if(horizontal>0) visualDirection = 1;
        if(horizontal<0) visualDirection = -1;
    }

    private void prepareTextures()
    {
        switch(state) {
            case Idle:
                animationController.setNewTexture(myAnimations.get("playerIdle"),2,1f);
                break;
            case Moving:
                animationController.setNewTexture(myAnimations.get("playerIdle"),2,0.2f);
                break;
            case Charging:
                animationController.setNewTexture(myAnimations.get("playerCharging"),2,0.4f);
                break;
            case Jumping:
                animationController.setNewTexture(myAnimations.get("playerJumping"),4,0.15f);
                break;
            case Falling:
                animationController.setNewTexture(myAnimations.get("playerFalling"),4,0.15f);
                break;
        }
    }

    public void packTextures(float dt)
    {
        animationController.update(dt);
        MyUtility.drawAnimatedSprite(instance.getRenderer(), body, animationController.getFrame(), visualDirection);
    }
}
