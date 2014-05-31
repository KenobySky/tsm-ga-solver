package net.nexusteam.tsmGaSolver.views;

import net.nexusteam.tsmGaSolver.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/** @author dermetfan */
public abstract class Settings {

	public static final Preferences prefs = Gdx.app.getPreferences("TSM-GA-Solver");

	public static final String WAYPOINT_QUANTITY = "waypoint quantity",
			CHROMOSOME_QUANTITY = "chromosome quantity",
			MUTATION_PERCENTAGE = "mutation percentage",
			MATING_POPULATION_PERCENTAGE = "mating population percentage",
			FAVORED_POPULATION_PERCENTAGE = "favored population percentage",
			CUT_LENGTH = "cut length",
			MAXIMUM_GENERATIONS = "maximum generations",
			MINIMUM_NON_CHANGE_GENERATIONS = "minimum non-change generations",
			MATING_PERCENTAGE = "mating percentage",
			ADD_WAYPOINTS_MANUALLY = "add waypoints manually";

	/** @see #isNumeric(char) */
	public static final TextFieldFilter numericFilter = new TextFieldFilter() {

		@Override
		public boolean acceptChar(TextField textField, char c) {
			return isNumeric(c);
		}

	};

	static {
		reset(false);
	}

	/** @return if the given char represents a numeric value */
	public static boolean isNumeric(char c) {
		return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
	}

	/** @param str the String to numerize
	 *  @return {@code "0"} if the given String is empty */
	public static String numerize(String str) {
		return str.isEmpty() ? "0" : str;
	}

	/**	resets the prefs to the default values
	 * 	@param override if values should be set even though they already exist */
	public static void reset(boolean override) {
		if(override || !prefs.contains(WAYPOINT_QUANTITY))
			prefs.putInteger(WAYPOINT_QUANTITY, 10);

		if(override || !prefs.contains(CHROMOSOME_QUANTITY))
			prefs.putInteger(CHROMOSOME_QUANTITY, 1000);

		if(override || !prefs.contains(MUTATION_PERCENTAGE))
			prefs.putFloat(MUTATION_PERCENTAGE, .1f);

		if(override || !prefs.contains(MATING_POPULATION_PERCENTAGE))
			prefs.putFloat(MATING_POPULATION_PERCENTAGE, .25f);

		if(override || !prefs.contains(FAVORED_POPULATION_PERCENTAGE))
			prefs.putFloat(FAVORED_POPULATION_PERCENTAGE, .75f);

		if(override || !prefs.contains(CUT_LENGTH))
			prefs.putInteger(CUT_LENGTH, 10);

		// doesn't have any effect yet

		if(override || !prefs.contains(MAXIMUM_GENERATIONS))
			prefs.putInteger(MAXIMUM_GENERATIONS, 5000);

		if(override || !prefs.contains(MINIMUM_NON_CHANGE_GENERATIONS))
			prefs.putInteger(MINIMUM_NON_CHANGE_GENERATIONS, 50);

		if(override || !prefs.contains(MATING_PERCENTAGE))
			prefs.putFloat(MATING_PERCENTAGE, .8f);

		// gui settings

		if(override || !prefs.contains(ADD_WAYPOINTS_MANUALLY))
			prefs.putBoolean(ADD_WAYPOINTS_MANUALLY, false);
	}

	/** adds the settings widgets to the given {@link Table} */
	public static void add(Table table) {
		Skin skin = Assets.manager.get(Assets.uiskin);

		// waypoint quantity
		Label waypointQuantityLabel = new Label("Waypoints", skin);
		TextField waypointQuantity = new TextField(prefs.getString(WAYPOINT_QUANTITY), skin);
		waypointQuantity.setTextFieldFilter(numericFilter);
		waypointQuantity.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putString(WAYPOINT_QUANTITY, textField.getText());
			}

		});

		// chromosome quantity
		Label chromosomeQuantityLabel = new Label("Chromosomes", skin);
		TextField chromosomeQuantity = new TextField(prefs.getString(CHROMOSOME_QUANTITY), skin);
		chromosomeQuantity.setTextFieldFilter(numericFilter);
		chromosomeQuantity.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putString(CHROMOSOME_QUANTITY, textField.getText());
			}

		});

		// mutation percentage
		Label mutationPercentageLabel = new Label("Mutation Percentage", skin);
		final Slider mutationPercentage = new Slider(0, 1, .1f, false, skin);
		mutationPercentage.setAnimateDuration(.1f);
		mutationPercentage.setValue(prefs.getFloat(MUTATION_PERCENTAGE));
		mutationPercentage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putFloat(MUTATION_PERCENTAGE, mutationPercentage.getValue());
			}

		});

		// mating population percentage
		Label matingPopulationPercentageLabel = new Label("Mating Population Percentage", skin);
		final Slider matingPopulationPercentage = new Slider(0, 1, .05f, false, skin);
		matingPopulationPercentage.setAnimateDuration(.1f);
		matingPopulationPercentage.setValue(prefs.getFloat(MATING_POPULATION_PERCENTAGE));

		// favored population percentage
		Label favoredPopulationPercentageLabel = new Label("Favored Population Percentage", skin);
		final Slider favoredPopulationPercentage = new Slider(0, 1, .05f, false, skin);
		favoredPopulationPercentage.setAnimateDuration(.1f);
		favoredPopulationPercentage.setValue(prefs.getFloat(FAVORED_POPULATION_PERCENTAGE));
		favoredPopulationPercentage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(favoredPopulationPercentage.getValue() < matingPopulationPercentage.getValue() - favoredPopulationPercentage.getStepSize())
					favoredPopulationPercentage.setValue(matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize());
				prefs.putFloat(FAVORED_POPULATION_PERCENTAGE, favoredPopulationPercentage.getValue());
			}

		});
		matingPopulationPercentage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(matingPopulationPercentage.getValue() > favoredPopulationPercentage.getValue() - favoredPopulationPercentage.getStepSize())
					favoredPopulationPercentage.setValue(matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize());
				prefs.putFloat(MATING_POPULATION_PERCENTAGE, matingPopulationPercentage.getValue());
			}

		});

		// cut length
		Label cutLengthLabel = new Label("Cut Length", skin);
		TextField cutLength = new TextField(prefs.getString(CUT_LENGTH), skin);
		cutLength.setTextFieldFilter(numericFilter);
		cutLength.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putString(CUT_LENGTH, textField.getText());
			}

		});

		// maximum generations
		Label maximumGenerationsLabel = new Label("Maximum Generations", skin);
		TextField maximumGenerations = new TextField(prefs.getString(MAXIMUM_GENERATIONS), skin);
		maximumGenerations.setTextFieldFilter(numericFilter);
		maximumGenerations.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putString(MAXIMUM_GENERATIONS, textField.getText());
			}

		});

		// minimum non-change generations
		Label minimumNonChangeGenerationsLabel = new Label("Mininum non-change Generations", skin);
		TextField minimumNonChangeGenerations = new TextField(prefs.getString(MINIMUM_NON_CHANGE_GENERATIONS), skin);
		minimumNonChangeGenerations.setTextFieldFilter(numericFilter);
		minimumNonChangeGenerations.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putString(MINIMUM_NON_CHANGE_GENERATIONS, textField.getText());
			}

		});

		// mating percentage
		Label matingPercentageLabel = new Label("Mating Percentage", skin);
		final Slider matingPercentage = new Slider(0, 1, .1f, false, skin);
		matingPercentage.setAnimateDuration(.1f);
		matingPercentage.setValue(prefs.getFloat(MATING_PERCENTAGE));
		matingPercentage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putFloat(MATING_PERCENTAGE, matingPercentage.getValue());
			}

		});

		// add waypoints manually
		final CheckBox addWaypointsManually = new CheckBox(" Add Waypoints Manually", skin);
		addWaypointsManually.setChecked(prefs.getBoolean(ADD_WAYPOINTS_MANUALLY));
		addWaypointsManually.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putBoolean(ADD_WAYPOINTS_MANUALLY, addWaypointsManually.isChecked());
			}

		});

		table.add(waypointQuantityLabel).fill();
		table.add(waypointQuantity).fill().row();
		table.add(chromosomeQuantityLabel).fill();
		table.add(chromosomeQuantity).fill().row();
		table.add(mutationPercentageLabel).fill();
		table.add(mutationPercentage).fill().row();
		table.add(matingPopulationPercentageLabel).fill();
		table.add(matingPopulationPercentage).fill().row();
		table.add(favoredPopulationPercentageLabel).fill();
		table.add(favoredPopulationPercentage).fill().row();
		table.add(cutLengthLabel).fill();
		table.add(cutLength).fill().row();
		table.add(maximumGenerationsLabel).fill();
		table.add(maximumGenerations).fill().row();
		table.add(minimumNonChangeGenerationsLabel).fill();
		table.add(minimumNonChangeGenerations).fill().row();
		table.add(matingPercentageLabel).fill();
		table.add(matingPercentage).fill().row();
		table.add(addWaypointsManually).colspan(2).fill();
	}
}
