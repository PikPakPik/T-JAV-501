package fr.epitale.game.MiniGame.Waze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import fr.epitale.game.Main;
import fr.epitale.game.Map.Character;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.Map.JAPEMap;
import javax.swing.text.Style;

public class WazeScreen implements Screen {

  private final Main game;
  public Character character;
  private Texture characterTexture;
  private SpriteBatch batch;
  private SpriteBatch batchEnd;
  private BitmapFont font;
  private float timeRemaining;

  private boolean gameOverLose = false;
  private long gameOverStartTime;
  private static JAPEMap japeMap;
  private final Epitale epitaleScreen;

  private Texture gameOverTexture;
  private ShapeRenderer shapeRenderer;
  private Pixmap pixmap;
  private TextureRegion textureRegion;
  private boolean hurryUp = false;
  private Timer.Task hurryUpTask = new Timer.Task() {
    @Override
    public void run() {
      hurryUp = !hurryUp;
    }
  };
  private Timer hurryUpTimer = new Timer();

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
    timeRemaining = 60f;
    // Initialiser la police pour afficher le temps restant
    batch = new SpriteBatch();
    batchEnd = new SpriteBatch();
    font = new BitmapFont();

    gameOverTexture = new Texture("gameover.jpg");
    shapeRenderer = new ShapeRenderer();
    pixmap =
      new Pixmap(
        Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight(),
        Pixmap.Format.RGBA8888
      );
    pixmap.setColor(new Color(0, 0, 0, 0.5f));
    pixmap.fill();
    textureRegion = new TextureRegion(new Texture(pixmap));
  }

  @Override
  public void show() {
    characterTexture = new Texture("tiles/tile_0085.png");
    hurryUpTimer.scheduleTask(hurryUpTask, 1f, 0.5f);
  }

  @Override
  public void render(float delta) {
    handleInput();
    updateCharacterPosition(0, 0);
    japeMap.moveCamera();
    timeRemaining -= delta;

    // Convertir le temps restant en minutes et secondes
    int minutes = (int) (timeRemaining / 60);
    int seconds = (int) (timeRemaining % 60);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    japeMap.tiledMapRenderer.setView(japeMap.camera);
    japeMap.tiledMapRenderer.render();
    batch.setProjectionMatrix(japeMap.camera.combined);

    if (isCharacterVisible()) {
      batch.begin();
      batch.draw(characterTexture, character.getX(), character.getY(), 16, 16);
      batch.end();
    }

    shapeRenderer.setProjectionMatrix(japeMap.camera.combined);
    shapeRenderer.begin(ShapeType.Line);

    Gdx.gl.glLineWidth(15f);

    int initialRadius = Math.max(
      Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight()
    );
    for (int radius = initialRadius; radius > 50; radius -= 1) {
      shapeRenderer.setColor(new Color(0, 0, 0, 0.1f));
      shapeRenderer.circle(character.getX() + 8, character.getY() + 8, radius);
    }
    shapeRenderer.end();

    batch.begin();
    batch.draw(
      textureRegion,
      0,
      0,
      Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight()
    );
    batch.end();

    // Afficher le temps restant
    batchEnd.begin();
    if (seconds < 10) {
      font.draw(batchEnd, minutes + ":0" + seconds, 10, 50);
    } else {
      font.draw(batchEnd, minutes + ":" + seconds, 10, 50);
    }
    batchEnd.end();
    font.getData().setScale(4, 4); // Augmente la taille de la police par un facteur de 2
    if (minutes == 2 && seconds < 50) {
      font.setColor(Color.ORANGE);
    } else if (minutes == 0 && seconds < 60) {
      batchEnd.begin();
      if (seconds < 10) {
        if (hurryUp) {
          font.setColor(Color.RED);
        } else {
          font.setColor(Color.ORANGE);
        }
        font.draw(batchEnd, minutes + ":0" + seconds, 10, 50);
      } else {
        if (hurryUp) {
          font.setColor(Color.RED);
        } else {
          font.setColor(Color.ORANGE);
        }
        font.draw(batchEnd, minutes + ":" + seconds, 10, 50);
      }
    }
    batchEnd.end();
    batchEnd.begin();
    if (timeRemaining <= 0) {
      if (!gameOverLose) {
        gameOverLose = true;
        gameOverStartTime = TimeUtils.millis();
      }
      long elapsedTime = TimeUtils.timeSinceMillis(gameOverStartTime);
      if (elapsedTime > 5000) {
        ((Main) Gdx.app.getApplicationListener()).restartGame();
        gameOverLose = false;
      } else {
        batchEnd.draw(
          gameOverTexture,
          0,
          0,
          Gdx.graphics.getWidth(),
          Gdx.graphics.getHeight()
        );
      }
    }
    batchEnd.end();
  }

  private boolean resetCharacterPosition() {
    return character.setPos(40 * 16, 2 * 16);
  }

  private void handleInput() {
    float deltaX = 0;
    float deltaY = 0;

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      deltaX = -character.getSpeed();
      System.out.println("LEFT");
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      deltaX = character.getSpeed();
      System.out.println("RIGHT");
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      deltaY = character.getSpeed();
      System.out.println("UP");
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      deltaY = -character.getSpeed();
      System.out.println("DOWN");
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
    TiledMapTileLayer trapsLayer = (TiledMapTileLayer) layers.get("traps");
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
        System.out.println("PRESSURE PLATE");
        japeMap.tiledMap.getLayers().remove(portailsLayers[i]);
      }
    }

    if (
      trapsLayer != null &&
      (
        isWall(trapsLayer, trapsLayer, topLeftX, topLeftY) ||
        isWall(trapsLayer, trapsLayer, topRightX, topLeftY) ||
        isWall(trapsLayer, trapsLayer, topLeftX, bottomLeftY) ||
        isWall(trapsLayer, trapsLayer, topRightX, bottomLeftY)
      )
    ) {
      System.out.println(character.getX() + " " + character.getY());
      return resetCharacterPosition(); // Modifier ici
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
      System.out.println("WALL");
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
        System.out.println("PORTAIL");
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
    // Libérez les ressources dans dispose()
    characterTexture.dispose();
    batch.dispose();
    batchEnd.dispose();
    font.dispose();
    gameOverTexture.dispose();
    pixmap.dispose();
    textureRegion.getTexture().dispose();

    if (shapeRenderer != null) {
      shapeRenderer.dispose();
      shapeRenderer = null; // Assurez-vous de définir shapeRenderer sur null après l'avoir libéré
    }
    // Ajoutez d'autres libérations de ressources si nécessaire
    game.setScreen(epitaleScreen);
  }
}
