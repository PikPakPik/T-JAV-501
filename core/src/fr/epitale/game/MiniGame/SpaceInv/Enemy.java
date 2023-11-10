package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

    protected Texture texture;
    protected Rectangle rect;
    protected float speed = 100;
    protected static final int ENEMY_ROWS = 5;
    protected static final int ENEMY_COLS = 10;
    protected static final float ENEMY_SPACING = 50;

    public Enemy(float x, float y) {
        texture = new Texture("Tiles/tile_0122.png");
        rect = new Rectangle(x, y, 16, 16);
    }

    public void move(boolean moveEnemiesRight) {
        if (moveEnemiesRight) {
            rect.x -= speed * Gdx.graphics.getDeltaTime();
        } else {
            rect.x += speed * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveDown() {
        rect.y -= 30;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public void dispose() {
        texture.dispose();
    }
}
