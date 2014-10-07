package net.nexusteam.tsmGaSolver.views;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.Controller;
import net.nexusteam.tsmGaSolver.ann.TSPChromosome;

/**
 * @author dermetfan
 */
public class TsmGaSolver extends ApplicationAdapter {

    private ShapeRenderer renderer;
    private Stage stage;
    private Viewport viewport;
    private Rectangle bounds;
    private Array<Vector2> waypoints = new Array<Vector2>();
    /**
     * the indices of {@link #waypoints} ordered by the currently optimal
     * solution
     */
    private IntArray optimum = new IntArray();
    private Label status, status2;

    private Controller controller;

    @Override
    public void create() {
        Assets.manager.load(Assets.class);
        Assets.manager.finishLoading();

        renderer = new ShapeRenderer();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        bounds = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());

        // create UI
        final Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final Samples samples = new Samples(skin);

        status = new Label("[status message]", skin, "status");
        status.setAlignment(Align.center);

        status2 = new Label("[current optimum]", skin, "status");
        status2.setAlignment(Align.center);

        final TextButton action = new TextButton(Settings.prefs.getBoolean(Settings.STEP_MANUALLY) ? "Step" : "Start", skin);
        action.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action.isDisabled()) {
                    return;
                }
                boolean stepManually = Settings.prefs.getBoolean(Settings.STEP_MANUALLY);
                if (!controller.isRunning()) {
                    String currentSample = samples.getSamples().getSelected();
                    if (currentSample != null && !currentSample.isEmpty()) {
                        Settings.prefs.putString(Settings.CURRENT_SAMPLE, currentSample); // IMPORTANT. If CURRENT_SAMPLE is not set, the WorkerThread cannot create a new Benchmark!
                    }
                    controller.initialize(bounds.width, bounds.height);
                    if (stepManually) {
                        controller.step(Settings.prefs.getInteger(Settings.STEP_ITERATIONS));
                    } else {
                        controller.start();
                    }
                    action.setText("Stop");
                } else {
                    action.setText(stepManually ? "Step" : "Start");
                    controller.stop();
                }
            }
        });

        Button settings = new TextButton("Settings", skin);
        settings.addListener(new ClickListener() {
            Window window = new Window("Settings", skin);

            {
                final int currentWaypointQuantity = Settings.prefs.getInteger(Settings.WAYPOINT_QUANTITY);
                Button close = new TextButton("Close", skin);
                close.addListener(new ClickListener() {
                    Runnable showRunnable = new Runnable() {
                        @Override
                        public void run() {
                            Settings.prefs.flush();
                            if (controller.isRunning()) {
                                controller.stop();
                            }
                            controller.configure();

                            // repopulate if necessary
                            int waypointQuantity = Settings.prefs.getInteger(Settings.WAYPOINT_QUANTITY);
                            if (currentWaypointQuantity != waypointQuantity) {
                                populate(waypointQuantity);
                            }

                            action.setText(Settings.prefs.getBoolean(Settings.STEP_MANUALLY) ? "Step" : "Start");
                            action.setDisabled(false);
                        }
                    };

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        window.addAction(Actions.sequence(Actions.fadeOut(0.4f), Actions.run(showRunnable)));
                    }
                });

                window.add(new Settings()).fill().row();
                window.add(close).fill();
                window.pack();
                 window.setPosition(stage.getWidth() / 2, stage.getHeight() / 2,Align.center);
                window.setColor(1, 1, 1, 0);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!controller.isRunning()) {
                    stage.addActor(window);
                    window.addAction(Actions.fadeIn(0.4f));
                    action.setDisabled(true);
                }
            }

        });

        final Button samplesButton = new TextButton("Samples", skin);
        samplesButton.addListener(new ClickListener() {
            Window window = new Window("Samples", skin);

            {
                Button close = new TextButton("close", skin);
                close.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        window.addAction(Actions.sequence(Actions.fadeOut(0.4f), Actions.removeActor()));
                    }
                });
                window.add(samples).row();
                window.add(close).fillX();
                window.setColor(1, 1, 1, 0);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.pack();
                window.setPosition(stage.getWidth() / 2 - samplesButton.getWidth() / 2, stage.getHeight() / 2 - samplesButton.getHeight() / 2);
                stage.addActor(window);
                window.addAction(Actions.fadeIn(0.4f));
            }
        });

        final Button benchmarks = new TextButton("Benchmarks", skin);
        benchmarks.addListener(new ClickListener() {
           
            Window window = new Window("Benchmarks", skin);
            {
                window.setResizable(true);
                Button close = new TextButton("Close", skin);
                close.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        window.addAction(Actions.sequence(Actions.fadeOut(0.4f), Actions.removeActor()));
                    }
                });
                
                window.add(samples.getBenchmarks()).expand().fill().row();
                window.add(close).expandX().fillX();
                window.setColor(1, 1, 1, 0);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.pack();
                window.setPosition(stage.getWidth() / 2, stage.getHeight() / 2,Align.center);
                //window.setCenterPosition(stage.getWidth() / 2, stage.getHeight() / 2);
                stage.addActor(window);
                window.addAction(Actions.fadeIn(0.4f));
            }
        });

        table.defaults().bottom().fillX();
        table.add(status).expand();
        table.add(action).uniformX();
        table.add(samplesButton).uniformX().row();
        table.add(status2);
        table.add(settings).uniformX();
        table.add(benchmarks).uniformX();

        // adjust bounds
        bounds.y = status.getHeight() + status2.getHeight();
        bounds.height -= bounds.y;

        populate(Settings.prefs.getInteger(Settings.WAYPOINT_QUANTITY));

        controller = new Controller(this, bounds.width, bounds.height, new Runnable() {
            @Override
            public void run() {
                action.setText(Settings.prefs.getBoolean(Settings.STEP_MANUALLY) ? "Step" : "Start");
                samples.getBenchmarks().getActive().setChecked(false);
                samples.updateSamples();
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public synchronized void render() {
        stage.act(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(viewport.getCamera().combined);

        // draw bounds
        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeType.Line);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        renderer.end();

        // draw optimum
        renderer.setColor(Color.GREEN);
        renderer.begin(ShapeType.Line);
        for (int i = 1; i < optimum.size; i++) {
            renderer.line(waypoints.get(optimum.get(i - 1)), waypoints.get(optimum.get(i)));
        }
        renderer.end();

        // draw cities
        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeType.Filled);
        for (Vector2 city : waypoints) {
            renderer.circle(city.x, city.y, 5);
        }
        renderer.end();

        stage.draw();
    }

    private void populate(int count) {
        Pools.freeAll(waypoints, true);
        waypoints.clear();

        for (; count > 0; count--) {
            Vector2 waypoint = Pools.obtain(Vector2.class);
            waypoint.set(MathUtils.random(bounds.x, bounds.x + bounds.width), MathUtils.random(bounds.y, bounds.y + bounds.height));
            waypoints.add(waypoint);
        }

        optimum.clear();
    }

    /**
     * updates the view according to the {@link #controller}
     */
    public synchronized void update() {
        status.setText(controller.status);

        optimum.clear();
        TSPChromosome c = controller.getTopChromosome();
        for (Integer gene : c.getGenes()) {
            optimum.add(gene);
        }

        float distance = 0;
        for (int i = 1; i < optimum.size; i++) {
            distance += waypoints.get(optimum.get(i - 1)).dst(waypoints.get(optimum.get(i)));
        }
        status2.setText("Distance: " + distance);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
        Assets.manager.dispose();
    }

    /**
     * @return the {@link #waypoints}
     */
    public Array<Vector2> getWaypoints() {
        return waypoints;
    }

    /**
     * @return the {@link #optimum}
     */
    public IntArray getOptimum() {
        return optimum;
    }

    /**
     * @return the {@link #controller}
     */
    public Controller getController() {
        return controller;
    }

}
