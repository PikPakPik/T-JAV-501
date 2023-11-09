package fr.epitale.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuScreen implements Screen {
    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 70;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 200;
    private static final int LOGO_WIDTH = 500;
    private static final int LOGO_HEIGHT = 250;

    Main game;
    Texture logo;
    Texture playButton;
    Texture exitButton;
    Animation<TextureRegion> background;
    float elapsed;

    public MenuScreen(Main game) {
        this.game = game;
        logo = new Texture("Menu/Game_Logo.png");
        playButton = new Texture("Menu/play_Button.png");
        exitButton = new Texture("Menu/exit_Button.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(logo, (game.WIDTH
                / 2 - this.LOGO_WIDTH / 2), game.HEIGHT - 300,
                LOGO_WIDTH, LOGO_HEIGHT);

        game.batch.draw(playButton, (game.WIDTH / 2 - this.PLAY_BUTTON_WIDTH / 2), this.PLAY_BUTTON_Y,
                PLAY_BUTTON_WIDTH,
                PLAY_BUTTON_HEIGHT);
        game.batch.draw(exitButton, (game.WIDTH / 2 - this.EXIT_BUTTON_WIDTH / 2), this.EXIT_BUTTON_Y,
                EXIT_BUTTON_WIDTH,
                EXIT_BUTTON_HEIGHT);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

}
