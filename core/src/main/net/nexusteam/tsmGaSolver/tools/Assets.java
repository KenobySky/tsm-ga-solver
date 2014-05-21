package main.net.nexusteam.tsmGaSolver.tools;

import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class Assets {

	public static final AnnotationAssetManager manager = new AnnotationAssetManager();

	@Asset(type = Skin.class)
	public static final String uiskin = "assets/uiskin.json";

}
