package fr.epitale.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import fr.epitale.game.Background;
import fr.epitale.game.EndScreen;
import fr.epitale.game.Main;
import fr.epitale.game.MiniGame.EpiDash.EpiDash;
import fr.epitale.game.MiniGame.SpaceInv.SpaceInvScreen;
import fr.epitale.game.MiniGame.Waze.MazeScreen;
import fr.epitale.game.PauseMenuScreen;

public class Epitale extends ScreenAdapter {

  private final Main game;
  public static Map tiledMap;
  public static Character character;
  private Texture characterTexture;
  public static SpriteBatch batch;
  private static EpitaleMap epitaleMap;
  private boolean isPaused = false;
  private PauseMenuScreen pauseMenuScreen;
  Background background;
  private float alpha = 0.0f;
  private boolean fading = false;
  private Texture fadeTexture; // Assurez-vous d'initialiser cette texture.
  private Music music = Gdx.audio.newMusic(
      Gdx.files.internal("Sound/game.mp3"));

  private void startFading() {
    fading = true;
    alpha = 0.0f;
  }

  public Epitale(final Main game) {
    this.game = game;
    character = new Character(34 * 16, 3 * 16);
    // character = new Character(69 * 16, 72 * 16);
    background = new Background();
    background.create();
    epitaleMap = new EpitaleMap(character);
    fadeTexture = new Texture("fade.png");
    music.setLooping(true);
    music.play();
  }

  @Override
  public void show() {
    tiledMap = epitaleMap;
    characterTexture = new Texture("Tiles/tile_0085.png");
    batch = new SpriteBatch();
    pauseMenuScreen = new PauseMenuScreen(game, background);
  }

  @Override
  public void render(float delta) {
    tiledMap.character = character;
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      isPaused = !isPaused;

      if (isPaused) {
        music.pause();
        game.setPreviousScreen(this);
        game.setPreviousInputProcessor(Gdx.input.getInputProcessor());
        game.setScreen(pauseMenuScreen);
      } else {
        game.setScreen(this);
        Gdx.input.setInputProcessor(null);
      }
    }

    if (!isPaused) {
      music.play();
      handleInput();
      updateCharacterPosition(0, 0);

      tiledMap.moveCamera();

      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      tiledMap.tiledMapRenderer.setView(tiledMap.camera);
      tiledMap.tiledMapRenderer.render();

      batch.begin();
      batch.setProjectionMatrix(tiledMap.camera.combined);

      if (isCharacterVisible()) {
        batch.draw(
            characterTexture,
            character.getX(),
            character.getY(),
            16,
            16);
      }
      batch.end();
      if (fading) {
        if (alpha < 1.0f) {
          alpha += delta; // Ajustez ce facteur en fonction de la vitesse du fondu souhaitée
          if (alpha > 1.0f) {
            alpha = 1.0f;
            // Changer d'écran ici
            game.setScreen(new EndScreen(game));
          }
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(
            fadeTexture,
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
      }
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

    TiledMapTileLayer endGameLayer = (TiledMapTileLayer) layers.get("endGame");
    TiledMapTileLayer epiDashLayer = (TiledMapTileLayer) layers.get("epiDash");

    TiledMapTileLayer spaceInvLayer = (TiledMapTileLayer) layers.get(
        "spaceInv");

    int topLeftX = (int) (newX / 16);
    int topLeftY = (int) ((newY + 14) / 16);
    int topRightX = (int) ((newX + 14) / 16);
    int bottomLeftY = (int) (newY / 16);

    if (key1Layer != null &&
        (isKey1(key1Layer, topLeftX, topLeftY) ||
            isKey1(key1Layer, topRightX, topLeftY) ||
            isKey1(key1Layer, topLeftX, bottomLeftY) ||
            isKey1(key1Layer, topRightX, bottomLeftY))) {
      tiledMap.tiledMap.getLayers().remove(door1Layer);
    }
    if (key2Layer != null &&
        (isKey2(key2Layer, topLeftX, topLeftY) ||
            isKey2(key2Layer, topRightX, topLeftY) ||
            isKey2(key2Layer, topLeftX, bottomLeftY) ||
            isKey2(key2Layer, topRightX, bottomLeftY))) {
      tiledMap.tiledMap.getLayers().remove(door2Layer);
    }
    if (key3Layer != null &&
        (isKey3(key3Layer, topLeftX, topLeftY) ||
            isKey3(key3Layer, topRightX, topLeftY) ||
            isKey3(key3Layer, topLeftX, bottomLeftY) ||
            isKey3(key3Layer, topRightX, bottomLeftY))) {
      tiledMap.tiledMap.getLayers().remove(door3Layer);
    }

    if (door1Layer != null &&
        (isWall(wallLayer, door1Layer, topLeftX, topLeftY) ||
            isWall(wallLayer, door1Layer, topRightX, topLeftY) ||
            isWall(wallLayer, door1Layer, topLeftX, bottomLeftY) ||
            isWall(wallLayer, door1Layer, topRightX, bottomLeftY))) {
      return false;
    }

    if (door2Layer != null &&
        (isWall(wallLayer, door2Layer, topLeftX, topLeftY) ||
            isWall(wallLayer, door2Layer, topRightX, topLeftY) ||
            isWall(wallLayer, door2Layer, topLeftX, bottomLeftY) ||
            isWall(wallLayer, door2Layer, topRightX, bottomLeftY))) {
      return false;
    }

    if (door3Layer != null &&
        (isWall(wallLayer, door3Layer, topLeftX, topLeftY) ||
            isWall(wallLayer, door3Layer, topRightX, topLeftY) ||
            isWall(wallLayer, door3Layer, topLeftX, bottomLeftY) ||
            isWall(wallLayer, door3Layer, topRightX, bottomLeftY))) {
      return false;
    }

    if (wallLayer != null &&
        (isWall(wallLayer, null, topLeftX, topLeftY) ||
            isWall(wallLayer, null, topRightX, topLeftY) ||
            isWall(wallLayer, null, topLeftX, bottomLeftY) ||
            isWall(wallLayer, null, topRightX, bottomLeftY))) {
      return false;
    }

    if (endGameLayer != null &&
        (isWall(endGameLayer, endGameLayer, topLeftX, topLeftY) &&
            !fading ||
            isWall(endGameLayer, endGameLayer, topRightX, topLeftY) &&
                !fading
            ||
            isWall(endGameLayer, endGameLayer, topLeftX, bottomLeftY) &&
                !fading
            ||
            isWall(endGameLayer, endGameLayer, topRightX, bottomLeftY) &&
                !fading)) {
      music.stop();
      startFading();
      return true;
    }

    if (japeLayer != null &&
        (isJape(japeLayer, topLeftX, topLeftY) ||
            isJape(japeLayer, topRightX, topLeftY) ||
            isJape(japeLayer, topLeftX, bottomLeftY) ||
            isJape(japeLayer, topRightX, bottomLeftY))) {
      music.pause();
      game.setScreen(new MazeScreen(game, character, this));
      tiledMap.tiledMap.getLayers().remove(japeLayer);
      return false;
    }

    if (isSpaceInv(spaceInvLayer, topLeftX, topLeftY) ||
        isSpaceInv(spaceInvLayer, topRightX, topLeftY) ||
        isSpaceInv(spaceInvLayer, topLeftX, bottomLeftY) ||
        isSpaceInv(spaceInvLayer, topRightX, bottomLeftY)) {
      music.pause();
      SpaceInvScreen spaceInvScreen = new SpaceInvScreen(game, character, this);
      game.setScreen(spaceInvScreen);
      tiledMap.tiledMap.getLayers().remove(spaceInvLayer);
    }

    if (isEpiDash(epiDashLayer, topLeftX, topLeftY) ||
        isEpiDash(epiDashLayer, topRightX, topLeftY) ||
        isEpiDash(epiDashLayer, topLeftX, bottomLeftY) ||
        isEpiDash(epiDashLayer, topRightX, bottomLeftY)) {
      music.pause();
      game.setScreen(new EpiDash(game, character, this));
      tiledMap.tiledMap.getLayers().remove(epiDashLayer);
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

  private boolean isWall(
      TiledMapTileLayer wallLayer,
      TiledMapTileLayer secondlayer,
      int x,
      int y) {
    TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
    TiledMapTileLayer.Cell cell2 = (secondlayer != null)
        ? secondlayer.getCell(x, y)
        : null;
    return cell != null || cell2 != null;
  }

  private boolean isJape(TiledMapTileLayer japeLayer, int x, int y) {
    return isCellNotNull(japeLayer, x, y);
  }

  private boolean isSpaceInv(TiledMapTileLayer spaceInvLayer, int x, int y) {
    return isCellNotNull(spaceInvLayer, x, y);
  }

  private boolean isEpiDash(TiledMapTileLayer epiDashLayer, int x, int y) {
    return isCellNotNull(epiDashLayer, x, y);
  }

  private boolean isCharacterVisible() {
    TiledMapTileLayer layerTunnel = (TiledMapTileLayer) tiledMap.tiledMap
        .getLayers()
        .get("tunnels");

    TiledMapTileLayer.Cell cellTunnel = layerTunnel.getCell(
        (int) (character.getX() / layerTunnel.getTileWidth()),
        (int) (character.getY() / layerTunnel.getTileHeight()));

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
    epitaleMap.dispose();
  }
}
