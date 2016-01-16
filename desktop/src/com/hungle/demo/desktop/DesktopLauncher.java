package com.hungle.demo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hungle.demo.BlockGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720/2;
		config.height = 1280/2;
        new LwjglApplication(new BlockGame(new DesktopControls()), config);
	}
}
