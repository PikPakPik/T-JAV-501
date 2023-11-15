package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

public class Enemy {

    protected Texture texture;
    protected Rectangle rect;
    protected float speed = 100;
    protected static final int ENEMY_ROWS = 5;
    protected static final int ENEMY_COLS = 10;
    protected static final float ENEMY_SPACING = 50;
    protected Array<EnemyProj> enemyProjs;
    private float shootTimer;
    private static final float SHOOT_INTERVAL = 0.2f;

    public Enemy(float x, float y) {
        texture = new Texture("Tiles/tile_0122.png");
        rect = new Rectangle(x, y, 16, 16);
        enemyProjs = new Array<>();
        shootTimer = 0.0f;
    }

    public void move(boolean moveEnemiesRight, float delta) {
        if (moveEnemiesRight) {
            rect.x -= speed * Gdx.graphics.getDeltaTime();
        } else {
            rect.x += speed * Gdx.graphics.getDeltaTime();
        }
        for (EnemyProj enemyProj : enemyProjs) {
            enemyProj.move();
        }

        shootTimer += delta;

        if (shootTimer >= SHOOT_INTERVAL) {
            if (MathUtils.random(0, 100) == 0) {
                shoot();
            }
            shootTimer = 0.0f;
        }
    }

    public void moveDown() {
        rect.y -= 30;
    }
    public void shoot() {
        EnemyProj enemyProj = new EnemyProj(rect.x + rect.width / 2 - 4, rect.y - rect.height);
        enemyProjs.add(enemyProj);
    }
    public void moveProjs() {
        for (EnemyProj enemyProj : enemyProjs) {
            enemyProj.move();
        }

        for (int i = enemyProjs.size - 1; i >= 0; i--) {
            EnemyProj playerProj = enemyProjs.get(i);
            if (playerProj.rect.y > 600) {
                enemyProjs.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
        for (EnemyProj enemyProj : enemyProjs) {
            enemyProj.render(batch);
        }
    }

    public void dispose() {
        texture.dispose();
        for (EnemyProj enemyProj : enemyProjs) {
            enemyProj.dispose();
        }
    }
}
