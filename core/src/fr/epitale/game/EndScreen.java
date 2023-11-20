package fr.epitale.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture playButton;
    private Texture exitButton;
    private Texture playButtonHover;
    private Texture exitButtonHover;
    private BitmapFont font;
    private float alpha = 0.0f; // Pour l'effet de fondu

    public EndScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Initialisation des textures (similaire à MenuScreen)
        playButton = new Texture("Menu/play_Button.png");
        exitButton = new Texture("Menu/exit_Button.png");
        playButtonHover = new Texture("Menu/play_Button_Hover.png");
        exitButtonHover = new Texture("Menu/exit_Button_Hover.png");
        final Main gameLogic = game;
        // Créer les boutons
        createButton(playButton, playButtonHover, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2, () -> {
            gameLogic.restartGame();
        }, 200, 175); 
        createButton(exitButton, exitButtonHover, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 200, () -> {
            Gdx.app.exit();
        }, 200, 175);

        font = new BitmapFont(); // Utilise une police par défaut, vous pouvez personnaliser cela
        font.setColor(Color.WHITE); // Définit la couleur du texte
        font.getData().setScale(2); // Ajuste la taille du texte
    }

    private void createButton(Texture texture, Texture textureHover, int x, int y, Runnable action, int width, int height) {
        final Runnable finalAction = action;
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(texture);
        style.imageOver = new TextureRegionDrawable(textureHover); // Utilisation de imageOver pour le hover
    
        ImageButton button = new ImageButton(style);
        button.setPosition(x, y);
        button.setSize(width, height); // Définir la taille réduite ici
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finalAction.run();
            }
        });
    
        stage.addActor(button);
    }
    

    @Override
    public void render(float delta) {
        if (alpha < 1) {
            alpha += delta; // Augmentez la vitesse du fondu si nécessaire
        }

        Gdx.gl.glClearColor(0, 0, 0, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        game.batch.begin();
        font.draw(game.batch, "You Win!", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 200);
        game.batch.end();
    }

    // Implémentez les autres méthodes nécessaires (resize, pause, resume, hide, dispose)
    @Override
    public void dispose() {
        // Disposez de vos ressources ici
        stage.dispose();
        playButton.dispose();
        exitButton.dispose();
        playButtonHover.dispose();
        exitButtonHover.dispose();
    }

    @Override
    public void show() {
        
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
}
