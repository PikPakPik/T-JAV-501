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
import fr.epitale.game.MiniGame.SpaceInv.SpaceInvScreen;

public class Epitale extends ScreenAdapter {

  private final Main game;
  public static Map tiledMap;
  public static Character character;
  private Texture characterTexture;
  private SpriteBatch batch;
  private static EpitaleMap epitaleMap;

  public Epitale(final Main game) {
    this.game = game;
    epitaleMap = new EpitaleMap(character);
    character = new Character(34 * 16, 3 * 16);
  }

  @Override
  public void show() {
    tiledMap = epitaleMap;
    characterTexture = new Texture("Tiles/tile_0085.png");
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
    int mapWidth = tiledMap.tiledMap.getProperties().get("width", Integer.class) * 16;
    int mapHeight = tiledMap.tiledMap.getProperties().get("height", Integer.class) * 16;

    if (newX < 0 || newX + 16 > mapWidth || newY < 0 || newY + 16 > mapHeight) {
      return false;
    }

    MapLayers layers = tiledMap.tiledMap.getLayers();
    TiledMapTileLayer wallLayer = (TiledMapTileLayer) layers.get("walls");

    TiledMapTileLayer door1Layer = (TiledMapTileLayer) layers.get("door01");
    TiledMapTileLayer door2Layer = (TiledMapTileLayer) layers.get("door02");
    TiledMapTileLayer door3Layer = (TiledMapTileLayer) layers.get("door03");
    TiledMapTileLayer key1Layer = (TiledMapTileLayer) layers.get("key01");
    TiledMapTileLayer key2Layer = (TiledMapTileLayer) layers.get("key02");
    TiledMapTileLayer key3Layer = (TiledMapTileLayer) layers.get("key03");
    TiledMapTileLayer japeLayer = (TiledMapTileLayer) layers.get("JAPE");
    TiledMapTileLayer spaceInvLayer = (TiledMapTileLayer) layers.get("spaceInv");
    TiledMapTileLayer portails1Layer = (TiledMapTileLayer) layers.get("portails1");
    TiledMapTileLayer pressureplate1Layer = (TiledMapTileLayer) layers.get("pressureplate1");
    TiledMapTileLayer endLayerJAPE = (TiledMapTileLayer) layers.get("end");


    int topLeftX = (int) (newX / 16);
    int topLeftY = (int) ((newY + 14) / 16);
    int topRightX = (int) ((newX + 14) / 16);
    int bottomLeftY = (int) (newY / 16);

    if(key1Layer != null && ( isKey1(key1Layer, topLeftX, topLeftY) ||
            isKey1(key1Layer, topRightX, topLeftY) ||
            isKey1(key1Layer, topLeftX, bottomLeftY) ||
            isKey1(key1Layer, topRightX, bottomLeftY))
    ){
      tiledMap.tiledMap.getLayers().remove(door1Layer);
    }
    if(key2Layer != null && ( isKey2(key2Layer, topLeftX, topLeftY) ||
            isKey2(key2Layer, topRightX, topLeftY) ||
            isKey2(key2Layer, topLeftX, bottomLeftY) ||
            isKey2(key2Layer, topRightX, bottomLeftY))
    ){
      tiledMap.tiledMap.getLayers().remove(door2Layer);
    }
    if(key3Layer != null && ( isKey3(key1Layer, topLeftX, topLeftY) ||
            isKey3(key3Layer, topRightX, topLeftY) ||
            isKey3(key3Layer, topLeftX, bottomLeftY) ||
            isKey3(key3Layer, topRightX, bottomLeftY))
    ){
      tiledMap.tiledMap.getLayers().remove(door3Layer);
    }
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
      character.setX(36 * 16);
      character.setY(3 * 16);
      return false;
    }

    if(pressureplate1Layer != null &&
      (
        isPressurePlate(pressureplate1Layer, topLeftX, topLeftY) ||
        isPressurePlate(pressureplate1Layer, topRightX, topLeftY) ||
        isPressurePlate(pressureplate1Layer, topLeftX, bottomLeftY) ||
        isPressurePlate(pressureplate1Layer, topRightX, bottomLeftY)
      )
    ) {
      tiledMap.tiledMap.getLayers().remove(portails1Layer);
    }

    if (
      wallLayer != null &&
      (
        isWall(wallLayer, portails1Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, portails1Layer, topRightX, topLeftY) ||
        isWall(wallLayer, portails1Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, portails1Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    if (
      door1Layer != null &&
      (
        isWall(wallLayer, door1Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, door1Layer, topRightX, topLeftY) ||
        isWall(wallLayer, door1Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, door1Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    if (
      door2Layer != null &&
      (
        isWall(wallLayer, door2Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, door2Layer, topRightX, topLeftY) ||
        isWall(wallLayer, door2Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, door2Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    if (
      door3Layer != null &&
      (
        isWall(wallLayer, door3Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, door3Layer, topRightX, topLeftY) ||
        isWall(wallLayer, door3Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, door3Layer, topRightX, bottomLeftY)
      )
    ) {
      return false;
    }

    if(portails1Layer != null &&
      (
        isWall(wallLayer, portails1Layer, topLeftX, topLeftY) ||
        isWall(wallLayer, portails1Layer, topRightX, topLeftY) ||
        isWall(wallLayer, portails1Layer, topLeftX, bottomLeftY) ||
        isWall(wallLayer, portails1Layer, topRightX, bottomLeftY)
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
      character.setX(34 * 16);
      character.setY(3 * 16);
      return false;
    }

    if (isSpaceInv(spaceInvLayer, topLeftX, topLeftY) ||
          isSpaceInv(spaceInvLayer, topRightX, topLeftY) ||
          isSpaceInv(spaceInvLayer, topLeftX, bottomLeftY) ||
          isSpaceInv(spaceInvLayer, topRightX, bottomLeftY)
    ) {
      SpaceInvScreen spaceInvScreen = new SpaceInvScreen(game, this);
      game.setScreen(spaceInvScreen);
      tiledMap.tiledMap.getLayers().remove(spaceInvLayer);
    }
    return true;
  }

  private boolean isCellNotNull(TiledMapTileLayer layer, int x, int y) {
    TiledMapTileLayer.Cell cell = (layer != null) ? layer.getCell(x, y) : null;
    return cell != null;
  }
  private boolean isKey1(TiledMapTileLayer key1Layer, int x, int y) {
    return isCellNotNull(key1Layer, x, y);
  }
  private boolean isKey2(TiledMapTileLayer key2Layer, int x, int y) {
    return isCellNotNull(key2Layer, x, y);
  }
  private boolean isKey3(TiledMapTileLayer key3Layer, int x, int y) {
    return isCellNotNull(key3Layer, x, y);
  }
  private boolean isWall(TiledMapTileLayer wallLayer, TiledMapTileLayer secondlayer, int x, int y) {
    TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
    TiledMapTileLayer.Cell cell2 = (secondlayer != null) ? secondlayer.getCell(x, y) : null;
    return cell != null || cell2 != null;
}

  private boolean isPressurePlate(TiledMapTileLayer pressureplate1Layer, int x, int y) {
    TiledMapTileLayer.Cell cell = (pressureplate1Layer != null) ? pressureplate1Layer.getCell(x, y) : null;
    return cell != null;
  }

  private boolean isJape(TiledMapTileLayer japeLayer, int x, int y) {
    return isCellNotNull(japeLayer, x, y);
  }
  private boolean isSpaceInv(TiledMapTileLayer spaceInvLayer, int x, int y) {
    return isCellNotNull(spaceInvLayer, x, y);
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
    epitaleMap.dispose();
  }
}
