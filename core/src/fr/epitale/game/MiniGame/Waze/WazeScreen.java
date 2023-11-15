package fr.epitale.game.MiniGame.Waze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import fr.epitale.game.Main;
import fr.epitale.game.Map.Character;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.Map.JAPEMap;

public class WazeScreen implements Screen {

  private final Main game;
  public Character character;
  private Texture characterTexture;
  private SpriteBatch batch;

  private static JAPEMap japeMap;
  private final Epitale epitaleScreen;

  public WazeScreen(
    final Main game,
    Character character,
    Epitale epitaleScreen
  ) {
    this.game = game;
    this.epitaleScreen = epitaleScreen;
    this.character = character;
    japeMap = new JAPEMap(character);
    character.setX(40 * 16);
    character.setY(2 * 16);
  }

  @Override
  public void show() {
    characterTexture = new Texture("tiles/tile_0085.png");
    batch = new SpriteBatch();
  }

  @Override
  public void render(float delta) {
    handleInput();
    updateCharacterPosition(0, 0);
    japeMap.moveCamera();

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    japeMap.tiledMapRenderer.setView(japeMap.camera);
    japeMap.tiledMapRenderer.render();

    if (isCharacterVisible()) {
      batch.begin(); // Début de la première session de dessin
      batch.setProjectionMatrix(japeMap.camera.combined);
      batch.draw(characterTexture, character.getX(), character.getY(), 16, 16);

      batch.end(); // Fin de la première session de dessin
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(japeMap.camera.combined);

    // Définir la largeur de ligne
    Gdx.gl.glLineWidth(20f); // Remplacez 3f par la largeur de ligne souhaitée

    // Dessiner un grand nombre de cercles concentriques pour simuler un effet de vision
    int initialRadius = Math.max(
      Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight()
    );
    for (int radius = initialRadius; radius > 50; radius -= 1) { // Arrêter de dessiner des cercles lorsque le rayon atteint 200
      shapeRenderer.begin(ShapeType.Line);
      shapeRenderer.setColor(new Color(0, 0, 0, 0.1f));
      shapeRenderer.circle(character.getX() + 8, character.getY() + 8, radius);
      shapeRenderer.end();
    }
    Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
    pixmap.setColor(new Color(0, 0, 0, 0.5f));
    pixmap.fill();

    TextureRegion textureRegion = new TextureRegion(new Texture(pixmap));

    batch.begin();
    batch.draw(textureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
      japeMap.tiledMap.getProperties().get("width", Integer.class) * 16;
    int mapHeight =
      japeMap.tiledMap.getProperties().get("height", Integer.class) * 16;

    if (newX < 0 || newX + 16 > mapWidth || newY < 0 || newY + 16 > mapHeight) {
      return false;
    }

    MapLayers layers = japeMap.tiledMap.getLayers();
    TiledMapTileLayer wallLayer = (TiledMapTileLayer) layers.get("walls");
    TiledMapTileLayer[] portailsLayers = new TiledMapTileLayer[4];
    TiledMapTileLayer[] pressureplateLayers = new TiledMapTileLayer[4];

    for (int i = 0; i < 4; i++) {
      portailsLayers[i] = (TiledMapTileLayer) layers.get("portails" + (i + 1));
      pressureplateLayers[i] =
        (TiledMapTileLayer) layers.get("pressureplate" + (i + 1));
    }

    TiledMapTileLayer endLayerJAPE = (TiledMapTileLayer) layers.get("end");

    int topLeftX = (int) (newX / 16);
    int topLeftY = (int) ((newY + 14) / 16);
    int topRightX = (int) ((newX + 14) / 16);
    int bottomLeftY = (int) (newY / 16);

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
        japeMap.tiledMap.getLayers().remove(portailsLayers[i]);
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
      endLayerJAPE != null && isCellNotNull(endLayerJAPE, topLeftX, topLeftY)
    ) {
      this.dispose();
    }

    return true;
  }

  private boolean isCellNotNull(TiledMapTileLayer layer, int x, int y) {
    TiledMapTileLayer.Cell cell = (layer != null) ? layer.getCell(x, y) : null;
    return cell != null;
  }

  private boolean isWall(
    TiledMapTileLayer wallLayer,
    TiledMapTileLayer secondlayer,
    int x,
    int y
  ) {
    TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
    TiledMapTileLayer.Cell cell2 = (secondlayer != null)
      ? secondlayer.getCell(x, y)
      : null;
    return cell != null || cell2 != null;
  }

  private boolean isPressurePlate(
    TiledMapTileLayer pressureplate1Layer,
    int x,
    int y
  ) {
    TiledMapTileLayer.Cell cell = (pressureplate1Layer != null)
      ? pressureplate1Layer.getCell(x, y)
      : null;
    return cell != null;
  }

  private boolean isCharacterVisible() {
    TiledMapTileLayer layerTunnel = (TiledMapTileLayer) japeMap.tiledMap
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
    game.setScreen(epitaleScreen);
    Epitale.character = character;
    character.setX(50 * 16);
    character.setY(52 * 16);
  }
}
