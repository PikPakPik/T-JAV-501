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
    character = new Character(34 * 16, 3 * 16);
    tiledMap = new EpitaleMap(character);
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

    if (isCharacterVisible()) {
      batch.setProjectionMatrix(tiledMap.camera.combined);
      batch.begin();
      batch.draw(characterTexture, character.getX(), character.getY(), 16, 16);
      batch.end();
    }
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
    TiledMapTileLayer japeLayer = (TiledMapTileLayer) layers.get("JAPE");
    TiledMapTileLayer endLayerJAPE = (TiledMapTileLayer) layers.get("end");

    TiledMapTileLayer[] portailsLayers = new TiledMapTileLayer[4];
    TiledMapTileLayer[] pressureplateLayers = new TiledMapTileLayer[4];

    for (int i = 0; i < 4; i++) {
      portailsLayers[i] = (TiledMapTileLayer) layers.get("portails" + (i + 1));
      pressureplateLayers[i] =
        (TiledMapTileLayer) layers.get("pressureplate" + (i + 1));
    }

    int topLeftX = (int) (newX / 16);
    int topLeftY = (int) ((newY + 14) / 16);
    int topRightX = (int) ((newX + 14) / 16);
    int bottomLeftY = (int) (newY / 16);

    if (
      endLayerJAPE != null &&
      (
        isPressurePlate(endLayerJAPE, topLeftX, topLeftY) ||
        isPressurePlate(endLayerJAPE, topRightX, topLeftY) ||
        isPressurePlate(endLayerJAPE, topLeftX, bottomLeftY) ||
        isPressurePlate(endLayerJAPE, topRightX, bottomLeftY)
      )
    ) {
      tiledMap = new EpitaleMap(character);
      character.setX(49 * 16);
      character.setY(40 * 16);
      TiledMapTileLayer japeLayerEpitale = (TiledMapTileLayer) tiledMap.tiledMap
        .getLayers()
        .get("JAPE");
      if (japeLayerEpitale != null) {
        tiledMap.tiledMap.getLayers().remove(japeLayerEpitale);
      }
      return false;
    }

    for (int i = 0; i < pressureplateLayers.length; i++) {
      TiledMapTileLayer pressureplateLayer = pressureplateLayers[i];
      if (
        pressureplateLayer != null &&
        (
          isPressurePlate(pressureplateLayer, topLeftX, topLeftY) ||
          isPressurePlate(pressureplateLayer, topRightX, topLeftY) ||
          isPressurePlate(pressureplateLayer, topLeftX, bottomLeftY) ||
          isPressurePlate(pressureplateLayer, topRightX, bottomLeftY)
        )
      ) {
        tiledMap.tiledMap.getLayers().remove(portailsLayers[i]);
      }
    }

    if (
      wallLayer != null &&
      (
        isWall(wallLayer, portailsLayers[0], topLeftX, topLeftY) ||
        isWall(wallLayer, portailsLayers[0], topRightX, topLeftY) ||
        isWall(wallLayer, portailsLayers[0], topLeftX, bottomLeftY) ||
        isWall(wallLayer, portailsLayers[0], topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    for (int i = 0; i < portailsLayers.length; i++) {
      TiledMapTileLayer portailsLayer = portailsLayers[i];
      if (
        portailsLayer != null &&
        (
          isWall(portailsLayer, portailsLayer, topLeftX, topLeftY) ||
          isWall(portailsLayer, portailsLayer, topRightX, topLeftY) ||
          isWall(portailsLayer, portailsLayer, topLeftX, bottomLeftY) ||
          isWall(portailsLayer, portailsLayer, topRightX, bottomLeftY)
        )
      ) {
        return false;
      }
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
    TiledMapTileLayer.Cell cell = (key1Layer != null)
      ? key1Layer.getCell(x, y)
      : null;
    return cell != null;
  }

  private boolean isWall(
    TiledMapTileLayer wallLayer,
    TiledMapTileLayer portailsLayer,
    int x,
    int y
  ) {
    TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
    TiledMapTileLayer.Cell cell2 = (portailsLayer != null)
      ? portailsLayer.getCell(x, y)
      : null;
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
