package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

import java.awt.Toolkit;
import java.awt.Dimension;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.x = -1;
		config.y = -1;
		config.width = (int) screenSize.getWidth();
		config.height = (int) screenSize.getHeight();
		config.title = "GDXGame";

        config.addIcon("icon_128.png", Files.FileType.Internal);
        config.addIcon("icon_32.png", Files.FileType.Internal);
        config.addIcon("icon_16.png", Files.FileType.Internal);

		new LwjglApplication(new MyGdxGame(), config);
	}
}
