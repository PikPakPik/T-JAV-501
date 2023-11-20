package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import fr.epitale.game.Main;
import fr.epitale.game.Map.Character;
import fr.epitale.game.Map.Epitale;

public class SpaceInvScreen implements Screen {
    protected final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private Array<Enemy> enemies;
    private boolean moveEnemiesRight = true;
    private Epitale epitaleScreen;
    private boolean gameOverLose = false;
    private long gameOverStartTime;
    private com.badlogic.gdx.audio.Music spaceInvMusic;
    private com.badlogic.gdx.audio.Music gameOverMusic;
    private Texture backgroundTexture;
    private Texture gameOverTexture;


    public SpaceInvScreen(final Main game, Character character, Epitale epitaleScreen) {
        this.game = game;
        this.epitaleScreen = epitaleScreen;
        initialize();
    }

    private void initialize() {
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

        backgroundTexture = new Texture("Tiles/tile_0049.png");
        gameOverTexture = new Texture("gameover.jpg");

        spaceInvMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/SpaceInvaders/spaceinvaders.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/loosemusic.mp3"));
        spaceInvMusic.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        handleInput();
        moveEnemies();
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

        if (isGameOverWin()) {
            spaceInvMusic.stop();
            game.setScreen(epitaleScreen);
            Epitale.character = new Character(28 * 16, 69 * 16);
        } else if (isGameOverLose()) {
            if (!gameOverLose) {
                gameOverLose = true;
                gameOverStartTime = TimeUtils.millis();
            }
            long elapsedTime = TimeUtils.timeSinceMillis(gameOverStartTime);
            if (elapsedTime > 5000) {
                ((Main) Gdx.app.getApplicationListener()).restartGame();
                gameOverLose = false;
            } else {
                spaceInvMusic.stop();
                gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/loosemusic.mp3"));
                gameOverMusic.play();
                batch.draw(gameOverTexture, 0, 0, 800, 600);
            }
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
    private boolean isGameOverWin() {
        return enemies.size == 0;
    }
    private boolean isGameOverLose() {
        for (Enemy enemy : enemies) {
            if (enemy.rect.y < 20) {
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
        batch.dispose();
        backgroundTexture.dispose();
        gameOverTexture.dispose();
        spaceInvMusic.dispose();
        gameOverMusic.dispose();
        game.setScreen(epitaleScreen);
    }
}