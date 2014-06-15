package net.nexusteam.tsmGaSolver.views;

import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.tools.Sample;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pools;

/** @author dermetfan */
public class Samples extends Window {

	List<String> samples;

	public Samples(String title) {
		this(title, Assets.manager.get(Assets.uiskin, Skin.class));
	}

	public Samples(String title, final Skin skin) {
		super("Samples", skin);
		setSkin(skin);

		final Dialog notification = new Dialog("Message", skin);
		final Label message = new Label("", skin);
		notification.text(message);
		notification.button("OK");

		final TextField name = new TextField("", skin);
		name.setMessageText("sample name");

		samples = new List<String>(skin);
		updateSamples();
		samples.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String text = samples.getSelected();
				if(text == null)
					text = "";
				name.setText(text);
			}
		});

		Button load = new TextButton("load", skin);
		load.addListener(new ClickListener() {

			Json json = new Json();

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Sample.exists(name.getText())) {
					FileHandle file = Sample.fileOf(name.getText());
					Sample sample = json.fromJson(Sample.class, file);
					if(sample != null) {
						TsmGaSolver solver = (TsmGaSolver) Gdx.app.getApplicationListener();
						if(solver.getController().isRunning())
							solver.getController().stop();
						Array<Vector2> waypoints = solver.getWaypoints();
						waypoints.clear();
						waypoints.addAll(sample.waypoints);
					} else {
						message.setText("Failed to load sample " + file.nameWithoutExtension());
						notification.show(getStage());
					}
				} else {
					message.setText("Sample " + name.getText() + " does not exist");
					notification.show(getStage());
				}

			}

		});

		Button delete = new TextButton("delete", skin);
		delete.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileHandle file = Sample.SAMPLE_DIR.child(name.getText() + "." + Sample.FILE_EXTENSION);
				if(file.exists()) {
					if(!file.delete()) {
						message.setText("Failed to delete sample: " + file.nameWithoutExtension());
						notification.show(getStage());

					} else {
						updateSamples();
						pack();
					}
				} else {
					message.setText("Sample " + file.nameWithoutExtension() + " does not exist");
					notification.show(getStage());
				}
			}

		});

		Button save = new TextButton("save", skin);
		save.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Array<Vector2> waypoints = ((TsmGaSolver) Gdx.app.getApplicationListener()).getWaypoints();
				FileHandle file = Sample.SAMPLE_DIR.child(name.getText() + "." + Sample.FILE_EXTENSION);
				if(file.exists()) {
					Dialog overrideDialog = new Dialog("Sample " + name.getText() + " already exists", skin) {

						@Override
						protected void result(Object object) {
							if(object instanceof Boolean && (Boolean) object) {
								new Sample(waypoints).save(name.getText());
								updateSamples();
								pack();
							}
							hide();
						}

					};
					overrideDialog.text("Override?").row().colspan(2);
					overrideDialog.button("Yes", true);
					overrideDialog.button("No", false);
					overrideDialog.show(getStage());
				} else {
					new Sample(waypoints).save(name.getText());
					updateSamples();
					pack();
				}
			}

		});

		Button close = new TextButton("close", skin);
		close.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				addAction(Actions.sequence(Actions.fadeOut(Dialog.fadeDuration), Actions.removeActor()));
			}

		});

		defaults().fill();
		add(new ScrollPane(samples)).colspan(2).row();
		add(name).colspan(2).row();
		Table loadAndDelete = new Table();
		loadAndDelete.defaults().expand().fill();
		loadAndDelete.add(load).row();
		loadAndDelete.add(delete);
		add(loadAndDelete);
		add(save).row();
		add(close).colspan(2);
	}

	public void updateSamples() {
		@SuppressWarnings("unchecked")
		Array<String> sampleNames = Pools.obtain(Array.class);
		sampleNames.clear();
		findSampleNames(sampleNames);
		sampleNames.sort();
		samples.setItems(sampleNames);
		sampleNames.clear();
		Pools.free(sampleNames);
	}

	public static void findSampleNames(Array<String> fill) {
		fill.clear();
		for(FileHandle file : Sample.SAMPLE_DIR.list("." + Sample.FILE_EXTENSION))
			fill.add(file.nameWithoutExtension());
	}

}
