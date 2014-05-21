package net.nexusteam.tsmGaSolver.tools;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author AndreLopes
 */
public class Random { // TODO why do we need this? Just use MathUtils directly (dermetfan)

	public static float getRandomPositiveNegativeFloat() {
		boolean positive = MathUtils.randomBoolean();
		if(positive)
			return MathUtils.random();
		else
			return MathUtils.random() * -1;

	}

	public static char getRandomLetter() {
		String alphabet = "0123456789ABCDE";
		int N = alphabet.length();

		return alphabet.charAt(MathUtils.random(N));

	}

	public static float getRandomFloat(float min, float max) {
		return MathUtils.random(min, max);
	}

	public static float getRandomFloat(float range) {
		return MathUtils.random(range);
	}

	public static boolean getRandomBoolean() {
		return MathUtils.randomBoolean();
	}

	public static float getRandomFloat() {
		return MathUtils.random();
	}

}
