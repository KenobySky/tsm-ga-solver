package net.nexusteam.tsmGaSolver.views;

import java.io.File;
import java.io.FilenameFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pools;
import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.tools.Benchmark;
import net.nexusteam.tsmGaSolver.tools.Sample;

/**
 * @author dermetfan
 */
public class Samples extends Table {

    public static void findSampleNames(Array<String> fill) {
        fill.clear();
        for (FileHandle sample : Sample.SAMPLE_DIR.list())
			if(sample.isDirectory() && sample.child(Sample.FILENAME).exists())
				fill.add(sample.name());
    }

    /**
     * @author dermetfan
     */
    public class Benchmarks extends Table {

        private final FilenameFilter benchmarkFileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.equalsIgnoreCase(Sample.FILENAME);
            }
        };
        private SelectBox<String> benchmarks;
        private Label currentBenchmarkName, infoLabel;
        /**
         * if checked, the next run should be benchmarked
         */
        private CheckBox active;

        public Benchmarks() {
            Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

            Table controls = new Table(skin), info = new Table(skin);

            { // controls
                benchmarks = new SelectBox<String>(skin);
                TextButton delete = new TextButton("delete", skin);
                delete.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if(Samples.this.samples.getSelected() != null) {
                            Benchmark.fileOf(Samples.this.samples.getSelected(), benchmarks.getSelected()).delete();
                            updateBenchmarks();
                        }
                    }
                });
                TextField name = new TextField(Settings.prefs.getString(Settings.NEW_BENCHMARK_NAME), skin);
                name.setMessageText("new name");
                name.setTextFieldListener(new TextFieldListener() {
                    @Override
                    public void keyTyped(TextField textField, char c) {
                        Settings.prefs.putString(Settings.NEW_BENCHMARK_NAME, textField.getText());
                    }
                });
                active = new CheckBox(" Benchmark this run", skin);
                active.setChecked(Settings.prefs.getBoolean(Settings.BENCHMARK_THIS_RUN));
                active.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (active.isDisabled()) {
                            return;
                        }
                        Settings.prefs.putBoolean(Settings.BENCHMARK_THIS_RUN, active.isChecked());
                    }
                });

                controls.defaults().colspan(2).fillX();
                controls.add(benchmarks).colspan(1);
                controls.add(delete).colspan(1).row();
                controls.add("New Benchmark:").center().fill(false).row();
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

            benchmarks.getList().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (benchmarks != null) {
                        if (benchmarks.getItems().size > 1) {
                            int selectedIndex = benchmarks.getList().getSelectedIndex();
                            showBenchmarkInfo(benchmarks.getItems().size > 0 ? benchmarks.getItems().get(selectedIndex) : null);
                        }
                    }
                }
            });

            pack();
        }

        public void updateBenchmarks() {
            String sample = Samples.this.samples.getSelected();
            if (sample == null || sample.isEmpty()) {
                showBenchmarkInfo(null);
                benchmarks.setItems("No sample selected");
                Settings.prefs.putBoolean(Settings.BENCHMARK_THIS_RUN, false); // we cannot create benchmarks without a sample
                active.setDisabled(true);
                return;
            }
            active.setDisabled(false);
            @SuppressWarnings("unchecked")
            Array<String> benchmarkNames = Pools.obtain(Array.class);
            benchmarkNames.clear();
            for (FileHandle benchmark : Sample.fileOf(sample).parent().list(benchmarkFileFilter)) {
                benchmarkNames.add(benchmark.nameWithoutExtension());
            }
            benchmarks.setItems(benchmarkNames);
            benchmarkNames.clear();
            Pools.free(benchmarkNames);

            showBenchmarkInfo(benchmarks.getItems().size > 0 ? benchmarks.getItems().first() : null);
        }

        public void showBenchmarkInfo(String name) {
            if (name == null || name.isEmpty()) {
                currentBenchmarkName.setText("");
                infoLabel.setText("Please select a benchmark.");
                return;
            }
            currentBenchmarkName.setText(name);
            String sample = Samples.this.samples.getSelected();
            benchmarks.setSelected(name);
            Benchmark benchmark = Benchmark.load(sample, name);
            infoLabel.setText(benchmark.toString());
        }

        /**
         * @return the {@link #active}
         */
        public CheckBox getActive() {
            return active;
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

        samples = new SelectBox<String>(skin);
        samples.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(samples.getSelected() != null) {
                    FileHandle file = Sample.fileOf(samples.getSelected());
                    Sample sample = new Json().fromJson(Sample.class, file);
                    if(sample != null) {
                        TsmGaSolver solver = (TsmGaSolver) Gdx.app.getApplicationListener();
                        if(solver.getController().isRunning()) {
                            solver.getController().stop();
                        }
                        Array<Vector2> waypoints = solver.getWaypoints();
                        waypoints.clear();
                        waypoints.addAll(sample.waypoints);
                        solver.getOptimum().clear();
                        benchmarks.updateBenchmarks();
                        Settings.prefs.putString(Settings.CURRENT_SAMPLE, samples.getSelected());
                    } else {
                        message.setText("Failed to load sample " + file.nameWithoutExtension());
                        notification.show(getStage());
                    }
                }
                benchmarks.updateBenchmarks();
            }
        });
        if (Settings.prefs.contains(Settings.CURRENT_SAMPLE)) {
            String settingsCurrentSample = Settings.prefs.getString(Settings.CURRENT_SAMPLE);
            if (samples.getItems().contains(settingsCurrentSample, false)) {
                samples.setSelected(settingsCurrentSample);
            }
        }

        final TextField name = new TextField("", skin);
        name.setMessageText("new name");
        name.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                Settings.prefs.putString(Settings.NEW_SAMPLE_NAME, textField.getText());
            }
        });

        Button delete = new TextButton("Delete", skin);
        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String sample = samples.getSelected();
                FileHandle file;
                if (sample != null && !sample.isEmpty() && (file = Sample.SAMPLE_DIR.child(sample)).exists()) {

                    if (!file.deleteDirectory()) {
                        if (file.extension().equalsIgnoreCase("null")) {
                            message.setText("Failed to delete sample!");
                        } else {
                            message.setText("Failed to delete sample: " + file.nameWithoutExtension());
                        }
                        notification.show(getStage());
                    } else {
                        updateSamples();
                        pack();
                    }

                } else {
                    if (sample != null && !sample.equalsIgnoreCase("null")) {
                        message.setText("Sample " + sample + " does not exist");
                    } else {
                        message.setText("Sample does not exist");
                    }
                    notification.show(getStage());
                }
            }
        });

        Button save = new TextButton("save", skin);
        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!name.getText().isEmpty()) {
                    final Array<Vector2> waypoints = ((TsmGaSolver) Gdx.app.getApplicationListener()).getWaypoints();
                    FileHandle file = Sample.fileOf(name.getText());
                    if (file.exists()) {
                        Dialog overrideDialog = new Dialog("Sample " + name.getText() + " already exists", skin) {
                            {
                                text("Override?").row().colspan(2);
                                button("Yes", true);
                                button("No", false);
                            }

                            @Override
                            protected void result(Object object) {
                                if (object instanceof Boolean && (Boolean) object) {
                                    new Sample(waypoints).save(name.getText());
                                    updateSamples();
                                    samples.setSelected(name.getText());
                                    Settings.prefs.putString(Settings.CURRENT_SAMPLE, name.getText());
                                }
                                hide();
                            }
                        };
                        overrideDialog.show(getStage());
                    } else {
                        new Sample(waypoints).save(name.getText());
                        updateSamples();
                        samples.setSelected(name.getText());
                        Settings.prefs.putString(Settings.CURRENT_SAMPLE, name.getText());
                    }
                } else {
                    message.setText("Please enter a name");
                    notification.show(getStage());
                }
            }
        });

        defaults().colspan(2);
        add(samples).fillX().colspan(1);
        add(delete).fillX().colspan(1).row();
        add("New Sample:").row();
        add(name).fillX().row();
        add(save).fillX().row();
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

    public SelectBox<String> getSamples() {
        return samples;
    }

}
