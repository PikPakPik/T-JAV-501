package fr.epitale.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.MiniGame.EpiDash.EpiDash;
import fr.epitale.game.Map.Character;

public class Main extends Game {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public SpriteBatch batch;
	Background background;
	private Screen previousScreen;
	private InputProcessor previousInputProcessor;

	@Override
	public void create() {
		// batch = new SpriteBatch();
		// background = new Background();
		// background.create();
		// this.setScreen(new MenuScreen(this, background));
		setScreen(new EpiDash(this, new Character(0, 0), new Epitale(this)));

	}

	public void restartGame() {
		getScreen().dispose();

		this.setScreen(new MenuScreen(this, background));
	}

	public void setPreviousScreen(Screen screen) {
		this.previousScreen = screen;
	}

	public Screen getPreviousScreen() {
		return previousScreen;
	}

	public void setPreviousInputProcessor(InputProcessor inputProcessor) {
		this.previousInputProcessor = inputProcessor;
	}

	public InputProcessor getPreviousInputProcessor() {
		return previousInputProcessor;
	}
}