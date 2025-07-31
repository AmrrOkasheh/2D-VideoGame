package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Texture texture;
    private Vector2 position;
    private float speed;
    private Rectangle bounds;

    public Enemy(String texturePath, float x, float y, float speed) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float delta) {
        position.y -= speed * delta;
        bounds.setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isOffScreen() {
        return position.y + texture.getHeight() < 0;
    }

    public void dispose() {
        texture.dispose();
    }
}
