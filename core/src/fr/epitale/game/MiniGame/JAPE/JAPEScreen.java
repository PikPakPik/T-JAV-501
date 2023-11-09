package fr.epitale.game.MiniGame.JAPE;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import fr.epitale.game.Main;

public class JAPEScreen implements Screen {
    final Main game;
    private OrthographicCamera camera;

    public JAPEScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {
        game.tiledMap = new TmxMapLoader().load("minigamejape.tmx");
        game.tiledMapRenderer = new OrthogonalTiledMapRenderer(game.tiledMap);
        game.character.setX(39*16);
        game.character.setY(0);
    }

    @Override
    public void render(float delta) {
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
        game.tiledMap.dispose();
    }
}
