package net.nexusteam.tsmGaSolver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.nexusteam.tsmGaSolver.TsmGaSolver;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 1280;
		cfg.height = 600;
		cfg.title = "TSP GA Solver 0.1a";

		new LwjglApplication(new TsmGaSolver(), cfg);
	}
}
