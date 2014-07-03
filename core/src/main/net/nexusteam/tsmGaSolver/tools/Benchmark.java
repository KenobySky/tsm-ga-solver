package net.nexusteam.tsmGaSolver.tools;

import java.time.Instant;
import java.util.Date;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

public class Benchmark {

	public static Benchmark load(String sample, String name) {
		return new Json().fromJson(Benchmark.class, Sample.fileOf(sample).sibling(name + ".json"));
	}

	public static FileHandle fileOf(String sample, String name) {
		return Sample.fileOf(sample).sibling(name + ".json");
	}

	private String sample;
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

	/** constructor for serialization */
	public Benchmark() {}

	public Benchmark(String sample) {
		if(!Sample.exists(sample))
			throw new IllegalArgumentException("Sample " + sample + " does not exist");
		this.sample = sample;
	}

	public FileHandle fileOf(String name) {
		return Sample.SAMPLE_DIR.child(sample).child(name + ".json");
	}

	public boolean exists(String name) {
		return fileOf(name).exists();
	}

	public void start() {
		start_time = TimeUtils.nanoTime();
	}

	public void end() {
		end_time = TimeUtils.nanoTime();
	}

	// TODO discuss when/how this is supposed to be used and if it makes sense to put this here
	//	private String calculateCostTime(long start_time) {
	//		long end_time = System.nanoTime();
	//		long diff = end_time - start_time;
	//
	//		// Show Time Spent in Different Formats
	//
	//		long milliseconds_spent = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.NANOSECONDS);
	//		long seconds_spent = TimeUnit.SECONDS.convert(diff, TimeUnit.NANOSECONDS);
	//		long minutes_spent = TimeUnit.MINUTES.convert(diff, TimeUnit.NANOSECONDS);
	//		long hours_spent = TimeUnit.HOURS.convert(diff, TimeUnit.NANOSECONDS);
	//		long days_spent = TimeUnit.DAYS.convert(diff, TimeUnit.NANOSECONDS);
	//
	//		total_spent_time += "Total Spent Time in Milliseconds : " + milliseconds_spent;
	//		total_spent_time += "\n";
	//		total_spent_time += "Total Spent Time in Seconds : " + seconds_spent;
	//		total_spent_time += "\n";
	//		total_spent_time += "Total Spent Time in Minutes : " + minutes_spent;
	//		total_spent_time += "\n";
	//		total_spent_time += "Total Spent Time in Hours : " + hours_spent;
	//		total_spent_time += "\n";
	//		total_spent_time += "Total Spent Time in Days : " + days_spent;
	//		total_spent_time += "\n";
	//
	//		System.out.println("Total Spent Time : " + total_spent_time);
	//
	//		// TODO ROBIN HELP
	//
	//		long milliseconds = 0;
	//		long seconds = 0;
	//		long minutes = 0;
	//		long hours = 0;
	//
	//		total_spent_time += "Calculated Time Spent : " + hours + "h " + minutes + "m " + seconds + "s " + milliseconds + "ms";
	//
	//		return total_spent_time;
	//	}

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

	public void save(String name) {
		fileOf(name).writeString(new Json().toJson(this), false);
	}

	public String findAvailableName(String sampleName) {
		FileHandle file = fileOf(sampleName);
		int num = 1;
		while(file.exists())
			file = fileOf(sampleName + '-' + String.valueOf(num++));
		return file.name();
	}

	@Override
	public String toString() {
		return "Sample: " + sample + '\n' +
				"Started: " + Date.from(Instant.ofEpochMilli(start_time)) + '\n' +
				"Duration: " + (TimeUtils.nanosToMillis(end_time - start_time) / 1000f) + " sec\n" +
				"Waypoints: " + waypoint_quantity + '\n' +
				"Chromosomes: " + chromosome_quantity + '\n' +
				"Cost: " + cost + '\n' +
				"Cut Length: " + cut_length + '\n' +
				"Iterations: " + iterations + '\n' +
				"Mating Population Percentage: " + mating_population_percentage + '\n' +
				"Favored Population Percentage: " + favored_population_percentage + '\n' +
				"Mutation Percentage: " + mutation_percentage + '\n' +
				"Mutation Quantity: " + mutation_quantity + '\n' +
				"Minimum non-change Generations: " + minimum_non_change_generations;
	}

}
