package fr.epitale.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.epitale.game.Map.Epitale;
import fr.epitale.game.MiniGame.EpiDash.EpiDash;

public class Main extends Game {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;


	public SpriteBatch batch;
	Background background;

	@Override
	public void create() {
		// batch = new SpriteBatch();
		// background = new Background();
		// background.create();
		// this.setScreen(new MenuScreen(this, background));
		setScreen(new EpiDash());

	}
	public void restartGame() {
		getScreen().dispose();

		this.setScreen(new MenuScreen(this, background));
	}
}