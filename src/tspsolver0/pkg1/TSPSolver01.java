package tspsolver0.pkg1;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 *
 * @based On Jeff Heaton Book ( Studying it )
 * 
 * @author Robin Stu
 * @author Andre V Lopes
 * 
 * 
 */
public class TSPSolver01 {

    public static void main(String[] args) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        config.useGL30 = false;//true?
        config.resizable = true;
        config.title = "TSP GA Solver 0.1a";

        LwjglApplication app = new LwjglApplication(new StartUp(), config);
    }

}
