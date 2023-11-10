package fr.epitale.game.Map;
public class Character {
    private float x;
    private float y;
    private boolean isVisible = true;

    private float speed;

    public Character(float initialX, float initialY) {
        x = initialX;
        y = initialY;
        speed = 1.5f;
    }

    public void move(float deltaX, float deltaY) {
        x += deltaX * speed;
        y += deltaY * speed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public float getSpeed() {
        return speed;
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean newVisible) {
        isVisible = newVisible;
    }

}