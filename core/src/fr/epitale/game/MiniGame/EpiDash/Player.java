package fr.epitale.game.MiniGame.EpiDash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {
    private Vector2 velocity = new Vector2();
    private float speed = 40 * 2, gravity = 70 * 1.5f;
    private TiledMapTileLayer collisionLayer;
    public boolean die = false;

    public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        setSize(sprite.getWidth() - 5, sprite.getHeight() - 5);
        this.collisionLayer = collisionLayer;
    }

    public void update(float delta) {
        velocity.y -= gravity * delta;
        if (velocity.y > speed)
            velocity.y = speed;
        else if (velocity.y < -speed)
            velocity.y = -speed;

        float oldX = getX(), oldY = getY(), tilesWidth = collisionLayer.getTileWidth(),
                tilesHeight = collisionLayer.getTileHeight();
        boolean collisionX = false;
        boolean collisionY = false;

        if (velocity.x < 0) {
            // Top left
            Cell cell = collisionLayer.getCell((int) (getX() / tilesWidth),
                    (int) ((getY() + getHeight()) / tilesHeight));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                collisionX = cell.getTile().getProperties().containsKey("wall");
                if (collisionX) {
                    die = true;
                }
                collisionX = cell.getTile().getProperties().containsKey("damage");
                if (collisionX) {
                    die = true;
                }

            }

            // // middle left
            if (!collisionX) {
                cell = collisionLayer.getCell((int) (getX() / tilesWidth),
                        (int) (((getY() + getHeight()) / 2) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionX = cell.getTile().getProperties().containsKey("wall");
                    if (collisionX) {
                        die = true;
                    }
                    collisionX = cell.getTile().getProperties().containsKey("damage");
                    if (collisionX) {
                        die = true;
                    }
                }
            }
            // bottom left
            if (!collisionX) {
                cell = collisionLayer.getCell((int) (getX() / tilesWidth),
                        (int) ((getY()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionX = cell.getTile().getProperties().containsKey("damage");
                    if (collisionX) {
                        die = true;
                    }
                    collisionX = cell.getTile().getProperties().containsKey("wall");

                }
            }

        } else if (velocity.x > 0) {
            // Top right
            Cell cell = collisionLayer.getCell((int) ((getX() + getWidth()) / tilesWidth),
                    (int) ((getY() + getHeight()) / tilesHeight));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                collisionX = cell.getTile().getProperties().containsKey("wall");
                if (collisionX) {
                    die = true;
                }
                collisionX = cell.getTile().getProperties().containsKey("damage");
                if (collisionX) {
                    die = true;
                }
            }

            // // middle right
            if (!collisionX) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth()) / tilesWidth),
                        (int) (((getY() + getHeight()) / 2) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionX = cell.getTile().getProperties().containsKey("wall");
                    if (collisionX) {
                        die = true;
                    }
                    collisionX = cell.getTile().getProperties().containsKey("damage");
                    if (collisionX) {
                        die = true;
                    }
                }

            }

            // // bottom right

            if (!collisionX) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth()) / tilesWidth),
                        (int) ((getY()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionX = cell.getTile().getProperties().containsKey("damage");
                    if (collisionX) {
                        die = true;
                    }
                    collisionX = cell.getTile().getProperties().containsKey("wall");
                }
            }

        }
        if (collisionX) {
            setX(oldX);
            velocity.x = 0;

        }

        setY(getY() + velocity.y * delta);
        if (velocity.y < 0) {
            // bottom left
            Cell cell = collisionLayer.getCell((int) (getX() / tilesWidth), (int) (getY() / tilesHeight));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                collisionY = cell.getTile().getProperties().containsKey("damage");
                if (collisionY) {
                    die = true;
                }
                collisionY = cell.getTile().getProperties().containsKey("wall");

            }

            // Bottom middle
            if (!collisionY) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tilesWidth),
                        (int) ((getY()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionY = cell.getTile().getProperties().containsKey("damage");
                    if (collisionY) {
                        die = true;
                    }
                    collisionY = cell.getTile().getProperties().containsKey("wall");
                }
            }
            // Bottom right
            if (!collisionY) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth()) / tilesWidth),
                        (int) ((getY()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionY = cell.getTile().getProperties().containsKey("damage");
                    if (collisionY) {
                        die = true;
                    }
                    collisionY = cell.getTile().getProperties().containsKey("wall");
                }
            }

        } else if (velocity.y > 0) {
            // // Top left
            Cell cell = collisionLayer.getCell((int) (getX() / tilesWidth),
                    (int) ((getY() + getHeight()) / tilesHeight));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                collisionY = cell.getTile().getProperties().containsKey("wall");
                if (collisionY) {
                    die = true;
                }
                collisionY = cell.getTile().getProperties().containsKey("damage");
                if (collisionY) {
                    die = true;
                }
            }
            // Top middle
            if (!collisionY) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tilesWidth),
                        (int) ((getY() + getHeight()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionY = cell.getTile().getProperties().containsKey("wall");
                    if (collisionY) {
                        die = true;
                    }
                    collisionY = cell.getTile().getProperties().containsKey("damage");
                    if (collisionY) {
                        die = true;
                    }
                }
            }
            // Top right
            if (!collisionY) {
                cell = collisionLayer.getCell((int) ((getX() + getWidth()) / tilesWidth),
                        (int) ((getY() + getHeight()) / tilesHeight));
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties() != null) {
                    collisionY = cell.getTile().getProperties().containsKey("wall");
                    if (collisionY) {
                        die = true;
                    }
                    collisionY = cell.getTile().getProperties().containsKey("damage");
                    if (collisionY) {
                        die = true;
                    }
                }
            }

        }
        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

    }

    @Override
    public void draw(Batch spriteBatch) {
        this.update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void jump() {
        velocity.y = speed * 2f;
        update(Gdx.graphics.getDeltaTime());
    }

    public boolean isGrounded() {
        if (velocity.y == 0) {

            update(Gdx.graphics.getDeltaTime());
            return true;
        }
        return false;
    }

    public void moveRight() {
        velocity.x = speed * Gdx.graphics.getDeltaTime();
        setX(getX() + velocity.x);
        update(Gdx.graphics.getDeltaTime());
        return;
    }

    public void moveLeft() {
        velocity.x = -speed * Gdx.graphics.getDeltaTime();
        setX(getX() + velocity.x);
        update(Gdx.graphics.getDeltaTime());
        return;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

}
