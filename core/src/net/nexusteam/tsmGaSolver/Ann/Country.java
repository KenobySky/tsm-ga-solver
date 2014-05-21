package net.nexusteam.tsmGaSolver.Ann;

import net.dermetfan.utils.math.MathUtils;

/**
 *
 * @author Andre V Lopes
 */
public class Country {

    private float x, y;
    private String name;

    public Country(float x, float y, String name) {
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
        System.out.println("Name Of the City :" + name);
        return name;
    }

    public float distance(Country b) {
        float dx, dy;
        dx = b.getX() - x;
        dy = b.getY() - y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float getDistance(Country a, Country b) {
        float distance = a.distance(b);
        return distance;
    }

    //Tests
    public static void main(String args[]) {
        Country ger = new Country(100, 100, "Germany");
        Country bra = new Country(20, 20, "Brasil");
        float distance = bra.distance(ger);
        System.out.println("Distance :" + distance);

    }
}
