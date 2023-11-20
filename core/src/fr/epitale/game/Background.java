package fr.epitale.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background implements ApplicationListener {

    private static final int FRAME_COLS = 5, FRAME_ROWS = 52;
    Animation<TextureRegion> walkAnimation;
    Texture walkSheet;
    SpriteBatch spriteBatch;
    protected boolean lastFrame = false;

    float stateTime;

    @Override
    public void create() {

        walkSheet = new Texture(Gdx.files.internal("Menu/BG_Sheet.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];

            }
        }
        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, false);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw current frame
        if (currentFrame == walkAnimation.getKeyFrames()[FRAME_COLS * FRAME_ROWS - 1]) {
            this.lastFrame = true;
        }
        spriteBatch.end();
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        walkSheet.dispose();
    }

    @Override
    public void resize(int width, int height) {
        
        throw new UnsupportedOperationException("Unimplemented method 'resize'");
    }

    @Override
    public void pause() {
        
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }
}