package net.nexusteam.tsmGaSolver.tools;

import java.util.Date;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

public class Benchmark {

    public static Benchmark load(String sample, String name) {
        return new Json().fromJson(Benchmark.class, fileOf(sample, name));
    }

    public static FileHandle fileOf(String sample, String name) {
        return Sample.fileOf(sample).sibling(name + ".json");
    }

    private int iterations;
    private float cost;
    private int waypoint_quantity;
    private int chromosome_quantity;
    private float mutation_percentage;
    private float mutation_quantity;
    private float mating_population_percentage;
    private float favored_population_percentage;
    private float cut_length;
    private int minimum_non_change_generations;
    private long start_time;
    private long end_time;

    /**
     * constructor for serialization
     */
    public Benchmark() {
    }

    public void start() {
        start_time = TimeUtils.millis();
    }

    public void end() {
        end_time = TimeUtils.millis();
    }

    public void set(int iterations, float cost, int waypoint_quantity, int chromosome_quantity, float mutation_percentage, float mating_population_percentage, float favored_population_percentage, float cut_length, int minimum_non_change_generations, float mutation_quantity) {
        this.iterations = iterations;
        this.cost = cost;
        this.waypoint_quantity = waypoint_quantity;
        this.chromosome_quantity = chromosome_quantity;
        this.mutation_percentage = mutation_percentage;
        this.mating_population_percentage = mating_population_percentage;
        this.favored_population_percentage = favored_population_percentage;
        this.cut_length = cut_length;
        this.minimum_non_change_generations = minimum_non_change_generations;
        this.mutation_quantity = mutation_quantity;
    }

    public void save(String sample, String name) {
        fileOf(sample, name).writeString(new Json().toJson(this), false);
    }

    @Override
    public String toString() {
        return "Started: " + new Date(start_time) + '\n'
                + "Duration: " + ((end_time - start_time) / 1000f) + " sec\n"
                + "Waypoints: " + waypoint_quantity + '\n'
                + "Chromosomes: " + chromosome_quantity + '\n'
                + "Cost: " + cost + '\n'
                + "Cut Length: " + cut_length + '\n'
                + "Iterations: " + iterations + '\n'
                + "Mating Population Percentage: " + mating_population_percentage + '\n'
                + "Favored Population Percentage: " + favored_population_percentage + '\n'
                + "Mutation Percentage: " + mutation_percentage + '\n'
                + "Mutation Quantity: " + mutation_quantity + '\n'
                + "Minimum non-change Generations: " + minimum_non_change_generations;
    }

}
