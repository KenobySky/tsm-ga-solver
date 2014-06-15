package net.nexusteam.tsmGaSolver.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pools;
import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.tools.Sample;

/** @author dermetfan */
public class Samples extends Table {

	public static void findSampleNames(Array<String> fill) {
		fill.clear();
		for(FileHandle file : Sample.SAMPLE_DIR.list("." + Sample.FILE_EXTENSION))
			fill.add(file.nameWithoutExtension());
	}

	List<String> samples;

	public Samples() {
		this(Assets.manager.get(Assets.uiskin, Skin.class));
	}

	public Samples(final Skin skin) {
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
				if(text == null) {
					text = "";
				}
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
						packParent();
					}
				} else {
					message.setText("Sample " + file.nameWithoutExtension() + " does not exist");
					notification.show(getStage());
				}
			}
		});

		Button save = new TextButton("save new sample", skin);
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
					packParent();
				}
			}
		});

		add(name).fillX().colspan(2).row();
		add(load).fillX();
		add(delete).fillX().row();
		add(save).colspan(2).fillX().row();
		add("Samples:").row();
		add(new ScrollPane(samples)).maxHeight(Gdx.graphics.getHeight() / 2).colspan(2).fill().row();
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

	public void packParent() {
		Actor parent = getParent();
		if(parent instanceof WidgetGroup) {
			((WidgetGroup) parent).pack();
		}
	}

}
