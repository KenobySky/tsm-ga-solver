package net.nexusteam.tsmGaSolver.ann;

import com.badlogic.gdx.math.Vector2;

/**	represents a waypoint of the salesman
 * 	@author Andre V Lopes */
public class Waypoint {

	private float x, y;
	private String name;

	public Waypoint(float x, float y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public float distance(Waypoint b) {
		return Vector2.dst(x, y, b.x, b.y);
	}

	public static float distance(Waypoint a, Waypoint b) {
		return a.distance(b);
	}

}
