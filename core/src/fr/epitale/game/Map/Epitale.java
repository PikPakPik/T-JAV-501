package fr.epitale.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import fr.epitale.game.Main;
import fr.epitale.game.MiniGame.JAPE.JAPEScreen;


public class Epitale implements Screen {

    final Main game;
    public OrthographicCamera camera;
    public TiledMap tiledMap;
    public TiledMapRenderer tiledMapRenderer;

    public Character character;
    public Texture characterTexture;
    public SpriteBatch batch;
    float zoomFactor = 0.3f;

    public Epitale(final Main game) {
        int windowWidth = 1280;
        int windowHeight = 1280;
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth * zoomFactor, windowHeight * zoomFactor); // Appliquer le zoom ici
        camera.update();
        Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
    }

    @Override
    public void show() {

        tiledMap = new TmxMapLoader().load("epitales-map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        character = new Character(34*16, 3*16);

        characterTexture = new Texture("tiles/tile_0085.png");
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaX = 0;
        float deltaY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            deltaX = -character.getSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            deltaX = character.getSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            deltaY = character.getSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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
                TiledMapTileLayer japeLayer = (TiledMapTileLayer) tiledMap.getLayers().get("JAPE");

                if (cell != null) {
                    Rectangle tileBounds = new Rectangle(col, mapHeight - row - 16, 16, 16);

                    if (newX < tileBounds.x + tileBounds.width - 16 && newX + 16 > tileBounds.x &&
                            newY < tileBounds.y - 16 + tileBounds.height && newY + 16 > tileBounds.y) {
                        deltaX = 0;
                        deltaY = 0;
                    }
                }

                if (japeLayer != null) {
                    TiledMapTileLayer.Cell cellJAPE = japeLayer.getCell((int) (character.getX() / 16), (int) (character.getY() / 16));
                    if (cellJAPE != null) {
                        game.setScreen(new JAPEScreen(this));
                        return;
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}
