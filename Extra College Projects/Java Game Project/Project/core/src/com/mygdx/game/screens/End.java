package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

public class End implements Screen {

    //Parent reference
    MyGdxGame parent;

        Stage stage;
    Table table;
    Skin skin;

    TextButton back;
    TextButton exit;

    public End(MyGdxGame parent)
    {
        //Get parent
        this.parent = parent;

        //Initialize table and stage
        stage = new Stage(new ScreenViewport());
        table = new Table();
        skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));

        //Initialize buttons
        back = new TextButton("Back", skin);
        exit = new TextButton("Exit", skin);

        //Helps with filling up the screen surface
        table.setFillParent(true);

        //Debug view for buttons
        //table.setDebug(true);

        //Add buttons to table
        table.add(new Label("You win!", this.skin));
        table.add(back).fillX().uniformX();
        table.row().pad(20.0F, 0.0F, 0.0F, 0.0F).uniformX();
        table.add(exit).fillX().uniformX();

        //Add table to screen
        stage.addActor(table);
    }

    @Override
    public void show() {
        //Set the input to this screen
        Gdx.input.setInputProcessor(stage);


        //Back button interaction
        back.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Constants.MENU);
                parent.clearEndScreen();
                dispose();
            }
        });

        //Exit button interaction
        exit.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void render(float delta) {
        //Clear old frame
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(16384);

        //Input the screen UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 0.033333335F));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
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
    //Dispose library resources properly
    parent = null;

    table.clear();
    stage.dispose();
    skin.dispose();

    //Clear everything from memory
    table = null;

    back = null;
    exit = null;
    }
}
