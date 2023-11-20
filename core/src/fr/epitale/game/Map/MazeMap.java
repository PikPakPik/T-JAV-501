package fr.epitale.game.Map;

public class MazeMap extends Map {
    public MazeMap(Character character) {
        super("maze.tmx", character);
        super.updateZoomFactor(0.25f);
    }
}
