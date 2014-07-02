package net.nexusteam.tsmGaSolver.views;

import java.io.File;
import java.io.FilenameFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pools;
import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.tools.Benchmark;
import net.nexusteam.tsmGaSolver.tools.Sample;

/** @author dermetfan */
public class Samples extends Table {

	public static void findSampleNames(Array<String> fill) {
		fill.clear();
		for(FileHandle file : Sample.SAMPLE_DIR.list())
			fill.add(file.nameWithoutExtension());
	}

	/** @author dermetfan */
	public class Benchmarks extends Table {

		private final FilenameFilter benchmarkFileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equalsIgnoreCase("sample.json");
			}
		};
		private SelectBox<String> benchmarks;
		private Label currentBenchmarkName, infoLabel;

		public Benchmarks() {
			Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

			Table controls = new Table(skin), info = new Table(skin);

			{ // controls
				benchmarks = new SelectBox<>(skin);
				TextButton rename = new TextButton("rename", skin);
				TextButton delete = new TextButton("delete", skin);
				TextField name = new TextField("", skin);
				name.setMessageText("new name");
				CheckBox active = new CheckBox(" Benchmark this run", skin);

				controls.defaults().colspan(2).fillX();
				controls.add(benchmarks).row();
				controls.add(rename).colspan(1);
				controls.add(delete).colspan(1).row();
				controls.add(name).row();
				controls.add(active);
				controls.pack();
			}

			{ // info
				currentBenchmarkName = new Label("[benchmarks benchmark]", skin);
				infoLabel = new Label("[info]", skin);

				info.add(currentBenchmarkName).row();
				info.add(new ScrollPane(infoLabel)).row();
				info.pack();
			}

			add(new SplitPane(controls, info, false, skin)).expand().fill();
			pack();
		}

		public void updateBenchmarks() {
			String sample = Samples.this.samples.getSelected();
			@SuppressWarnings("unchecked")
			Array<String> benchmarkNames = Pools.obtain(Array.class);
			benchmarkNames.clear();
			for(FileHandle benchmark : Sample.fileOf(sample).parent().list(benchmarkFileFilter))
				benchmarkNames.add(benchmark.nameWithoutExtension());
			benchmarks.setItems(benchmarkNames);
			benchmarkNames.clear();
			Pools.free(benchmarkNames);
			showBenchmarkInfo(benchmarks.getItems().size > 0 ? benchmarks.getItems().first() : null);
		}

		public void showBenchmarkInfo(String name) {
			if(name == null || name.isEmpty()) {
				currentBenchmarkName.setText("-");
				infoLabel.setText("Please select a benchmark from the left.");
			}
			currentBenchmarkName.setText(name);
			String sample = Samples.this.samples.getSelected();
			benchmarks.setSelected(name);
			Benchmark benchmark = Benchmark.load(sample, name);
			infoLabel.setText(benchmark.toString());
		}

	}

	private Benchmarks benchmarks = new Benchmarks();
	private SelectBox<String> samples;

	public Samples() {
		this(Assets.manager.get(Assets.uiskin, Skin.class));
	}

	public Samples(final Skin skin) {
		setSkin(skin);

		final Dialog notification = new Dialog("Message", skin);
		final Label message = new Label("", skin);
		notification.text(message);
		notification.button("OK");

		samples = new SelectBox<>(skin);

		final TextField name = new TextField("", skin);
		name.setMessageText("new name");

		updateSamples();

		Button load = new TextButton("load", skin);
		load.addListener(new ClickListener() {
			Json json = new Json();

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileHandle file = Sample.fileOf(samples.getSelected());
				Sample sample = json.fromJson(Sample.class, file);
				if(sample != null) {
					TsmGaSolver solver = (TsmGaSolver) Gdx.app.getApplicationListener();
					if(solver.getController().isRunning())
						solver.getController().stop();
					Array<Vector2> waypoints = solver.getWaypoints();
					waypoints.clear();
					waypoints.addAll(sample.waypoints);
					benchmarks.updateBenchmarks();
				} else {
					message.setText("Failed to load sample " + file.nameWithoutExtension());
					notification.show(getStage());
				}
			}
		});

		Button delete = new TextButton("delete", skin);
		delete.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileHandle file = Sample.SAMPLE_DIR.child(samples.getSelected());
				if(file.exists()) {
					if(!file.deleteDirectory()) {
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

		Button save = new TextButton("save as new sample", skin);
		save.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!name.getText().isEmpty()) {
					final Array<Vector2> waypoints = ((TsmGaSolver) Gdx.app.getApplicationListener()).getWaypoints();
					FileHandle file = Sample.fileOf(name.getText());
					if(file.exists()) {
						Dialog overrideDialog = new Dialog("Sample " + name.getText() + " already exists", skin) {
							{
								text("Override?").row().colspan(2);
								button("Yes", true);
								button("No", false);
							}

							@Override
							protected void result(Object object) {
								if(object instanceof Boolean && (Boolean) object) {
									new Sample(waypoints).save(name.getText());
									updateSamples();
								}
								hide();
							}
						};
						overrideDialog.show(getStage());
					} else {
						new Sample(waypoints).save(name.getText());
						updateSamples();
					}
				} else {
					message.setText("Please enter a name");
					notification.show(getStage());
				}
			}
		});

		add(samples).fillX().colspan(2).row();
		add(load).fillX();
		add(delete).fillX().row();
		add(name).fillX().colspan(2).row();
		add(save).colspan(2).fillX().row();
	}

	public void updateSamples() {
		@SuppressWarnings("unchecked")
		Array<String> sampleNames = Pools.obtain(Array.class);
		findSampleNames(sampleNames);
		sampleNames.sort();
		samples.setItems(sampleNames);
		sampleNames.clear();
		Pools.free(sampleNames);
		benchmarks.updateBenchmarks();
	}

	public Benchmarks getBenchmarks() {
		return benchmarks;
	}

}
