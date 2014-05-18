package net.nexusteam.tsmGaSolver.tools;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author AndreLopes
 */
public class Random { // TODO why do we need this? Just use MathUtils directly (dermetfan)

	public static float getRandomFloat(float min, float max) {
		return MathUtils.random(min, max);
	}

	public static float getRandomFloat(float range)
	{
		return MathUtils.random(range);
	}

	public static boolean getRandomBoolean()
	{
		return MathUtils.randomBoolean();
	}

	public static float getRandomFloat()
	{
		return MathUtils.random();
	}

}
