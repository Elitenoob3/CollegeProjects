package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.screens.*;

public class MyGdxGame extends Game {


	LoadingScreen loadingScreen;
	Menu menu;
	Instance gameInstance;
	Options options;
	End end;

	Vector2 position;

	@Override
	public void create () {

		//Load the main menu
		position = new Vector2();
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		//Clear everything from memory
		if(loadingScreen != null)
		loadingScreen.dispose();

		if(menu!=null)
		menu.dispose();

		if(options!=null)
		options.dispose();

		if(gameInstance!=null)
		gameInstance.dispose();

		loadingScreen = null;
		menu = null;
		options = null;
		gameInstance = null;

		position = null;

		super.dispose();
	}

	public void changeScreen(int screen)
	{
		switch (screen)
		{
			case Constants.MENU:
				if(menu == null) menu = new Menu(this);
				setScreen(menu);
				break;
			case Constants.GAME:
				if(gameInstance == null) gameInstance = new Instance(this);
				setScreen(gameInstance);
				break;
			case Constants.CONTINUE:
				if(gameInstance == null) gameInstance = new Instance(this, position);
				setScreen(gameInstance);
				break;
			case Constants.OPTIONS:
				if(gameInstance == null) options = new Options(this);
				setScreen(options);
				break;
			case Constants.WIN_SCREEN:
				if(end == null) end = new End(this);
				setScreen(end);
				break;
		}
	}

	public void clearMenu()
	{
		menu = null;
	}

	public void clearEndScreen()
	{
		end = null;
	}

	public void clearGameInstance()
	{
		gameInstance = null;
	}

	public void clearOptions()
	{
		options = null;
	}
}
