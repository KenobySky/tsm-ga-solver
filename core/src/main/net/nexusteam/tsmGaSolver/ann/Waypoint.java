package net.nexusteam.tsmGaSolver.ann;

import com.badlogic.gdx.math.Vector2;

/**	represents a waypoint of the salesman
 * 	@author Andre V Lopes */
@SuppressWarnings("serial")
public class Waypoint extends Vector2 {

	private final String name;

	public Waypoint(String name) {
		this.name = name;
	}

	public Waypoint(float x, float y, String name) {
		super(x, y);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
