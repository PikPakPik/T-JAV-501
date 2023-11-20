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
    private BitmapFont healthFont;
    private BitmapFont pauseFont;
    private BitmapFont restartFont;
    int stringIndex = 0;
    String messageRestart = "";
    Texture gameBg = new Texture("Epidash_Background.jpg");

    private Long gameOverStartTime;
    Character character;
    float time = 0;

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
        healthFont = new BitmapFont();
        pauseFont = new BitmapFont();
        restartFont = new BitmapFont();
        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("tiles/tile_0085.png")),
                (TiledMapTileLayer) map.getLayers().get(0));
        // player.setPosition(111 * 16, 30 * 16);
        player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
        player.pause = true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(gameBg, 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        batch.end();
        renderer.setView(camera);
        renderer.render();

        camera.zoom = 0.15f;
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        renderer.getBatch().begin();
        player.draw(renderer.getBatch());

        if (player.die || player.win || player.talkToGuy) {
            player.stopMoving();

            if (player.health > 0 && player.die) {
                player.health--;
                player.die = false;
                // player.setPosition(111 * 16, 30 * 16);
                player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
            }

            if (isGameOverWin()) {

                game.setScreen(epitaleScreen);
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
        if (player.pause) {
            pauseFont.draw(batch, "Press 'Enter Button' to start", Gdx.graphics.getWidth() / 2 - 100,
                    Gdx.graphics.getHeight() / 2);
            pauseFont.getData().setScale(4, 4);
            pauseFont.setColor(0, 0, 0, 1);
        }

        healthFont.draw(batch, "Health: " + player.health, 10, Gdx.graphics.getHeight() - 10);
        healthFont.getData().setScale(2, 2);
        batch.end();
        if (player.talkToGuy) {
            batch.begin();
            String text = "Hello, are you lost?\n" +
                    "You are going in the wrong way!!!\n" +
                    "You can reach the Epitech building and finish this game by going through the door.\n" +
                    "Good luck!";
            if (time >= 0.2f) {
                messageRestart = messageRestart + text.charAt(stringIndex);
                stringIndex++;
                time -= 0.2f;
            }

            restartFont.draw(batch, text, Gdx.graphics.getWidth() / 2 - 100,
                    Gdx.graphics.getHeight() / 2);
            player.talkToGuy = false;
            // player.setPosition(111 * 16, 25 * 16);
            batch.end();
            player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
        }
        if (player.health == 0) {

            if (time > 5) {
                time = 0f;
                ((Main) Gdx.app.getApplicationListener()).restartGame();
            } else {
                time += Gdx.graphics.getDeltaTime();
                batch.begin();
                batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight());
                batch.end();
            }

        }

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
