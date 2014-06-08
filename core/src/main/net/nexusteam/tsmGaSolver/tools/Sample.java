package net.nexusteam.tsmGaSolver.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/** Represents a Sample
 *  @author Andre Vinícius Lopes
 *  @author dermetfan */
public class Sample {

	public static final FileHandle SAMPLE_DIR = Gdx.files.isLocalStorageAvailable() ? Gdx.files.local("samples") : Gdx.files.external("TSM-GA-Solver").child("samples");
	public static final String FILE_EXTENSION = "tsmgass";
	public Array<? extends Vector2> waypoints;

	public Sample() {}

	public Sample(Array<? extends Vector2> waypoints) {
		this.waypoints = waypoints;
	}

	public void save(String name) {
		if(waypoints == null || waypoints != null && waypoints.size < 2)
			throw new IllegalStateException("The waypoints must not be null and have at least 2 entries: " + (waypoints == null ? "null" : waypoints.size));
		new Json().toJson(this, SAMPLE_DIR.child(name + "." + FILE_EXTENSION));
	}

	public static Sample load(String name) {
		return new Json().fromJson(Sample.class, SAMPLE_DIR.child(name + "." + FILE_EXTENSION));
	}

}
