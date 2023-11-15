package fr.epitale.game.Map;

public class JAPEMap extends Map {
    public JAPEMap(Character character) {
        super("minigamejape.tmx", character);
        super.updateZoomFactor(1f);
    }
}
