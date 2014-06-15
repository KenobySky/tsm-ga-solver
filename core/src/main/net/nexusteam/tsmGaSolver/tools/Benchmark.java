
package net.nexusteam.tsmGaSolver.tools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Benchmark {

	private String dir = "../benchmark/";

	private String sampleName = "";
	private float completedIterations;
	private String benchmarkFileName = "";
	private float cost = 0;
	private float wayPoints_Quantity;
	private float chromosomes_Quantity;
	private float mutation_Percentage;
	private float mating_Population_Percentage;
	private float favored_Populating_Percentage;
	private float cut_Length;
	private float minimum_Non_Change_Generations;

	private long start_time;
	private String total_spent_time;

	public Benchmark (String sampleName, float completedIterations, float currentCost, float wayPoints_Quantity,
		float chromosomes_Quantity, float mutation_Percentage, float mating_Population_Percentage,
		float favored_Populating_Percentage, float cut_Length, float minimum_Non_Change_Generations) {

		this.sampleName = sampleName;
		this.completedIterations = completedIterations;
		this.cost = currentCost;
		this.wayPoints_Quantity = wayPoints_Quantity;
		this.chromosomes_Quantity = chromosomes_Quantity;
		this.mutation_Percentage = mutation_Percentage;
		this.mating_Population_Percentage = mating_Population_Percentage;
		this.favored_Populating_Percentage = favored_Populating_Percentage;
		this.cut_Length = cut_Length;
		this.minimum_Non_Change_Generations = minimum_Non_Change_Generations;

		// Calculate Time

		this.start_time = System.nanoTime();

		
		Gdx.files.local(dir).mkdirs();
		

	}

	// Only save when finished iteration
	public void update (boolean saveFile, float currentQuantityIterations, float currentCost, float wayPoints_Quantity,
		float chromosomes_Quantity, float mutation_Percentage, float mating_Population_Percentage,
		float favored_Populating_Percentage, float cut_Length, float minimum_Non_Change_Generations) {

		this.completedIterations = currentQuantityIterations;
		this.cost = currentCost;
		this.wayPoints_Quantity = wayPoints_Quantity;
		this.chromosomes_Quantity = chromosomes_Quantity;
		this.mutation_Percentage = mutation_Percentage;
		this.mating_Population_Percentage = mating_Population_Percentage;
		this.favored_Populating_Percentage = favored_Populating_Percentage;
		this.cut_Length = cut_Length;
		this.minimum_Non_Change_Generations = minimum_Non_Change_Generations;

		if (saveFile) {
			total_spent_time = calculateCostTime(start_time);

			saveBenchmark();
		} else {
			//System.out.println("Updated benchmark!");
		}

	}

	private String calculateCostTime (long start_time) {

		long end_time = System.nanoTime();
		long diff = end_time - start_time;

		// Show Time Spent in Different Formats

		long milliseconds_spent = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.NANOSECONDS);
		long seconds_spent = TimeUnit.SECONDS.convert(diff, TimeUnit.NANOSECONDS);
		long minutes_spent = TimeUnit.MINUTES.convert(diff, TimeUnit.NANOSECONDS);
		long hours_spent = TimeUnit.HOURS.convert(diff, TimeUnit.NANOSECONDS);
		long days_spent = TimeUnit.DAYS.convert(diff, TimeUnit.NANOSECONDS);

		total_spent_time += "Total Spent Time in Milliseconds : " + milliseconds_spent;
		total_spent_time += "\n";
		total_spent_time += "Total Spent Time in Seconds : " + seconds_spent;
		total_spent_time += "\n";
		total_spent_time += "Total Spent Time in Minutes : " + minutes_spent;
		total_spent_time += "\n";
		total_spent_time += "Total Spent Time in Hours : " + hours_spent;
		total_spent_time += "\n";
		total_spent_time += "Total Spent Time in Days : " + days_spent;
		total_spent_time += "\n";

		System.out.println("Total Spent Time : " + total_spent_time);

		// ROBIN HELP TODO

		long milliseconds = 0;
		long seconds = 0;
		long minutes = 0;
		long hours = 0;

		total_spent_time += "Calculated Time Spent : " + hours + "h " + minutes + "m " + seconds + "s " + milliseconds + "ms";

		return total_spent_time;
	}

	private void saveBenchmark () {

		benchmarkFileName = recordBenchmarkName(sampleName);
		FileHandle localBenchmarkFile = Gdx.files.local(dir + benchmarkFileName);
		
		try {
			localBenchmarkFile.file().createNewFile();

			localBenchmarkFile.writeString("Sample Name : " + sampleName, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Solution Found after #Completed Iterations : " + completedIterations, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Total Cost Calculated : " + cost, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Way Points Quantity : " + wayPoints_Quantity, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Chromosomes Quantity : " + chromosomes_Quantity, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Mutation Percentage : " + mutation_Percentage, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Mating Population Percentage : " + mating_Population_Percentage, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Favored Population Percentage : " + favored_Populating_Percentage, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Cut Length : " + cut_Length, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Minimum Non Change Generations : " + minimum_Non_Change_Generations, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("Total spent Time : " + total_spent_time, true);
			localBenchmarkFile.writeString("\n", true);
			localBenchmarkFile.writeString("End Of Benchmark File!", true);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String recordBenchmarkName (String samplename) {

		String benchmarkFileName = samplename + "_TSMGABenchmark";

		FileHandle internal = Gdx.files.local(dir + benchmarkFileName);

		boolean fileExists = internal.exists();
		if (!fileExists) {
			return benchmarkFileName;
		}

		// Calculate new name
		// Aux Variables
		int counter = 1;
		String newBenchmarkFilename;

		while (fileExists) {
			newBenchmarkFilename = benchmarkFileName + "_" + counter;
			fileExists = Gdx.files.internal(dir + newBenchmarkFilename).exists();

			if (!fileExists) {
				benchmarkFileName = newBenchmarkFilename;
				break;
			} else {
				counter++;
			}

		}

		return benchmarkFileName;

	}

	// André TODO
	@Override
	public String toString () {
		String information = "";

		information += "Sample Name : " + sampleName;
		information += "\n";

		information += "Current Completed Iterations : " + completedIterations;
		information += "\n";

		information += "Total Cost Calculated : " + cost;
		information += "\n";

		information += "Way Points Quantity : " + wayPoints_Quantity;
		information += "\n";

		information += "Chromosomes Quantity : " + chromosomes_Quantity;
		information += "\n";

		information += "Mutation Percentage : " + mutation_Percentage;
		information += "\n";

		information += "Mating Population Percentage : " + mating_Population_Percentage;
		information += "\n";

		information += "Favored Population Percentage : " + favored_Populating_Percentage;
		information += "\n";

		information += "Cut Length : " + cut_Length;
		information += "\n";

		information += "Minimum Non Change Generations : " + minimum_Non_Change_Generations;
		information += "\n";
		information += "Total Time Spent " + total_spent_time;
		return information;

	}
}
