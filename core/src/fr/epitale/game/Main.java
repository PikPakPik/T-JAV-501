package fr.epitale.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Main extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	Character character;
	Texture characterTexture;
	SpriteBatch batch;
	float zoomFactor = 0.3f;

	@Override
	public void create() {
		int windowWidth = 1280;
		int windowHeight = 1280;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, windowWidth * zoomFactor, windowHeight * zoomFactor); // Appliquer le zoom ici
		camera.update();

		Gdx.graphics.setWindowedMode(windowWidth, windowHeight);

		tiledMap = new TmxMapLoader().load("epitales-map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		character = new Character(34*16, 3*16);

		characterTexture = new Texture("tiles/tile_0085.png");
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float deltaX = 0;
		float deltaY = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			deltaX = -character.getSpeed();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			deltaX = character.getSpeed();
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			deltaY = character.getSpeed();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			deltaY = -character.getSpeed();
		}

		float newX = character.getX() + deltaX;
		float newY = character.getY() + deltaY;

		int mapWidth = tiledMap.getProperties().get("width", Integer.class) * 16;
		int mapHeight = tiledMap.getProperties().get("height", Integer.class) * 16;
		boolean characterVisible = true;

		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get("walls")).getCell(col / 16, (mapHeight - row - 16) / 16);

				if (cell != null) {
					Rectangle tileBounds = new Rectangle(col, mapHeight - row - 16, 16, 16);

					if (newX < tileBounds.x + tileBounds.width - 16 && newX + 16 > tileBounds.x &&
							newY < tileBounds.y - 16 + tileBounds.height && newY + 16 > tileBounds.y) {
						deltaX = 0;
						deltaY = 0;
					}
				}
			}
		}

		for (int row = 0; row < mapHeight; row += 16) {
			for (int col = 0; col < mapWidth; col += 16) {
				TiledMapTileLayer tunnelsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("tunnels");

				int characterTileX = (int) (newX / 16);
				int characterTileY = (int) ((mapHeight - newY - 16) / 16);
				int cellCol = characterTileX * 16 / 16;  // Indice de colonne dans la couche "tunnels"
				int cellRow = (mapHeight - characterTileY * 16) / 16 - 1;  // Indice de ligne dans la couche "tunnels"

				TiledMapTileLayer.Cell cell = tunnelsLayer.getCell(cellCol, cellRow);

				if (cell != null) {
					characterVisible = false;
					break;
				}
			}
		}

		float characterX = character.getX() + deltaX;
		float characterY = character.getY() + deltaY;

		if (characterX >= 0 && characterX + 16 <= mapWidth && characterY >= 0 && characterY + 16 <= mapHeight) {
			character.move(deltaX, deltaY);
		}

		camera.position.set(character.getX(), character.getY(), 0);
		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if (characterVisible) {
			batch.draw(characterTexture, character.getX(), character.getY(), 16, 16);
		}

		batch.end();
	}



	@Override
	public void dispose() {
		tiledMap.dispose();
	}
}
