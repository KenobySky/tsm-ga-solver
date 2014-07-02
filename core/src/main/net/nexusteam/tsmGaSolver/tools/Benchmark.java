package net.nexusteam.tsmGaSolver.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

public class Benchmark {

	public FileHandle fileOf(String name) {
		return Sample.SAMPLE_DIR.child(sample).child(name + ".json");
	}

	public boolean exists(String name) {
		return fileOf(name).exists();
	}

	private String sample;
	private float iterations;
	private float cost;
	private float waypoint_quantity;
	private float chromosome_quantity;
	private float mutation_percentage;
	private float mutation_quantity;
	private float mating_population_percentage;
	private float favored_populating_percentage;
	private float cut_length;
	private float minimum_non_change_generations;

	private long start_time;
	private long end_time;

	/** constructor for serialization */
	public Benchmark() {}

	public Benchmark(String sample) {
		if(!Sample.exists(sample))
			throw new IllegalArgumentException("Sample " + sample + " does not exist");
		this.sample = sample;
	}

	public void start() {
		start_time = TimeUtils.nanoTime();
	}

	public void end() {
		end_time = TimeUtils.nanoTime();
	}

	public void set(float iterations, float cost, float waypoint_quantity, float chromosome_quantity, float mutation_percentage, float mating_population_percentage, float favored_populating_percentage, float cut_length, float minimum_non_change_generations, float mutation_quantity) {
		this.iterations = iterations;
		this.cost = cost;
		this.waypoint_quantity = waypoint_quantity;
		this.chromosome_quantity = chromosome_quantity;
		this.mutation_percentage = mutation_percentage;
		this.mating_population_percentage = mating_population_percentage;
		this.favored_populating_percentage = favored_populating_percentage;
		this.cut_length = cut_length;
		this.minimum_non_change_generations = minimum_non_change_generations;
		this.mutation_quantity = mutation_quantity;
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

	public static Benchmark load(String sample, String name) {
		return new Json().fromJson(Benchmark.class, Sample.fileOf(sample).sibling(name + ".json"));
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
		return new Json().prettyPrint(this);
	}

	// getters and setters

	public float getIterations() {
		return iterations;
	}

	public void setIterations(float iterations) {
		this.iterations = iterations;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getWaypoint_quantity() {
		return waypoint_quantity;
	}

	public void setWaypoint_quantity(float waypoint_quantity) {
		this.waypoint_quantity = waypoint_quantity;
	}

	public float getChromosome_quantity() {
		return chromosome_quantity;
	}

	public void setChromosome_quantity(float chromosome_quantity) {
		this.chromosome_quantity = chromosome_quantity;
	}

	public float getMutation_percentage() {
		return mutation_percentage;
	}

	public void setMutation_percentage(float mutation_percentage) {
		this.mutation_percentage = mutation_percentage;
	}

	public float getMutation_quantity() {
		return mutation_quantity;
	}

	public void setMutation_quantity(float mutation_quantity) {
		this.mutation_quantity = mutation_quantity;
	}

	public float getMating_population_percentage() {
		return mating_population_percentage;
	}

	public void setMating_population_percentage(float mating_population_percentage) {
		this.mating_population_percentage = mating_population_percentage;
	}

	public float getFavored_populating_percentage() {
		return favored_populating_percentage;
	}

	public void setFavored_populating_percentage(float favored_populating_percentage) {
		this.favored_populating_percentage = favored_populating_percentage;
	}

	public float getCut_length() {
		return cut_length;
	}

	public void setCut_length(float cut_length) {
		this.cut_length = cut_length;
	}

	public float getMinimum_non_change_generations() {
		return minimum_non_change_generations;
	}

	public void setMinimum_non_change_generations(float minimum_non_change_generations) {
		this.minimum_non_change_generations = minimum_non_change_generations;
	}

	public long getStart_time() {
		return start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

}
