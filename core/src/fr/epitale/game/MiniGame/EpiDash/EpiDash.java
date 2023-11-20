package fr.epitale.game.MiniGame.EpiDash;

import java.security.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import fr.epitale.game.Map.Character;

import fr.epitale.game.Main;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.Map.JAPEMap;

import com.badlogic.gdx.Input;

public class EpiDash implements Screen {
    private final Epitale epitaleScreen;

    private Main game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    Sprite sprite;
    Texture gameOverTexture = new Texture("gameover.jpg");
    private SpriteBatch batch;
    private BitmapFont font;

    private Long gameOverStartTime;
    Character character;

    public EpiDash(final Main game, Character character, Epitale epitaleScreen) {
        this.game = game;
        this.epitaleScreen = epitaleScreen;
        this.character = character;
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("EpitechDash2.tmx");
        batch = new SpriteBatch();
        font = new BitmapFont();
        renderer = new OrthogonalTiledMapRenderer(map);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("tiles/tile_0085.png")),
                (TiledMapTileLayer) map.getLayers().get(0));
        player.setPosition(111 * 16, 25 * 16);
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
        if (player.health == 0) {
            gameOverStartTime = TimeUtils.millis();

            long elapsedTime = TimeUtils.timeSinceMillis(gameOverStartTime);
            if (elapsedTime > 5000) {
                ((Main) Gdx.app.getApplicationListener()).restartGame();
            } else {
                renderer.getBatch().draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight());
            }

        }
        if (player.die || player.win) {
            player.stopMoving();

            if (player.health > 0 && player.die) {
                player.health--;
                player.die = false;
                player.setPosition(111 * 16, 25 * 16);

            }

            if (isGameOverWin()) {

                game.setScreen(epitaleScreen);
                Epitale.character = new Character(28 * 16, 69 * 16);
            }
        }
        if (!player.pause && !player.die)

        {
            player.moveRight();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && player.health > 0) {
            player.pause = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isGrounded() && !player.die && !player.pause
                && player.health > 0) {
            player.jump();

        }

        renderer.getBatch().end();
        batch.begin();

        font.draw(batch, "Health: " + player.health, 200, Gdx.graphics.getHeight() - 10);
        font.getData().setScale(2, 2);
        batch.end();

    }

    private boolean isGameOverWin() {
        return player.win;
    }

    private boolean isGameOverLose() {
        return player.die;
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
