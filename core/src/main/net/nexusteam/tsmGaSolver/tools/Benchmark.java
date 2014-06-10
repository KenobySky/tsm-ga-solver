
package net.nexusteam.tsmGaSolver.tools;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Benchmark {

	private String dir = "/benchmarks/";

	private String sampleName = "";
	private float completedIterations;
	private String benchmarkFileName = "";
	private float cost = 0;
	float wayPoints_Quantity;
	float chromosomes_Quantity;
	float mutation_Percentage;
	float mating_Population_Percentage;
	float favored_Populating_Percentage;
	float cut_Length;
	float minimum_Non_Change_Generations;

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

		benchmarkFileName = recordBenchmarkName(sampleName);

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
			saveBenchmark();
		} else {
			System.out.println("Updated benchmark!");
		}

	}

	private void saveBenchmark () {

		FileHandle localBenchmarkFile = Gdx.files.local(benchmarkFileName);
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
			localBenchmarkFile.writeString("End Of Benchmark File!", true);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String recordBenchmarkName (String samplename) {

		String benchmarkFileName = samplename + "_benchmark";

		FileHandle internal = Gdx.files.internal(dir + benchmarkFileName);

		boolean fileExists = internal.exists();
		if (!fileExists) {
			return benchmarkFileName;
		}

		// Calculate new name
		// Aux Variables
		int counter = 0;
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

		return information;

	}
}
