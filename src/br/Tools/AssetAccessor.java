package br.Tools;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author André Vinícius Lopes
 */
public class AssetAccessor implements Disposable {

    private static String atlasDirectory = "packed/resources.atlas";
    private static final AssetAccessor instance = new AssetAccessor();
    public final AssetManager manager = new AssetManager();

    public AssetAccessor() {

    }

    public void load() {

        manager.load(atlasDirectory, TextureAtlas.class);

        manager.finishLoading();

    }

    private static AssetAccessor getInstance() {
        return instance;
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public static TextureAtlas getAtlas() {
        return AssetAccessor.getInstance().manager.get(atlasDirectory, TextureAtlas.class);

    }

}
