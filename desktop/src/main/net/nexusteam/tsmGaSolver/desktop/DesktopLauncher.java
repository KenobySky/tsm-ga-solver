package net.nexusteam.tsmGaSolver.desktop;

import net.dermetfan.utils.ArrayUtils;
import net.nexusteam.tsmGaSolver.views.Settings;
import net.nexusteam.tsmGaSolver.views.TsmGaSolver;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 1024;
		cfg.height = 768;
		cfg.title = "Traveling Salesman Problem - Genetic Algorithm Solver 0.4 Alpha";

		new LwjglApplication(new TsmGaSolver(), cfg);

		if(ArrayUtils.contains(args, "--reset", false)) {
			Settings.reset(true);
			Settings.prefs.flush();
		}
	}

}
