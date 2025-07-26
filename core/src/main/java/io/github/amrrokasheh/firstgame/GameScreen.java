package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private Texture playerTexture;

    // Camera + Viewport
    private OrthographicCamera camera;
    private Viewport viewport;

    // Virtual resolution (fixed game size)
    private static final int VIRTUAL_WIDTH = 960;
    private static final int VIRTUAL_HEIGHT = 540;

    // Player state
    private float playerX, playerY;
    private float speed = 150f; // pixels per second

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load 32x32 player image from assets/
        playerTexture = new Texture(Gdx.files.internal("player.png"));

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();

        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        // Center player
        playerX = (VIRTUAL_WIDTH - 32) / 2f;
        playerY = (VIRTUAL_HEIGHT - 32) / 2f;
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        // Clear screen to white
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set camera before drawing
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerTexture, playerX, playerY);
        batch.end();
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) playerY += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) playerY -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) playerX -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) playerX += speed * delta;

        // Keep player inside screen
        float maxX = VIRTUAL_WIDTH - 32;
        float maxY = VIRTUAL_HEIGHT - 32;
        playerX = Math.max(0, Math.min(playerX, maxX));
        playerY = Math.max(0, Math.min(playerY, maxY));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // <-- this handles resizing!
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
    }
}
