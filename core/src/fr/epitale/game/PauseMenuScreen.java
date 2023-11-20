package fr.epitale.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseMenuScreen implements Screen {
    private static final int EXIT_BUTTON_WIDTH = 500;
    private static final int EXIT_BUTTON_HEIGHT = 240;
    public static final int EXIT_BUTTON_Y = 100;

    Main game;
    Texture playButton;
    Texture exitButton;
    Texture bgImg;
    Background background;

    private final Stage stage;

    public PauseMenuScreen(final Main game, final Background background) {
        this.game = game;
        this.background = background;
        playButton = new Texture("Menu/play_Button.png");
        exitButton = new Texture("Menu/exit_Button.png");
        bgImg = new Texture("Menu/bg-img-pause.png");

        stage = new Stage(new ScreenViewport());
        exitBtn();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void exitBtn() {
        ImageButton.ImageButtonStyle styleExitbtn = new ImageButton.ImageButtonStyle();
        styleExitbtn.imageUp = new TextureRegionDrawable(exitButton);
        ImageButton myExitBtn = new ImageButton(styleExitbtn);
        myExitBtn.setPosition((float) (Gdx.graphics.getWidth() - EXIT_BUTTON_WIDTH) / 2, (float) (Gdx.graphics.getHeight() - EXIT_BUTTON_Y) / 2 + EXIT_BUTTON_Y);
        myExitBtn.setSize(EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        myExitBtn.addListener(new ClickListener() {
            ImageButton myExitBtn;
            public ClickListener init(ImageButton myExitBtn) {
                this.myExitBtn = myExitBtn;
                return this;
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

        }.init(myExitBtn));

        stage.addActor(myExitBtn);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        Gdx.input.setInputProcessor(stage);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getPreviousScreen());
        }
        stage.act(delta);
        stage.draw();

        game.batch.end();
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

    }

}
