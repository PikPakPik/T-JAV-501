package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import fr.epitale.game.Main;

public class SpaceInvScreen implements Screen {

    protected OrthographicCamera camera;
    protected SpriteBatch batch;

    protected Texture playerShip;
    protected Rectangle playerRect;

    protected Array<Enemy> enemies;

    private static final int ENEMY_ROWS = 5;
    private static final int ENEMY_COLS = 10;
    private static final float ENEMY_SPACING = 50;

    public SpaceInvScreen(final Main game) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();

        playerShip = new Texture("tiles/tile_0085.png");
        playerRect = new Rectangle(800 / 2 - 64 / 2, 20, 64, 64);

        enemies = new Array<>();

        for (int row = 0; row < ENEMY_ROWS; row++) {
            for (int col = 0; col < ENEMY_COLS; col++) {
                Enemy enemy = new Enemy(col * ENEMY_SPACING, 600 - (row * ENEMY_SPACING));
                enemies.add(enemy);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        handleInput();
        movePlayer();
        moveEnemies();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerShip, playerRect.x, playerRect.y);
        for (Enemy enemy : enemies) {
            batch.draw(enemy.texture, enemy.rect.x, enemy.rect.y);
        }
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerRect.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerRect.x += 200 * Gdx.graphics.getDeltaTime();
        }
    }

    private void movePlayer() {
        if (playerRect.x < 0) {
            playerRect.x = 0;
        }
        if (playerRect.x > 800 - 64) {
            playerRect.x = 800 - 64;
        }
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
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
        playerShip.dispose();
        for (Enemy enemy : enemies) {
            enemy.texture.dispose();
        }
    }

}
