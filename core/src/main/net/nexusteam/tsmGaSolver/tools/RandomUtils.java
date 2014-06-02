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

	public static String getRandomName()
	{
		String name ="";
		for(int i = 0; i < MathUtils.random(1, 6); i++)
		{
			name = "" + getRandomLetter();
		}

		if(name.isEmpty())
		{
			name = "A";
		}
		return name;
	}

	public static char getRandomLetter(String alphabet) {
		return alphabet.charAt(MathUtils.random(alphabet.length() - 1));
	}

}
