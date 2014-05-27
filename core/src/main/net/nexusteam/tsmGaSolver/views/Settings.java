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

	protected static Preferences prefs = Gdx.app.getPreferences("TSM-GA-Solver");

	protected static final String WAYPOINT_QUANTITY = "waypoint quantity", MAXIMUM_GENERATORS = "maximum generators", MATING_PERCENTAGE = "mating percentage", TOP_POPULATION = "top population", MAXIMUM_NON_CHANGE_GENERATORS = "maximum non-change generators", CUT_LENGTH = "cut length", MUTATION_PERCENTAGE = "mutation percentage", ADD_CITIES_MANUALLY = "add cities manually", CHROMOSOME_QUANTITY = "chromosome quantity";

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
		switch(c) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return true;
		default:
			return false;
		}
	}

	/**	resets the prefs to the default values
	 * 	@param if values should be set even if they already exist */
	public static void reset(boolean override) {
		if(override || !prefs.contains(WAYPOINT_QUANTITY))
			prefs.putInteger(WAYPOINT_QUANTITY, 10);
		if(override || !prefs.contains(MAXIMUM_GENERATORS))
			prefs.putInteger(MAXIMUM_GENERATORS, 10);
		if(override || !prefs.contains(MATING_PERCENTAGE))
			prefs.putFloat(MATING_PERCENTAGE, 80);
		if(override || !prefs.contains(TOP_POPULATION))
			prefs.putInteger(TOP_POPULATION, 25);
		if(override || !prefs.contains(MAXIMUM_NON_CHANGE_GENERATORS))
			prefs.putInteger(MAXIMUM_NON_CHANGE_GENERATORS, 5);
		if(override || !prefs.contains(CUT_LENGTH))
			prefs.putFloat(CUT_LENGTH, 10);
		if(override || !prefs.contains(MUTATION_PERCENTAGE))
			prefs.putFloat(MUTATION_PERCENTAGE, 80);
		if(override || !prefs.contains(ADD_CITIES_MANUALLY))
			prefs.putBoolean(ADD_CITIES_MANUALLY, false);
	}

	/** adds the settings widgets to the given {@link Table} */
	public static void add(Table table) {
		Skin skin = Assets.manager.get(Assets.uiskin);

		// waypoint quantity
		Label waypointQuantity = new Label("Waypoint Quantity", skin);
		final TextField waypointQuantityField = new TextField(prefs.getString(WAYPOINT_QUANTITY), skin);
		waypointQuantityField.setTextFieldFilter(numericFilter);
		waypointQuantityField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(WAYPOINT_QUANTITY, Integer.parseInt(waypointQuantityField.getText()));
			}

		});

		// maximum generators
		Label maximumGenerators = new Label("Maximum Generators", skin);
		final TextField maximumGeneratorsField = new TextField(prefs.getString(MAXIMUM_GENERATORS), skin);
		maximumGeneratorsField.setTextFieldFilter(numericFilter);
		maximumGeneratorsField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(MAXIMUM_GENERATORS, Integer.parseInt(maximumGeneratorsField.getText()));
			}

		});

		// mating percentage
		Label matingPercentage = new Label("Mating Percentage", skin);
		final Slider matingPercentageSlider = new Slider(0, 100, .1f, false, skin);
		matingPercentageSlider.setValue(prefs.getFloat(MATING_PERCENTAGE));
		matingPercentageSlider.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putFloat(MATING_PERCENTAGE, matingPercentageSlider.getValue());
			}

		});

		// top population
		Label topPopulation = new Label("Top Population", skin);
		final Slider topPopulationSlider = new Slider(0, 100, .1f, false, skin);
		topPopulationSlider.setValue(prefs.getFloat(TOP_POPULATION));
		topPopulationSlider.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putFloat(TOP_POPULATION, topPopulationSlider.getValue());
			}

		});

		// maximum non-change generators
		Label maximumNonChangeGenerators = new Label("Maximum Non-Change Generators", skin);
		final TextField maximumNonChangeGeneratorsField = new TextField(prefs.getString(MAXIMUM_NON_CHANGE_GENERATORS), skin);
		maximumNonChangeGeneratorsField.setTextFieldFilter(numericFilter);
		maximumNonChangeGeneratorsField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(MAXIMUM_NON_CHANGE_GENERATORS, Integer.parseInt(maximumNonChangeGeneratorsField.getText()));
			}

		});

		// cut length
		Label cutLength = new Label("Cut Length", skin);
		final TextField cutLengthField = new TextField(prefs.getString(CUT_LENGTH), skin);
		cutLengthField.setTextFieldFilter(numericFilter);
		cutLengthField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putFloat(CUT_LENGTH, Float.parseFloat(cutLengthField.getText()));
			}

		});

		// mutation percentage
		Label mutationPercentage = new Label("Mutation Percentage", skin);
		final Slider mutationPercentageSlider = new Slider(0, 100, .1f, false, skin);
		mutationPercentageSlider.setValue(prefs.getFloat(MUTATION_PERCENTAGE));
		mutationPercentageSlider.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putFloat(MUTATION_PERCENTAGE, mutationPercentageSlider.getValue());
			}

		});

		// add cities manually
		final CheckBox addCitiesManually = new CheckBox(" add cities manually", skin);
		addCitiesManually.setChecked(prefs.getBoolean(ADD_CITIES_MANUALLY));
		addCitiesManually.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putBoolean(ADD_CITIES_MANUALLY, addCitiesManually.isChecked());
			}

		});

		table.defaults().fill();
		table.add(waypointQuantity);
		table.add(waypointQuantityField).row();
		table.add(maximumGenerators);
		table.add(maximumGeneratorsField).row();
		table.add(matingPercentage);
		table.add(matingPercentageSlider).row();
		table.add(topPopulation);
		table.add(topPopulationSlider).row();
		table.add(maximumNonChangeGenerators);
		table.add(maximumNonChangeGeneratorsField).row();
		table.add(cutLength);
		table.add(cutLengthField).row();
		table.add(mutationPercentage);
		table.add(mutationPercentageSlider).row();
		table.add(addCitiesManually).colspan(2);
	}

}
