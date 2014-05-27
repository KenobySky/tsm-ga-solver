package net.nexusteam.tsmGaSolver.desktop;

import net.dermetfan.utils.ArrayUtils;
import net.nexusteam.tsmGaSolver.views.Settings;
import net.nexusteam.tsmGaSolver.views.TsmGaSolver;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 800;
		cfg.height = 600;
		cfg.title = "TSP GA Solver 0.3 Alpha";

		new LwjglApplication(new TsmGaSolver(), cfg);

		if(ArrayUtils.contains(args, "--reset", false))
			Settings.reset(true);
	}
}
