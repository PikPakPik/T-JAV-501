package fr.epitale.game.Map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public abstract class Map implements Screen {
  public OrthographicCamera camera;
  public TiledMap tiledMap;
  public Character character;
  public BatchTiledMapRenderer tiledMapRenderer;
  float zoomFactor = 0.3f;

  public Map(String map, Character character) {
    int windowWidth = 1280;
    int windowHeight = 720;
    this.character = character;
    tiledMap = new TmxMapLoader().load(map);
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    camera = new OrthographicCamera();
    camera.setToOrtho(
      false,
      windowWidth * zoomFactor,
      windowHeight * zoomFactor
    );
    camera.update();
  }

  @Override
  public void show() {}

  public void updateZoomFactor(float zoomFactor) {
    camera.setToOrtho(
      false,
      1280 * zoomFactor,
      720 * zoomFactor
    );
  }

  public void moveCamera() {
    camera.position.set(character.getX(),character.getY(), 0);
    camera.update();
  }

  @Override
  public void render(float delta) {
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();
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
  public void dispose() {}
}
