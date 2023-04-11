package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.screens.Instance;

public abstract class Jumper {
    protected AnimationController animationController;

    protected enum State
    {
        Idle,
        Moving,
        Charging,
        Jumping,
        Falling
    }

    protected State state;
    protected State lastState;

    protected final float moveSpeed = 5f;
    protected final float jumpStrengthMax = 1.5f;
    protected float jumpStrength = 0f;
    protected float visualDirection;

    protected Body body;
    protected Instance instance;

    protected  boolean charging;
    protected  boolean jumping;
    protected float horizontal;

    public Jumper(Body body, Instance instance)
    {
        animationController = new AnimationController();
        this.body = body;

        this.body.setUserData(this);
        this.body.setFixedRotation(true);

        // SET CONTACT FRICTION

        this.instance = instance;
    }

    public abstract void performActions(float dt);

    public void dispose()
    {
    animationController.dispose();
    animationController = null;

    instance.getWorld().destroyBody(body);
    body = null;

    instance = null;
    }

    public AnimationController getAnimationController()
    {
        return animationController;
    }

    public Body getBody()
    {
        return body;
    }

    public boolean getJumping()
    {
        return jumping;
    }

    public void setJumping(boolean jumping)
    {
        this.jumping = jumping;
    }

    public void setHorizontal(int horizontal)
    {
        this.horizontal = horizontal;
    }

    public boolean getCharging()
    {
        return charging;
    }

    public void setCharging(boolean charging)
    {
        this.charging = charging;
    }
}
