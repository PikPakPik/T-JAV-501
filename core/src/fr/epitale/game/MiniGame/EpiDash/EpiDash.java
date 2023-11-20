package fr.epitale.game.MiniGame.EpiDash;

import java.security.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import fr.epitale.game.Map.Character;
import fr.epitale.game.Main;
import fr.epitale.game.Map.Epitale;

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
    Texture guyToTalkWith = new Texture("Tiles/tile_0097.png");
    private Music EpiDashMusic;
    private Music jumpMusic;
    private Music dieMusic;
    private Music gameOverMusic;
    Character character;
    float time = 0;
    float speechDuration = 5f;

    public EpiDash(final Main game, Character character, Epitale epitaleScreen) {
        this.game = game;
        this.epitaleScreen = epitaleScreen;
        this.character = character;
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        EpiDashMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Epidash/EpidashGame.mp3"));
        EpiDashMusic.setLooping(true);
        EpiDashMusic.play();
        map = loader.load("EpitechDash2.tmx");
        batch = new SpriteBatch();
        healthFont = new BitmapFont();
        pauseFont = new BitmapFont();
        restartFont = new BitmapFont();
        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("tiles/tile_0085.png")),
                (TiledMapTileLayer) map.getLayers().get(0));
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
                dieMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Epidash/HitTheWall.mp3"));
                dieMusic.play();
                player.die = false;
                player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
            }

            if (isGameOverWin()) {
                EpiDashMusic.stop();
                game.setScreen(epitaleScreen);
            }
        }
        if (!player.pause && !player.die)

        {
            player.moveRight();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && player.health > 0 && !player.talkToGuy) {
            player.pause = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isGrounded() && !player.die && !player.pause
                && player.health > 0 && !player.talkToGuy) {
            jumpMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Epidash/Jump.mp3"));
            jumpMusic.play();
            player.jump();

        }

        renderer.getBatch().end();

        batch.begin();
        if (player.pause) {
            pauseFont.draw(batch, "Press 'Enter Button' to start", Gdx.graphics.getWidth() / 2 - 200,
                    Gdx.graphics.getHeight() / 2);

            pauseFont.getData().setScale(4, 4);
            pauseFont.setColor(0, 0, 0, 1);
        }

        healthFont.draw(batch, "Health: " + player.health, 10, Gdx.graphics.getHeight() - 10);
        healthFont.getData().setScale(2, 2);
        batch.end();
        if (player.talkToGuy) {
            batch.begin();
            batch.draw(gameBg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(guyToTalkWith, (Gdx.graphics.getWidth() - 200) / 2,
                    Gdx.graphics.getHeight() / 2 + 100, 200, 200);
            String text = "Hello, are you lost?\nYou are going in the wrong way!!!\nYou can reach the Epitech building and finish this game\nby going through the door.\nGood luck!!!";
            time += Gdx.graphics.getDeltaTime();
            if (time >= 0.04f && stringIndex < text.length() && messageRestart != text) {
                messageRestart = messageRestart + text.charAt(stringIndex);
                stringIndex++;
                time -= 0.04f;
            }

            if (time > 5) {
                time = 0f;
                player.talkToGuy = false;
            }

            restartFont.draw(batch, messageRestart, Gdx.graphics.getWidth() / 3,
                    Gdx.graphics.getHeight() / 2);
            restartFont.getData().setScale(3, 3);
            restartFont.setColor(1, 1, 1, 1);

            batch.end();

            player.setPosition(11 * 16, 9 * player.getCollisionLayer().getHeight());
        }
        if (player.health == 0) {
            EpiDashMusic.stop();
            gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/loosemusic.mp3"));
            gameOverMusic.play();

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
