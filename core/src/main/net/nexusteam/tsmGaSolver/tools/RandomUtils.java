package net.nexusteam.tsmGaSolver.tools;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author AndreLopes
 */
public class RandomUtils {

	public static float getRandomPositiveNegativeFloat() {
		return MathUtils.randomBoolean() ? MathUtils.random() : -MathUtils.random();
	}

	public static char getRandomLetter() {
		return getRandomLetter("ABCDEFGHIJKLMNOPQRSTVWXYZ");
	}

	public static char getRandomLetter(String alphabet) {
		return alphabet.charAt(MathUtils.random(alphabet.length() - 1));
	}

}
