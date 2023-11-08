package fr.epitale.game;
public class Character {
    private float x;
    private float y;

    private float speed;

    public Character(float initialX, float initialY) {
        x = initialX;
        y = initialY;
        speed = 1;
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

}
