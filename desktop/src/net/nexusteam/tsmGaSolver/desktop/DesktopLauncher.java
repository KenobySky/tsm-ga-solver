package net.nexusteam.tsmGaSolver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.dermetfan.utils.ArrayUtils;
import net.nexusteam.tsmGaSolver.views.Settings;
import net.nexusteam.tsmGaSolver.TsmGaSolver;

public class DesktopLauncher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 1024;
        cfg.height = 768;
        cfg.title = "Traveling Salesman Problem - Genetic Algorithm Solver 1.2.2 ";

        LwjglApplication lwjglApplication = new LwjglApplication(new TsmGaSolver(), cfg);

        if (ArrayUtils.contains(args, "--reset", false)) {
            Settings.reset(true);
            Settings.prefs.flush();
        }
    }

}
