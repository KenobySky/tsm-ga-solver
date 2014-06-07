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

	/** @return a {@link #getRandomName(int, int) random name} with 1 to 6 letters */
	public static String getRandomName() {
		return getRandomName(1, 6);
	}

	public static String getRandomName(int minLetters, int maxLetters) {
		String name = "";
		for(int i = 0; i < MathUtils.random(minLetters, maxLetters); i++)
			name = "" + getRandomLetter();
		if(name.isEmpty())
			name = "A";
		return name;
	}

	public static char getRandomLetter(String alphabet) {
		return alphabet.charAt(MathUtils.random(alphabet.length() - 1));
	}

}
