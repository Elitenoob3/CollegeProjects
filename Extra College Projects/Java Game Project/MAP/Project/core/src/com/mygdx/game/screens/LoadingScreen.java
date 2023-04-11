package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

public class LoadingScreen implements Screen {

    //Parent reference
    MyGdxGame parent;

    public LoadingScreen(MyGdxGame parent)
    {
        this.parent = parent;
    }

    @Override
    public void show() {
        parent.changeScreen(Constants.MENU);
        dispose();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        parent = null;
    }
}
