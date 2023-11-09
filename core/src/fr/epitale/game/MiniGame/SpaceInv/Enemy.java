package fr.epitale.game.MiniGame.SpaceInv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    protected Texture texture;
    protected Rectangle rect;
    protected float speed = 50;

    Enemy(float x, float y) {
        texture = new Texture("tiles/tiles_0122.png");
        rect = new Rectangle(x, y, 32, 32);
    }

    void move() {
        rect.y -= speed * Gdx.graphics.getDeltaTime();
    }
}
