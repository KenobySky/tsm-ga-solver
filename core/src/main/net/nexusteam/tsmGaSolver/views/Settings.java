package net.nexusteam.tsmGaSolver.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import net.dermetfan.utils.libgdx.scene2d.ui.Tooltip;
import net.nexusteam.tsmGaSolver.Assets;

/**
 * @author dermetfan
 */
public class Settings extends Table {

    public static final Preferences prefs = Gdx.app.getPreferences("TSM-GA-Solver");

    public static final String WAYPOINT_QUANTITY = "waypoint quantity", CHROMOSOME_QUANTITY = "chromosome quantity", MUTATION_PERCENTAGE = "mutation percentage", MATING_POPULATION_PERCENTAGE = "mating population percentage", FAVORED_POPULATION_PERCENTAGE = "favored population percentage", CUT_LENGTH = "cut length", MAXIMUM_GENERATIONS = "maximum generations", MINIMUM_NON_CHANGE_GENERATIONS = "minimum non-change generations", STEP_MANUALLY = "step manually", STEP_ITERATIONS = "step iterations", CURRENT_SAMPLE = "current_sample", NEW_SAMPLE_NAME = "new sample name", NEW_BENCHMARK_NAME = "new benchmark name", BENCHMARK_THIS_RUN = "benchmark this run";

    /**
     * @see #isNumeric(char)
     */
    public static final TextFieldFilter numericFilter = new TextFieldFilter() {
        @Override
        public boolean acceptChar(TextField textField, char c) {
            return isNumeric(c);
        }
    };

    static {
        reset(false);
    }

    /**
     * @param c
     * @return if the given char represents a numeric value
     */
    public static boolean isNumeric(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    /**
     * @param str the String to numerize
     * @return {@code "0"} if the given String is empty
     */
    public static String numerize(String str) {
        return str.isEmpty() ? "0" : str;
    }

    /**
     * resets the prefs to the default values
     *
     * @param override if values should be set even though they already exist
     */
    public static void reset(boolean override) {
        put(WAYPOINT_QUANTITY, 10, override);
        put(CHROMOSOME_QUANTITY, 1000, override);
        put(MUTATION_PERCENTAGE, .1f, override);
        put(MATING_POPULATION_PERCENTAGE, .25f, override);
        put(FAVORED_POPULATION_PERCENTAGE, .75f, override);
        put(CUT_LENGTH, 10, override);
        put(MINIMUM_NON_CHANGE_GENERATIONS, 50, override);
        put(STEP_ITERATIONS, 10, override);

        // doesn't have any effect yet
        put(MAXIMUM_GENERATIONS,0, override);

        // gui settings
        put(STEP_MANUALLY, false, override);
    }

    /**
     * @param key the key
     * @param value the value
     * @param override if the value should be put even if it already exists
     */
    public static <T> void put(String key, T value, boolean override) {
        if (!override && prefs.contains(key)) {
            return;
        }
        if (value instanceof String) {
            prefs.putString(key, (String) value);
        } else if (value instanceof Integer) {
            prefs.putInteger(key, (Integer) value);
        } else if (value instanceof Float) {
            prefs.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            prefs.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            prefs.putLong(key, (Long) value);
        }
    }

    /**
     * adds the settings widgets to the given
     * {@link com.badlogic.gdx.scenes.scene2d.ui.Table}
     */
    public Settings() {

        Skin skin = Assets.manager.get(Assets.uiskin);

        // value tooltip
        final Label valueLabel = new Label("", skin, "status");
        final Container<Label> valueContainer = new Container<Label>(valueLabel);
        valueContainer.setBackground(valueLabel.getStyle().background);
        valueContainer.pack();

        Tooltip<Container> valueTooltip = new Tooltip<Container>(valueContainer) {
            {
                showOn(Type.touchDown);
                hideNotOn(Type.touchDown);
            }

            Actor actor;

            @Override
            public boolean handle(Event e) {
                super.handle(e);
                actor = e.getListenerActor();
                if (actor instanceof Slider) {
                    valueLabel.setText(String.valueOf(((Slider) actor).getValue() * 100));
                } else if (actor instanceof TextField) {
                    valueLabel.setText(((TextField) actor).getText());
                }
                return false;
            }

            @Override
            public boolean show(Event e) {
                super.show(e);
                Vector2 tmp = Pools.obtain(Vector2.class);
                actor.localToStageCoordinates(tmp.set(0, 0));
                popup.setPosition(tmp.x + actor.getWidth() + valueLabel.getWidth() / 2 + 5, tmp.y + actor.getHeight() / 2);
                Pools.free(tmp);
                return false;
            }

        };

        // waypoint quantity
        Label waypointQuantityLabel = new Label("Waypoints", skin);
        TextField waypointQuantity = new TextField(prefs.getString(WAYPOINT_QUANTITY), skin);
        waypointQuantity.setTextFieldFilter(numericFilter);
        waypointQuantity.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String num = String.valueOf(Math.max(Integer.parseInt(numerize(textField.getText())), 2));
                prefs.putString(WAYPOINT_QUANTITY, num);
            }
        });

        // chromosome quantity
        Label chromosomeQuantityLabel = new Label("Chromosomes", skin);
        TextField chromosomeQuantity = new TextField(prefs.getString(CHROMOSOME_QUANTITY), skin);
        chromosomeQuantity.setTextFieldFilter(numericFilter);
        chromosomeQuantity.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                prefs.putString(CHROMOSOME_QUANTITY, numerize(textField.getText()));
            }
        });

        // mutation percentage
        Label mutationPercentageLabel = new Label("Mutation Percentage", skin);

        final Slider mutationPercentage = new Slider(0, 1, 0.01f, false, skin);
        mutationPercentage.setAnimateDuration(.1f);
        mutationPercentage.setValue(prefs.getFloat(MUTATION_PERCENTAGE));
        mutationPercentage.addListener(valueTooltip);
        mutationPercentage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putFloat(MUTATION_PERCENTAGE, mutationPercentage.getValue());
            }
        });

        // mating population percentage
        Label matingPopulationPercentageLabel = new Label("Mating Population Percentage", skin);
        final Slider matingPopulationPercentage = new Slider(0, 1, .01f, false, skin);
        matingPopulationPercentage.setAnimateDuration(.1f);
        matingPopulationPercentage.setValue(prefs.getFloat(MATING_POPULATION_PERCENTAGE));
        matingPopulationPercentage.addListener(valueTooltip);

        // favored population percentage
        Label favoredPopulationPercentageLabel = new Label("Favored Population Percentage", skin);
        final Slider favoredPopulationPercentage = new Slider(0, 1, .01f, false, skin);
        favoredPopulationPercentage.setAnimateDuration(.1f);
        favoredPopulationPercentage.setValue(prefs.getFloat(FAVORED_POPULATION_PERCENTAGE));
        favoredPopulationPercentage.addListener(valueTooltip);
        favoredPopulationPercentage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (favoredPopulationPercentage.getValue() < matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize()) {
                    favoredPopulationPercentage.setValue(matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize());
                }
                prefs.putFloat(FAVORED_POPULATION_PERCENTAGE, favoredPopulationPercentage.getValue());
            }
        });
        matingPopulationPercentage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (matingPopulationPercentage.getValue() > favoredPopulationPercentage.getValue() - favoredPopulationPercentage.getStepSize()) {
                    favoredPopulationPercentage.setValue(matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize());
                }
                if (matingPopulationPercentage.getValue() + favoredPopulationPercentage.getStepSize() >= matingPopulationPercentage.getMaxValue()) {
                    matingPopulationPercentage.setValue(matingPopulationPercentage.getMaxValue() - Math.max(favoredPopulationPercentage.getStepSize(), matingPopulationPercentage.getStepSize()) * 1.5f);
                }
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
                prefs.putString(CUT_LENGTH, numerize(textField.getText()));
            }
        });

        // maximum generations
        Label maximumGenerationsLabel = new Label("Maximum Generations", skin);
        TextField maximumGenerations = new TextField(prefs.getString(MAXIMUM_GENERATIONS), skin);
        maximumGenerations.setTextFieldFilter(numericFilter);
        maximumGenerations.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                prefs.putString(MAXIMUM_GENERATIONS, numerize(textField.getText()));
            }
        });

        // minimum non-change generations
        Label minimumNonChangeGenerationsLabel = new Label("Mininum non-change Generations", skin);
        TextField minimumNonChangeGenerations = new TextField(prefs.getString(MINIMUM_NON_CHANGE_GENERATIONS), skin);
        minimumNonChangeGenerations.setTextFieldFilter(numericFilter);
        minimumNonChangeGenerations.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                prefs.putString(MINIMUM_NON_CHANGE_GENERATIONS, numerize(textField.getText()));
            }
        });

        // step manually
        final CheckBox stepManually = new CheckBox(" step manually", skin);
        stepManually.setChecked(prefs.getBoolean(STEP_MANUALLY));
        stepManually.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putBoolean(STEP_MANUALLY, stepManually.isChecked());
            }
        });

        TextField stepIterations = new TextField(prefs.getString(STEP_ITERATIONS), skin);
        stepIterations.setMessageText("Generations per step");
        stepIterations.setTextFieldFilter(numericFilter);
        stepIterations.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                int iterations = Integer.parseInt(numerize(textField.getText()));
                prefs.putInteger(STEP_ITERATIONS, Math.max(1, iterations));
            }
        });

        add(waypointQuantityLabel).fill();
        add(waypointQuantity).fill().row();
        add(chromosomeQuantityLabel).fill();
        add(chromosomeQuantity).fill().row();
        add(mutationPercentageLabel).fill();
        add(mutationPercentage).fill().row();
        add(matingPopulationPercentageLabel).fill();
        add(matingPopulationPercentage).fill().row();
        add(favoredPopulationPercentageLabel).fill();
        add(favoredPopulationPercentage).fill().row();
        add(cutLengthLabel).fill();
        add(cutLength).fill().row();
        add(maximumGenerationsLabel).fill();
        add(maximumGenerations).fill().row();
        add(minimumNonChangeGenerationsLabel).fill();
        add(minimumNonChangeGenerations).fill().row();
        add(stepManually).fill();
        add(stepIterations).fill();
    }

}
