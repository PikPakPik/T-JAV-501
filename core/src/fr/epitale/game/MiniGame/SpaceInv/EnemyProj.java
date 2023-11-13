package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class EnemyProj {
    protected Texture texture;
    protected Rectangle rect;
    protected float speed = 300;

    public EnemyProj(float x, float y) {
        texture = new Texture("Tiles/tile_0130.png");
        rect = new Rectangle(x, y, 16, 16);
    }

    public void move() {
        rect.y -= speed * Gdx.graphics.getDeltaTime();
    }
    public boolean collidesWith(Rectangle otherRect) {
        return this.rect.overlaps(otherRect);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public void dispose() {
        texture.dispose();
    }
}
