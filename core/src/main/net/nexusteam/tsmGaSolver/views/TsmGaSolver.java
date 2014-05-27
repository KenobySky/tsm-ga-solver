package net.nexusteam.tsmGaSolver.views;

import net.nexusteam.tsmGaSolver.Assets;
import net.nexusteam.tsmGaSolver.Controller;

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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** @author dermetfan */
public class TsmGaSolver extends ApplicationAdapter {

	private ShapeRenderer renderer;
	private Stage stage;
	private Viewport viewport;
	private Rectangle bounds;
	private Array<Vector2> path = new Array<Vector2>();
	private Array<Vector2> optimum = new Array<Vector2>();
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

		controller = new Controller(this, bounds.x - bounds.width, bounds.y - bounds.height);

		// create UI
		final Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		status = new Label("[status message]", skin);
		status.setAlignment(Align.center);

		status2 = new Label("[current optimum]", skin);
		status2.setAlignment(Align.center);

		Button start = new TextButton("Start", skin), settings = new TextButton("Settings", skin);
		start.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO implement
				status.setText("not yet implemented");
				optimum.shuffle();
			}

		});
		settings.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Window window = new Window("Settings", skin);
				Settings.add(window);
				Button close = new TextButton("close", skin);
				close.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						window.remove();
						Settings.prefs.flush();
					}

				});
				window.row();
				window.add(close).colspan(2);
				window.pack();
				window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
				stage.addActor(window);
			}

		});

		table.defaults().bottom().fillX();
		table.add(status).expand();
		table.add(start).row();
		table.add(status2);
		table.add(settings);

		// adjust bounds
		bounds.y = status.getHeight() + status2.getHeight();
		bounds.height -= bounds.y;

		populate(MathUtils.random(3, 15));
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setProjectionMatrix(viewport.getCamera().combined);
		renderer.begin(ShapeType.Line);
		renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

		for(int i = 1; i < path.size; i++)
			renderer.line(path.get(i - 1), path.get(i));

		renderer.setColor(Color.GREEN);

		for(int i = 1; i < optimum.size; i++)
			renderer.line(optimum.get(i - 1), optimum.get(i));

		renderer.setColor(Color.WHITE);
		renderer.end();

		renderer.begin(ShapeType.Filled);

		for(Vector2 city : path)
			renderer.circle(city.x, city.y, 7);

		renderer.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void populate(int count) {
		Pools.freeAll(path, true);
		path.clear();

		for(; count > 0; count--) {
			Vector2 waypoint = Pools.obtain(Vector2.class);
			waypoint.set(MathUtils.random(bounds.x, bounds.x + bounds.width), MathUtils.random(bounds.y, bounds.y + bounds.height));
			path.add(waypoint);
		}

		optimum.clear();

		// TODO remove, this is just demo
		optimum.addAll(path);
		optimum.shuffle();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		stage.dispose();
		Assets.manager.dispose();
	}

}
