package com.ziamor.heavyrunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ziamor.heavyrunner.Runner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Heavy Runner";
		config.width = 768;
		config.height = 512;
		new LwjglApplication(new Runner(), config);
	}
}
