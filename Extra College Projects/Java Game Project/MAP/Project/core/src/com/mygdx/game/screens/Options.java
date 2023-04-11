package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

import javax.swing.*;

public class Options implements Screen {

    Preferences prefs = Gdx.app.getPreferences("Prefs");

    //Parent reference
    MyGdxGame parent;

    Stage stage;

    Table table;
    Skin skin;

    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;

    public Options(MyGdxGame _parent)
    {
        parent = _parent;

        skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table();

        table.setFillParent(true);

        final Slider volumeMusicSlider = new Slider(0.0F, 1.0F, 0.1F, false, this.skin);
        final Slider volumeSoundSlider = new Slider(0.0F, 1.0F, 0.1F, false, this.skin);
        final CheckBox musicCheckbox = new CheckBox((String)null, this.skin);
        final CheckBox soundCheckbox = new CheckBox((String)null, this.skin);

        volumeMusicLabel = new Label("Music volume", this.skin);
        volumeSoundLabel = new Label("Sounds volume", this.skin);
        musicOnOffLabel = new Label("Music", this.skin);
        soundOnOffLabel = new Label("Sounds", this.skin);

        Label titleLabel = new Label("Options", this.skin);
        TextButton backButton = new TextButton("Back", this.skin);

        table.add(titleLabel);
        table.row();
        table.add(volumeMusicLabel);
        table.add(volumeMusicSlider);
        table.row();
        table.add(musicOnOffLabel);
        table.add(musicCheckbox);
        table.row();
        table.add(volumeSoundLabel);
        table.add(volumeSoundSlider);
        table.row();
        table.add(soundOnOffLabel);
        table.add(soundCheckbox);
        table.row();
        table.add(backButton);

        volumeMusicSlider.setValue(prefs.getFloat("musicLevel")*5);
        volumeMusicSlider.addListener(new EventListener() {
            public boolean handle(Event event) {
                prefs.putFloat("musicLevel",volumeMusicSlider.getValue()/5);
                return false;
            }
        });
        volumeSoundSlider.setValue(prefs.getFloat("soundLevel")*5);
        volumeSoundSlider.addListener(new EventListener() {
            public boolean handle(Event event) {
                prefs.putFloat("soundLevel",volumeSoundSlider.getValue()/5);
                return false;
            }
        });
        musicCheckbox.setChecked(prefs.getBoolean("musicOn"));
        musicCheckbox.addListener(new EventListener() {
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                prefs.putBoolean("musicOn",enabled);
                return false;
            }
        });
        soundCheckbox.setChecked(prefs.getBoolean("soundOn"));
        soundCheckbox.addListener(new EventListener() {
            public boolean handle(Event event) {
                boolean enabled = soundCheckbox.isChecked();
                prefs.putBoolean("soundOn",enabled);
                return false;
            }
        });
        backButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Constants.MENU);
                parent.clearOptions();
                dispose();
            }
        });

        //table.setDebug(true);
        stage.addActor(table);
    }

    @Override
    public void show() {
        //Set the input to this screen
        Gdx.input.setInputProcessor(stage);
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
        stage.getViewport().update(width, height, true);
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
        //Save settings
        {
            prefs.flush();
        }

        //Dispose library resources properly
        parent = null;

        table.clear();
        stage.dispose();
        skin.dispose();

        //Clear everything from memory
        table = null;

        volumeMusicLabel=null;
        volumeSoundLabel=null;
        musicOnOffLabel=null;
        soundOnOffLabel=null;
    }
}
