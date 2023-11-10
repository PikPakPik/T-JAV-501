package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import fr.epitale.game.Main;

public class SpaceInvScreen implements Screen {

    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Player player;
    private final Array<Enemy> enemies;
    private boolean moveEnemiesRight = true;

    public SpaceInvScreen(final Main game) {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();

        player = new Player();
        enemies = new Array<>();

        for (int row = 0; row < Enemy.ENEMY_ROWS; row++) {
            for (int col = 0; col < Enemy.ENEMY_COLS; col++) {
                Enemy enemy = new Enemy(col * Enemy.ENEMY_SPACING, 600 - (row * Enemy.ENEMY_SPACING));
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
        moveEnemies();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.render(batch);
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
        player.movePlayerProjs();
        player.shoot();
        player.renderPlayerProjs(batch);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move(moveEnemiesRight);
        }

        if (ChangeEnemiesDirection()) {
            for (Enemy enemy : enemies) {
                enemy.moveDown();
            }
            moveEnemiesRight = !moveEnemiesRight;
        }
    }

    private boolean ChangeEnemiesDirection() {
        for (Enemy enemy : enemies) {
            float enemyX = enemy.rect.x;
            float enemyWidth = enemy.rect.width;

            if (enemyX <= 0 && moveEnemiesRight || (enemyX + enemyWidth) >= 800 && !moveEnemiesRight) {
                return true;
            }
        }
        return false;
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
        player.dispose();
        player.disposePlayerProjs();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
    }
}
