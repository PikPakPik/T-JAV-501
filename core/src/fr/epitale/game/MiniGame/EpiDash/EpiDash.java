package fr.epitale.game.MiniGame.EpiDash;

import java.security.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import fr.epitale.game.Main;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.Map.JAPEMap;

import com.badlogic.gdx.Input;

public class EpiDash implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    Sprite sprite;

    public EpiDash() {
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("EpitechDash2.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("tiles/tile_0085.png")),
                (TiledMapTileLayer) map.getLayers().get(0));
        player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        camera.zoom = 0.3f;
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        if (player.die) {
            player.stopMoving();
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                player.die = false;
                player.pause = false;
                player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
            }
        }
        if (!player.pause) {
            player.moveRight();

        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isGrounded() && !player.die) {
            player.jump();

        }

        renderer.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        map.dispose();
        renderer.dispose();
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        dispose();
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }
}
