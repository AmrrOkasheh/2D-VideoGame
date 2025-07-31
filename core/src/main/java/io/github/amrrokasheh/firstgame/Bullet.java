package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public float x, y;
    private float speed = 600f;
    private float directionX, directionY;
    private Texture texture;
    private Rectangle bounds;
    private float rotation; // angle in degrees

    public Bullet(float x, float y, float dirX, float dirY, Texture texture) {
        this.x = x;
        this.y = y;

        Vector2 dir = new Vector2(dirX, dirY).nor();
        this.directionX = dir.x;
        this.directionY = dir.y;

        this.rotation = dir.angleDeg(); // calculate angle to face direction

        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float delta) {
        x += directionX * speed * delta;
        y += directionY * speed * delta;
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        float originX = texture.getWidth() / 2f;
        float originY = texture.getHeight() / 2f;

        batch.draw(
            texture,
            x - originX, y - originY,               // draw from center
            originX, originY,                       // origin for rotation
            texture.getWidth(), texture.getHeight(), // size
            1f, 1f,                                 // scale
            rotation,                               // rotation in degrees
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false
        );
    }

    public boolean isOutOfBounds(float width, float height) {
        return x < 0 || y < 0 || x > width || y > height;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
