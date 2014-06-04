package net.nexusteam.tsmGaSolver.tools;

import net.nexusteam.tsmGaSolver.ann.Waypoint;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SampleManager implements java.io.Serializable {

	private final String FILE_EXTENSION = ".tsmSample";
	private Sample sample;

	
	//Should it be Array<Waypoint> or Array<Vector2> ? 
	public void saveSample(String nameOfSample, Array<Waypoint> waypointsArray) {
		 boolean continueProcess = validateSaveProcess(nameOfSample, waypointsArray);
		 if(continueProcess)
		 {
		sample = new Sample();
		sample.addWaypoints(waypointsArray);
		
		
		
		
		//Not a clue of what im doing here.
		//Have to save the waypoints positions so they can be loaded later for tests.
		Json json = new Json();
		
		json.toJson(sample,Sample.class);
		json.writeArrayStart();
		json.writeFields(sample);
		json.writeArrayEnd();
		 }else
		 {
			 System.out.println("Failed to save Sample! Did not pass the validation Process!");
			 System.out.println("Failed to save Sample! Please Check the samplename ,it ,must not contain .tsmSample");
			 System.out.println("Failed to save Sample! The WayPoints Quantity Must be >= 2");
		 }

	}

	public Array<Waypoint> loadSample(String nameFile) {
	
		return null;

	}
	
	
	private boolean validateSaveProcess(String nameOfSample,Array<Waypoint> waypointsArray)
	{
		if(!nameOfSample.contains(FILE_EXTENSION))
		{
			nameOfSample.concat(FILE_EXTENSION);
		}
		
		if(waypointsArray == null)
		{
			return false;
		}
		
		if(waypointsArray.size < 2)
		{
			return false;
		}
		
		
		return true;
		
	}

	
	
	//Represents a Sample
	public class Sample {

		private Array<Waypoint> waypoints;

		public void addWaypoints(Array<Waypoint> waypoints) {
			this.waypoints.addAll(waypoints);
		}

		public Array<Waypoint> getWayPoints()
		{
			if(waypoints!= null)
			{
				return waypoints;
			}else
			{
				System.out.println("Warning : Waypoints are empty! - Retuning null!");
				return null;
			}
		}
		
	}

}
