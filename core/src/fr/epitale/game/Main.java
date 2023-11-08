package fr.epitale.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Main extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	@Override
	public void create() {
		int screenWidth = Gdx.graphics.getWidth(); // Largeur actuelle de l'écran
		int screenHeight = Gdx.graphics.getHeight(); // Hauteur actuelle de l'écran

		// Définissez la largeur et la hauteur souhaitées pour la fenêtre
		int windowWidth = 1280; // Remplacez par la largeur souhaitée
		int windowHeight = 1280;// Remplacez par la hauteur souhaitée

		camera = new OrthographicCamera();
		camera.setToOrtho(false, windowWidth, windowHeight);
		camera.update();

		// Changez la taille de la fenêtre
		Gdx.graphics.setWindowedMode(windowWidth, windowHeight);

		tiledMap = new TmxMapLoader().load("epitales-map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}

	@Override
	public void dispose() {
		tiledMap.dispose();
	}
}
