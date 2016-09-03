package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	MyGdxGame mainGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		mainGame = new MyGdxGame();
		initialize(mainGame, config);

		MyGdxGame.setPlatformResolver(new AndroidResolver(mainGame, this));
	}
}
