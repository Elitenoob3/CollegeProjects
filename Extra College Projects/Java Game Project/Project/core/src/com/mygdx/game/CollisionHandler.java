package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.Instance;

public class CollisionHandler implements ContactListener {
    Instance parent;

    Preferences prefs = Gdx.app.getPreferences("Prefs");

    //Collision sound from https://www.fesliyanstudios.com/royalty-free-sound-effects-download/slap-163
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("slap.mp3"));

    public CollisionHandler(Instance parent) {
        this.parent = parent;
    }

    public void beginContact(Contact contact)
    {
    short a = contact.getFixtureA().getFilterData().categoryBits;
    short b = contact.getFixtureB().getFilterData().categoryBits;

    Fixture a_Fixture = contact.getFixtureA();
    Fixture b_Fixture = contact.getFixtureB();

    if(a_Fixture!= null && b_Fixture!= null)
        {
            playerWallCollision(a,b,a_Fixture,b_Fixture);
            playerLevelCollision(a,b,a_Fixture,b_Fixture);
            playerEndCollision(a,b,a_Fixture,b_Fixture);
        }
    }

    public void endContact(Contact contact) {
        short a = contact.getFixtureA().getFilterData().categoryBits;
        short b = contact.getFixtureB().getFilterData().categoryBits;

        Fixture a_Fixture = contact.getFixtureA();
        Fixture b_Fixture = contact.getFixtureB();

        if(a_Fixture!= null && b_Fixture!= null)
        {
            playerWallEndCollision(a,b,a_Fixture,b_Fixture);
        }
    }

    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void playerWallCollision(short a, short b, Fixture a_Fixture, Fixture b_Fixture) {

        if (a == Constants.BIT_SCANNER && b == Constants.BIT_WALLS) {
            if(a_Fixture.getBody().getAngularVelocity() == 0)
            {
                Body body = a_Fixture.getBody();

                Jumper jumper =  (Jumper) body.getUserData();
                jumper.setJumping(false);
                body.setLinearVelocity(-body.getLinearVelocity().x,body.getLinearVelocity().y);
            }
        }
        if (b == Constants.BIT_SCANNER && a == Constants.BIT_WALLS) {
            if(b_Fixture.getBody().getAngularVelocity() == 0)
            {
                Body body = b_Fixture.getBody();

                Jumper jumper =  (Jumper) body.getUserData();
                jumper.setJumping(false);
                body.setLinearVelocity(-body.getLinearVelocity().x,body.getLinearVelocity().y);
            }
        }
        if (a == Constants.BIT_PLAYER && b == Constants.BIT_WALLS) {
            if(a_Fixture.getBody().getAngularVelocity() == 0)
            {
                Body body = a_Fixture.getBody();

                Jumper jumper =  (Jumper) body.getUserData();
                body.setLinearVelocity(-body.getLinearVelocity().x/2,body.getLinearVelocity().y);
                if(prefs.getBoolean("soundOn"))
                    sound.play(prefs.getFloat("soundLevel")/2f);
            }
        }
        if (b == Constants.BIT_PLAYER && a == Constants.BIT_WALLS) {
            if(b_Fixture.getBody().getAngularVelocity() == 0)
            {
                Body body = b_Fixture.getBody();

                Jumper jumper =  (Jumper) body.getUserData();
                body.setLinearVelocity(-body.getLinearVelocity().x/2,body.getLinearVelocity().y);
                if(prefs.getBoolean("soundOn"))
                    sound.play(prefs.getFloat("soundLevel")/2f);
            }
        }
    }

    private void playerLevelCollision(short a, short b, Fixture a_Fixture, Fixture b_Fixture) {

        Vector2 aPos = a_Fixture.getBody().getPosition();
        Vector2 bPos = b_Fixture.getBody().getPosition();

        if (a == Constants.BIT_PLAYER && b == Constants.BIT_LEVEL) {
            {
            if(!b_Fixture.testPoint(new Vector2(0,parent.getCposy()+10)))
             {
                 //System.out.println(parent.getCposy());
                 if(aPos.y > parent.getCposy()) parent.setCposy(parent.getCposy()+20);
                 else
                 if(aPos.y < parent.getCposy()) parent.setCposy(parent.getCposy()-20);
             }
            }
        }
        if (b == Constants.BIT_PLAYER && a == Constants.BIT_LEVEL) {
            {
               if(!a_Fixture.testPoint(new Vector2(0,parent.getCposy()+10)))
                {
                    //System.out.println(parent.getCposy());
                    if(bPos.y > parent.getCposy()) parent.setCposy(parent.getCposy()+20);
                    else
                    if(bPos.y < parent.getCposy()) parent.setCposy(parent.getCposy()-20);
                }
            }
        }
    }

    private void playerWallEndCollision(short a, short b, Fixture a_Fixture, Fixture b_Fixture) {

        if (a == Constants.BIT_SCANNER && b == Constants.BIT_WALLS) {
            {
                Jumper jumper =  (Jumper) a_Fixture.getBody().getUserData();
                jumper.setJumping(true);
                jumper.setCharging(false);
            }
        }
        if (b == Constants.BIT_SCANNER && a == Constants.BIT_WALLS) {
            {
                Jumper jumper =  (Jumper) b_Fixture.getBody().getUserData();
                jumper.setJumping(true);
                jumper.setCharging(false);
            }
        }
    }

    private void playerEndCollision(short a, short b, Fixture a_Fixture, Fixture b_Fixture) {

        if (a == Constants.BIT_PLAYER && b == Constants.BIT_END) {
            {
            parent.winCondition = true;
            }
        }

        if (b == Constants.BIT_PLAYER && a == Constants.BIT_END) {
            {
            parent.winCondition = true;
            }
        }
    }

    public void dispose()
    {
        parent = null;
    }
}

