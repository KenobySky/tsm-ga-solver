package net.nexusteam.tsmGaSolver.views;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import net.dermetfan.gdx.scenes.scene2d.EventMultiplexer;
import net.dermetfan.gdx.scenes.scene2d.Scene2DUtils;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.AddToStageBehavior;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.Behavior;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.Behavior.Adapter;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.FadeBehavior;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.PositionBehavior;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.PositionBehavior.AlignedOffsetPosition;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.PositionBehavior.OffsetPosition;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.PositionBehavior.PointerPosition;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.PositionBehavior.Position;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.Reaction;
import net.dermetfan.gdx.scenes.scene2d.ui.Popup.TooltipBehavior;
import net.nexusteam.tsmGaSolver.Assets;

/**
 * @author dermetfan
 */
public class Settings extends Table {

	public static final Preferences prefs = Gdx.app.getPreferences("TSM-GA-Solver");

	public static final String WAYPOINT_QUANTITY = "waypoint quantity", CHROMOSOME_QUANTITY = "chromosome quantity", MUTATION_PERCENTAGE = "mutation percentage", MATING_POPULATION_PERCENTAGE = "mating population percentage", FAVORED_POPULATION_PERCENTAGE = "favored population percentage", MAXIMUM_GENERATIONS = "maximum generations", MINIMUM_NON_CHANGE_GENERATIONS = "minimum non-change generations", STEP_MANUALLY = "step manually", STEP_ITERATIONS = "step iterations", CURRENT_SAMPLE = "current_sample", NEW_SAMPLE_NAME = "new sample name", NEW_BENCHMARK_NAME = "new benchmark name", BENCHMARK_THIS_RUN = "benchmark this run";

	/**
	 * @see #isNumeric(char)
	 */
	public static final TextFieldFilter numericFilter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return isNumeric(c);
		}
	}, decimalFilter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return numericFilter.acceptChar(textField, c) || c == '.';
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
	 * Resets the prefs to the default values
	 *
	 * @param override if values should be set even though they already exist
	 */
	public static void reset(boolean override) {
		put(WAYPOINT_QUANTITY, 10, override);
		put(CHROMOSOME_QUANTITY, 1000, override);
		put(MUTATION_PERCENTAGE, .1f, override);
		put(MATING_POPULATION_PERCENTAGE, .25f, override);
		put(FAVORED_POPULATION_PERCENTAGE, .75f, override);
		put(MINIMUM_NON_CHANGE_GENERATIONS, 50, override);
		put(STEP_ITERATIONS, 10, override);
		put(MAXIMUM_GENERATIONS, 0, override);

		//GUI settings
		put(STEP_MANUALLY, false, override);

		prefs.flush(); // necessary on android
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
	 * {@link Table}
	 */
	public Settings() {
		Skin skin = Assets.manager.get(Assets.uiskin);

		final EventMultiplexer tooltipHider = new EventMultiplexer();
		Behavior addTooltipHiderBehavior = new Behavior.Adapter() {
			boolean added;
			@Override
			public Reaction handle(Event event, Popup popup) {
				if(!added) {
					event.getStage().addListener(tooltipHider);
					added = true;
				}
				return null;
			}
		};

		//Value ToolTip
		final Label valueLabel = new Label("", skin, "status");
		final Container<Label> valueContainer = new Container<Label>(valueLabel);
		valueContainer.setBackground(valueLabel.getStyle().background);
		valueContainer.pack();

		TooltipBehavior valueTooltipBehavior = new TooltipBehavior();
		valueTooltipBehavior.showOn(Type.touchDown);
		valueTooltipBehavior.showOn(Type.touchDragged);
		valueTooltipBehavior.hideNotOn(Type.touchDown);
		valueTooltipBehavior.hideNotOn(Type.mouseMoved);
		Popup<Container> valueTooltip = new Popup<Container>(valueContainer, valueTooltipBehavior, new AddToStageBehavior(), addTooltipHiderBehavior, new PositionBehavior(new Position() {
			@Override
			public void apply(Event event, Actor popup) {
				Actor target = event.getTarget();
				Vector2 pos = Scene2DUtils.positionInStageCoordinates(target);
				pos.x += target.getWidth();
				popup.setPosition(pos.x, pos.y);
			}
		}), new Adapter() {
			@Override
			public Reaction handle(Event event, Popup popup) {
				Actor actor = event.getListenerActor();
				if (actor instanceof Slider) {
					valueLabel.setText(String.valueOf(((Slider) actor).getValue() * 100));
					if(popup.getPopup() instanceof Layout)
						((Layout) popup.getPopup()).pack();
				}
				return super.handle(event, popup);
			}

			@Override
			public boolean show(Event event, Popup popup) {
				if(popup.getPopup() instanceof Layout)
					((Layout) popup.getPopup()).pack();
				return super.show(event, popup);
			}
		}, new FadeBehavior());
		tooltipHider.add(valueTooltip);

		// waypoint quantity
		Label waypointQuantityLabel = new Label("Waypoints", skin);
		TextField waypointQuantity = new TextField(String.valueOf(prefs.getInteger(WAYPOINT_QUANTITY)), skin);
		waypointQuantity.setTextFieldFilter(numericFilter);
		waypointQuantity.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				Integer num = Math.max(Integer.parseInt(numerize(textField.getText())), 2);
				prefs.putInteger(WAYPOINT_QUANTITY, num);
			}
		});

		// chromosome quantity
		Label chromosomeQuantityLabel = new Label("Chromosomes", skin);
		TextField chromosomeQuantity = new TextField(String.valueOf(prefs.getInteger(CHROMOSOME_QUANTITY)), skin);
		chromosomeQuantity.setTextFieldFilter(numericFilter);
		chromosomeQuantity.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(CHROMOSOME_QUANTITY, Integer.parseInt(numerize(textField.getText())));
			}
		});

		// mutation percentage
		Label mutationPercentageLabel = new Label("Mutation Percentage", skin);
		final Slider mutationPercentage = new Slider(0, 1, .0000001f, false, skin);
		mutationPercentageLabel.addListener(new NewValueListener(mutationPercentage, skin));
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
		final Slider matingPopulationPercentage = new Slider(0, 1, .0000001f, false, skin);
		matingPopulationPercentageLabel.addListener(new NewValueListener(matingPopulationPercentage, skin));
		matingPopulationPercentage.setAnimateDuration(.1f);
		matingPopulationPercentage.setValue(prefs.getFloat(MATING_POPULATION_PERCENTAGE));
		matingPopulationPercentage.addListener(valueTooltip);

		// favored population percentage
		Label favoredPopulationPercentageLabel = new Label("Favored Population Percentage", skin);
		final Slider favoredPopulationPercentage = new Slider(0, 1, .0000001f, false, skin);
		favoredPopulationPercentageLabel.addListener(new NewValueListener(favoredPopulationPercentage, skin));
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

		// maximum generations
		Label maximumGenerationsLabel = new Label("Maximum Generations", skin);
		TextField maximumGenerations = new TextField(String.valueOf(prefs.getInteger(MAXIMUM_GENERATIONS)), skin);
		maximumGenerations.setTextFieldFilter(numericFilter);
		Popup<Label> maximumGenerationsTooltip = new Popup<Label>(new Label(" 0 means no limit", skin, "status"), new TooltipBehavior(), new AddToStageBehavior(), addTooltipHiderBehavior, new PositionBehavior(new PointerPosition(), new AlignedOffsetPosition(Align.topLeft), new OffsetPosition(7, -10)), new FadeBehavior());
		tooltipHider.add(maximumGenerationsTooltip);
		maximumGenerations.addListener(maximumGenerationsTooltip);
		maximumGenerations.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(MAXIMUM_GENERATIONS, Integer.parseInt(numerize(textField.getText())));
			}
		});

		//Minimum non-change generations
		Label minimumNonChangeGenerationsLabel = new Label("Mininum non-change Generations", skin);
		TextField minimumNonChangeGenerations = new TextField(String.valueOf(prefs.getInteger(MINIMUM_NON_CHANGE_GENERATIONS)), skin);
		minimumNonChangeGenerations.setTextFieldFilter(numericFilter);
		minimumNonChangeGenerations.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				prefs.putInteger(MINIMUM_NON_CHANGE_GENERATIONS, Integer.parseInt(numerize(textField.getText())));
			}
		});
		Popup<Label> minimumNonChangeGenerationsTooltip = new Popup<Label>(new Label(" 0 means no limit", skin, "status"), new TooltipBehavior(), new AddToStageBehavior(), addTooltipHiderBehavior, new PositionBehavior(new PointerPosition(), new AlignedOffsetPosition(Align.topLeft), new OffsetPosition(7, -10)), new FadeBehavior());
		tooltipHider.add(minimumNonChangeGenerationsTooltip);
		minimumNonChangeGenerations.addListener(minimumNonChangeGenerationsTooltip);

		// step manually
		final CheckBox stepManually = new CheckBox(" step manually", skin);
		stepManually.setChecked(prefs.getBoolean(STEP_MANUALLY));
		stepManually.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putBoolean(STEP_MANUALLY, stepManually.isChecked());
			}
		});

		TextField stepIterations = new TextField(String.valueOf(prefs.getInteger(STEP_ITERATIONS)), skin);
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
		add(maximumGenerationsLabel).fill();
		add(maximumGenerations).fill().row();
		add(minimumNonChangeGenerationsLabel).fill().padRight(5);
		add(minimumNonChangeGenerations).fill().row();
		add(stepManually).fill();
		add(stepIterations).fill();
	}

	private static class NewValueListener extends ClickListener {

		private final Dialog newValueDialog;
		private final TextField newValueDialogInput;

		/** for Android, iOS, WebGL, Applet */
		private final TextInputListener listener = new TextInputListener() {
			@Override
			public void input(String text) {
				apply(text);
			}

			@Override
			public void canceled() {}
		};

		private ProgressBar target;

		public NewValueListener(ProgressBar target, final Skin skin) {
			this.target = target;

			newValueDialogInput = new TextField("", skin);
			newValueDialogInput.setTextFieldFilter(decimalFilter);

			newValueDialog = new Dialog("new value", skin) {
				{
					getContentTable().add(newValueDialogInput);
					button("OK", true);
					button("cancel");
					key(Keys.ENTER, true);
					key(Keys.ESCAPE, null);
				}

				@Override
				protected void result(Object object) {
					if(object != null && (Boolean) object)
						apply(newValueDialogInput.getText());
				}
			};
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			String current = String.valueOf(target.getValue() * 100);
			if(Gdx.app.getType() == ApplicationType.Desktop) {
				newValueDialogInput.setText("");
				newValueDialogInput.setMessageText(current);
				newValueDialog.show(target.getStage());
			} else
				Gdx.input.getPlaceholderTextInput(listener, "new value", current);
		}

		private void apply(String text) {
			if(text == null || text.isEmpty())
				return;
			text = text.trim().replaceAll("[\\D&&[^\\.]]+", ""); // remove all non-digits except the dot
			text = text.replaceAll("\\.{2,}", "."); // replace multiple dots with one dot
			text = numerize(text);
			target.setValue(Float.parseFloat(text) / 100);
		}

	}

}
