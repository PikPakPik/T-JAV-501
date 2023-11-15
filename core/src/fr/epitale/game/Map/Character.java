package fr.epitale.game.Map;

public class Character {
    private float x;
    private float y;
    private final boolean isVisible;
    private final float speed;

    public Character(float initialX, float initialY) {
        this.x = initialX;
        this.y = initialY;
        this.isVisible = true;
        this.speed = 1f;
    }

    public void move(float deltaX, float deltaY) {
        this.x += deltaX * this.speed;
        this.y += deltaY * this.speed;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setX(float newX) {
        this.x = newX;
    }

    public void setY(float newY) {
        this.y = newY;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setPos(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }
}