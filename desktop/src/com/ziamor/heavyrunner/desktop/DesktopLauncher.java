package com.ziamor.heavyrunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ziamor.heavyrunner.Runner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "D_Sync";
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new Runner(), config);
	}
}
