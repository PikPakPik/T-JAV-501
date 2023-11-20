package fr.epitale.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.epitale.game.Map.Epitale;

public class PauseMenuScreen implements Screen {
    private static final int EXIT_BUTTON_WIDTH = 500;
    private static final int EXIT_BUTTON_HEIGHT = 240;
    public static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_WIDTH = 600;
    private static final int PLAY_BUTTON_HEIGHT = 240;
    private static final int PLAY_BUTTON_Y = 500;
    private static final int LOGO_WIDTH = 600;
    private static final int LOGO_HEIGHT = 250;

    Main game;
    Texture logo;
    Texture playButton;
    Texture exitButton;
    Texture playButtonHover;
    Texture exitButtonHover;
    Background background;

    private Stage stage;

    public PauseMenuScreen(final Main game, final Background background) {
        this.game = game;
        this.background = background;
        logo = new Texture("Menu/Game_Logo.png");
        playButton = new Texture("Menu/play_Button.png");
        exitButton = new Texture("Menu/exit_Button.png");
        playButtonHover = new Texture("Menu/play_Button_Hover.png");
        exitButtonHover = new Texture("Menu/exit_Button_Hover.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        playBtnLogicV2();
        exitBtnLogic();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    public void playBtnLogicV2() {

        ImageButton.ImageButtonStyle stylePlaybtn = new ImageButton.ImageButtonStyle();
        stylePlaybtn.imageUp = new TextureRegionDrawable(new TextureRegion(playButton));

        ImageButton myPlayBtn = new ImageButton(stylePlaybtn);
        myPlayBtn.setPosition((stage.getWidth() - PLAY_BUTTON_WIDTH) / 2, (stage.getHeight()- (float) PLAY_BUTTON_Y) /2);
        myPlayBtn.setSize(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        myPlayBtn.addListener(new ClickListener() {
            ImageButton myPlayBtn;

            public ClickListener init(ImageButton myPlayBtn) {
                this.myPlayBtn = myPlayBtn;
                return this;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                myPlayBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(playButtonHover));
                super.enter(event, x, y, pointer, fromActor);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                myPlayBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(playButton));
                super.exit(event, x, y, pointer, toActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Epitale(game));
                dispose();
            }
        }.init(myPlayBtn));

        stage.addActor(myPlayBtn);
    }

    public void exitBtnLogic() {
        ImageButton.ImageButtonStyle styleExitbtn = new ImageButton.ImageButtonStyle();
        styleExitbtn.imageUp = new TextureRegionDrawable(new TextureRegion(exitButton));

        ImageButton myExitBtn = new ImageButton(styleExitbtn);
        myExitBtn.setPosition((stage.getWidth() - EXIT_BUTTON_WIDTH) / 2, (stage.getHeight() - (float) EXIT_BUTTON_Y) / 2 + EXIT_BUTTON_Y);
        myExitBtn.setSize(EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        myExitBtn.addListener(new ClickListener() {
            ImageButton myExitBtn;

            public ClickListener init(ImageButton myExitBtn) {
                this.myExitBtn = myExitBtn;
                return this;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // TODO Auto-generated method stub
                myExitBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(exitButtonHover));

                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // TODO Auto-generated method stub
                myExitBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(exitButton));

                super.exit(event, x, y, pointer, toActor);

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

        background.render();
        game.batch.begin();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getPreviousScreen());
            Gdx.input.setInputProcessor(game.getPreviousInputProcessor());
        }
        if (background.lastFrame) {
            game.batch.draw(logo, (Main.WIDTH / 3 - LOGO_WIDTH / 2), Main.HEIGHT - 300,
                    LOGO_WIDTH, LOGO_HEIGHT);
            stage.act(delta);
            stage.draw();
            stage.act(delta);
        }
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
        dispose();
    }

    @Override
    public void dispose() {
    }
}