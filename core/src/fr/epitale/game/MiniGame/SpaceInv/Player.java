package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {

    private final Texture texture;
    private final Rectangle rect;
    protected final Array<PlayerProj> playerProjs;
    private float shootingCooldown = 0;
    private static final float SHOOTING_COOLDOWN_DURATION = 3;
    private boolean isPlayerVisible = true;
    private float blinkTimer = 0.0f;
    private static final float BLINK_INTERVAL = 0.1f;

    public Player() {
        texture = new Texture("Tiles/tile_0085.png");
        rect = new Rectangle((float) 800 / 2 - (float) 64 / 2, 20, 16, 16);
        playerProjs = new Array<>();
    }

    public void moveLeft() {
        rect.x -= 300 * Gdx.graphics.getDeltaTime();
        keepPlayerIn();
    }

    public void moveRight() {
        rect.x += 300 * Gdx.graphics.getDeltaTime();
        keepPlayerIn();
    }

    private void keepPlayerIn() {
        if (rect.x < 0) {
            rect.x = 0;
        }
        if (rect.x > 800 - 16) {
            rect.x = 800 - 16;
        }
    }

    public void shoot() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && shootingCooldown <= 0) {
            PlayerProj playerProj = new PlayerProj(rect.x, rect.y);
            playerProjs.add(playerProj);
        }
    }

    public void movePlayerProjs() {
        for (PlayerProj playerProj : playerProjs) {
            playerProj.move();
        }

        for (int i = playerProjs.size - 1; i >= 0; i--) {
            PlayerProj playerProj = playerProjs.get(i);
            if (playerProj.rect.y > 600) {
                playerProjs.removeIndex(i);
            }
        }
    }

    public void renderPlayerProjs(SpriteBatch batch) {
        for (PlayerProj playerProj : playerProjs) {
            playerProj.render(batch);
        }
    }

    public void checkEnemyProjectileCollision(Array<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            for (EnemyProj enemyProj : enemy.enemyProjs) {
                if (enemyProj.collidesWith(rect)) {
                    onPlayerHit();
                }
            }
        }
    }
    private void onPlayerHit() {
        shootingCooldown = SHOOTING_COOLDOWN_DURATION;
    }

    public void update(float delta, Array<Enemy> enemies) {
        shootingCooldown = Math.max(0, shootingCooldown - delta);

        checkEnemyProjectileCollision(enemies);
        movePlayerProjs();
    }

    public void render(SpriteBatch batch) {
        if (shootingCooldown > 0) {
            blinkTimer += Gdx.graphics.getDeltaTime();
            if (blinkTimer >= BLINK_INTERVAL) {
                isPlayerVisible = !isPlayerVisible;
                blinkTimer = 0.0f;
            }

            if (isPlayerVisible) {
                batch.draw(texture, rect.x, rect.y);
            }
        } else {
            batch.draw(texture, rect.x, rect.y);
        }
    }

    public void dispose() {
        texture.dispose();
    }

}
