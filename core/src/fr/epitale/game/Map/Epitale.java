package fr.epitale.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import fr.epitale.game.Main;


public class Epitale extends ScreenAdapter {

    final Main game;
    public OrthographicCamera camera;
    public Map tiledMap;

    public Character character;
    public Texture characterTexture;
    public SpriteBatch batch;

    public Epitale(final Main game) {
        this.game = game;
        Gdx.graphics.setWindowedMode(1280, 720);
    }

    @Override
    public void show() {

        tiledMap = new EpitaleMap();

        character = new Character(34*16, 3*16);

        characterTexture = new Texture("tiles/tile_0085.png");
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateCharacterPosition(0, 0);

        tiledMap.moveCamera(character);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMap.tiledMapRenderer.setView(tiledMap.camera);
        tiledMap.tiledMapRenderer.render();

        batch.setProjectionMatrix(tiledMap.camera.combined);
        batch.begin();

        if (isCharacterVisible()) {
            batch.draw(characterTexture, character.getX(), character.getY(), 16, 16);
        }

        batch.end();
    }

    private void handleInput() {
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

        updateCharacterPosition(deltaX, deltaY);
    }

    private void updateCharacterPosition(float deltaX, float deltaY) {
        float newX = character.getX() + deltaX;
        float newY = character.getY() + deltaY;

        // Vérifiez les collisions ici et ajustez newX, newY si nécessaire

        if (isValidMove(newX, newY)) {
            character.setX(newX);
            character.setY(newY);
        }
    }

    private boolean isValidMove(float newX, float newY) {
        int mapWidth = tiledMap.tiledMap.getProperties().get("width", Integer.class) * 16;
        int mapHeight = tiledMap.tiledMap.getProperties().get("height", Integer.class) * 16;

        // Vérification des limites de la carte
        if (newX < 0 || newX + 16 > mapWidth || newY < 0 || newY + 16 > mapHeight) {
            return false;
        }

        TiledMapTileLayer wallLayer = (TiledMapTileLayer) tiledMap.tiledMap.getLayers().get("walls");

        // Convertir les coordonnées du monde en coordonnées de la couche de tuiles
        int fromX = (int) (newX / 16);
        int toX = (int) ((newX + 16) / 16);
        int fromY = (int) (newY / 16);
        int toY = (int) ((newY + 16) / 16);

        // Vérifier la collision avec la couche de murs
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
                if (cell != null) {
                    return false;  // Collision détectée
                }
            }
        }

        return true;  // Aucune collision détectée
    }





    private boolean isCharacterVisible() {
        TiledMapTileLayer layerTunnel = (TiledMapTileLayer) tiledMap.tiledMap.getLayers().get("tunnels");

        TiledMapTileLayer.Cell cellTunnel = layerTunnel.getCell((int) (character.getX() / layerTunnel.getTileWidth()), (int) (character.getY() / layerTunnel.getTileHeight()));

        if (cellTunnel != null) {
            Object property = cellTunnel.getTile().getProperties().get("tunnel");
            return property == null;
        }

        return true;
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