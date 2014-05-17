package br.Tools;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author AndreLopes
 */
public class Random {

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
