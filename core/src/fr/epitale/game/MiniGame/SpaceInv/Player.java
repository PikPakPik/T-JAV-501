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

    public Player() {
        texture = new Texture("Tiles/tile_0085.png");
        rect = new Rectangle((float) 800 / 2 - (float) 64 / 2, 20, 64, 64);
        playerProjs = new Array<>();
    }

    public void moveLeft() {
        rect.x -= 200 * Gdx.graphics.getDeltaTime();
        keepPlayerIn();
    }

    public void moveRight() {
        rect.x += 200 * Gdx.graphics.getDeltaTime();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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

    public void disposePlayerProjs() {
        for (PlayerProj playerProj : playerProjs) {
            playerProj.dispose();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public void dispose() {
        texture.dispose();
    }

}
