package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		MyGdxGame game = new MyGdxGame();

		// Configure platform dependent code
		DesktopResolver res = new DesktopResolver(game);
		MyGdxGame.setPlatformResolver(res);



		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
