package fr.epitale.game;

import com.badlogic.gdx.Game;
import fr.epitale.game.Map.Epitale;

public class Main extends Game {

	@Override
	public void create() {
		setScreen(new Epitale(this));
	}
}