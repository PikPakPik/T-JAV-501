package fr.epitale.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import fr.epitale.game.Main;

public class Epitale extends ScreenAdapter {

  private final Main game;
  private Map tiledMap;

  public static Character character;
  private Texture characterTexture;
  private SpriteBatch batch;

  public Epitale(final Main game) {
    this.game = game;
  }

  @Override
  public void show() {
    tiledMap = new EpitaleMap(character);

    character = new Character(34 * 16, 3 * 16);

    characterTexture = new Texture("tiles/tile_0085.png");
    batch = new SpriteBatch();
  }

  @Override
  public void render(float delta) {
    handleInput();
    updateCharacterPosition(0, 0);

    tiledMap.moveCamera();

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

    if (isValidMove(newX, character.getY())) {
      character.setX(newX);
    }
    if (isValidMove(character.getX(), newY)) {
      character.setY(newY);
    }
  }

  private boolean isValidMove(float newX, float newY) {
    int mapWidth =
      tiledMap.tiledMap.getProperties().get("width", Integer.class) * 16;
    int mapHeight =
      tiledMap.tiledMap.getProperties().get("height", Integer.class) * 16;

    if (newX < 0 || newX + 16 > mapWidth || newY < 0 || newY + 16 > mapHeight) {
      return false;
    }

    MapLayers layers = tiledMap.tiledMap.getLayers();
    TiledMapTileLayer wallLayer = (TiledMapTileLayer) layers.get("walls");
    TiledMapTileLayer door1Layer = (TiledMapTileLayer) layers.get("portails1");
    TiledMapTileLayer japeLayer = (TiledMapTileLayer) layers.get("JAPE");
    TiledMapTileLayer key1Layer = (TiledMapTileLayer) layers.get("pressureplate1");

    int topLeftX = (int) (newX / 16);
    int topLeftY = (int) ((newY + 14) / 16);
    int topRightX = (int) ((newX + 14) / 16);
    int bottomLeftY = (int) (newY / 16);

    if(key1Layer != null &&
      (
        isPressurePlate(key1Layer, topLeftX, topLeftY) ||
        isPressurePlate(key1Layer, topRightX, topLeftY) ||
        isPressurePlate(key1Layer, topLeftX, bottomLeftY) ||
        isPressurePlate(key1Layer, topRightX, bottomLeftY)
      )
    ) {
      tiledMap.tiledMap.getLayers().remove(door1Layer);
    }

    if (
      wallLayer != null &&
      (
        isWall(wallLayer, door1Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, door1Layer, topRightX, topLeftY) ||
        isWall(wallLayer, door1Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, door1Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    if(door1Layer != null &&
      (
        isWall(door1Layer, door1Layer, topLeftX, topLeftY) ||
        isWall(door1Layer, door1Layer, topRightX, topLeftY) ||
        isWall(door1Layer, door1Layer, topLeftX, bottomLeftY) ||
        isWall(door1Layer, door1Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    } 

    if (
      japeLayer != null &&
      (
        isJape(japeLayer, topLeftX, topLeftY) ||
        isJape(japeLayer, topRightX, topLeftY) ||
        isJape(japeLayer, topLeftX, bottomLeftY) ||
        isJape(japeLayer, topRightX, bottomLeftY)
      )
    ) {
      tiledMap = new JAPEMap(character);
      character.setX(36 * 16);
      character.setY(0);
      return false;
    }

    return true;
  }

  private boolean isPressurePlate(TiledMapTileLayer key1Layer, int x, int y) {
    TiledMapTileLayer.Cell cell = (key1Layer != null) ? key1Layer.getCell(x, y) : null;
    return cell != null;
  }

  private boolean isWall(TiledMapTileLayer wallLayer, TiledMapTileLayer door1Layer, int x, int y) {
    TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
    TiledMapTileLayer.Cell cell2 = (door1Layer != null) ? door1Layer.getCell(x, y) : null;
    return cell != null || cell2 != null;
}

  private boolean isJape(TiledMapTileLayer japeLayer, int x, int y) {
    TiledMapTileLayer.Cell cell = japeLayer.getCell(x, y);
    return cell != null;
  }

  private boolean isCharacterVisible() {
    TiledMapTileLayer layerTunnel = (TiledMapTileLayer) tiledMap.tiledMap
      .getLayers()
      .get("tunnels");

    TiledMapTileLayer.Cell cellTunnel = layerTunnel.getCell(
      (int) (character.getX() / layerTunnel.getTileWidth()),
      (int) (character.getY() / layerTunnel.getTileHeight())
    );

    if (cellTunnel != null) {
      Object property = cellTunnel.getTile().getProperties().get("tunnel");
      return property == null;
    }

    return true;
  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    tiledMap.dispose();
  }
}
