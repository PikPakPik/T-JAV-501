package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import fr.epitale.game.Main;
import fr.epitale.game.Map.Character;
import fr.epitale.game.Map.Epitale;

public class SpaceInvScreen implements Screen {
    protected final Main game;
    protected Texture backgroundTexture;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Player player;
    private final Array<Enemy> enemies;
    private boolean moveEnemiesRight = true;
    private final Epitale epitaleScreen;

    public SpaceInvScreen(final Main game, Epitale epitaleScreen) {
        this.game = game;
        this.epitaleScreen = epitaleScreen;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        Texture backgroundTexture = new Texture("Tiles/tile_0049.png");
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(backgroundTexture, 0, 0, 800, 600);

        player.render(batch);
        for (Enemy enemy : enemies) {
            enemy.render(batch);
            enemy.moveProjs();
            enemy.move(moveEnemiesRight, Gdx.graphics.getDeltaTime());
        }

        player.movePlayerProjs();
        player.shoot();
        player.renderPlayerProjs(batch);
        player.checkEnemyProjectileCollision(enemies);
        player.renderPlayerProjs(batch);
        player.update(delta, enemies);
        checkCollisions();
        if (isGameOver()) {
            dispose();
            return;
        }
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
    private void checkCollisions() {
        for (PlayerProj playerProj : player.playerProjs) {
            for (Enemy enemy : enemies) {
                if (playerProj.collidesWith(enemy)) {
                    player.playerProjs.removeValue(playerProj, true);
                    enemies.removeValue(enemy, true);
                }
            }
        }
    }
    private boolean isGameOver() {
        for (Enemy enemy : enemies) {
            if (enemy.rect.y < 20) {
                return true;
            }
        }
        return enemies.size == 0;
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
        game.setScreen(epitaleScreen);
        Epitale.character = new Character(28 * 16, 69 * 16);
    }
}