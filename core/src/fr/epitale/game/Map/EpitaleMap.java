package fr.epitale.game.Map;

public class EpitaleMap extends Map {
    public EpitaleMap(Character character) {
        super("epitales-map.tmx", character);
        super.updateZoomFactor(0.3f);
    }
}
