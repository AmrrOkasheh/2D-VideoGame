// Source code is decompiled from a .class file using FernFlower decompiler.
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
    private Texture weaponTexture;

    // Camera Variables
    private OrthographicCamera camera;
    private Viewport viewport;

    // Map Size Variables
    private static final int TILE_SIZE = 32;
    private static final int TILES_WIDE = 20;
    private static final int TILES_TALL = 12;
    private static final int VIRTUAL_WIDTH = TILE_SIZE * TILES_WIDE;   // 640
    private static final int VIRTUAL_HEIGHT = TILE_SIZE * TILES_TALL; // 384

    // Player Position
    private float playerX;
    private float playerY;

    // Round Variables
    private int currentRound = 1;
    private boolean roundOver = false;
    private float roundEndTimer = 0f;
    boolean waveComplete = false;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        playerTexture = new Texture(Gdx.files.internal("amrr.png"));
        weaponTexture = new Texture(Gdx.files.internal("drac.png"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        playerX = (VIRTUAL_WIDTH - TILE_SIZE) / 2f;
        playerY = (VIRTUAL_HEIGHT - TILE_SIZE) / 2f;
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerTexture, playerX, playerY);
        batch.draw(weaponTexture, playerX + 24, playerY + 8);
        batch.end();

        // --- Wave check ---
        if (!waveComplete) {
            roundEndTimer += delta;
            if (roundEndTimer >= 5f) {
                waveComplete = true;
                game.stats.addUpgradePoints(1);
                game.setScreen(new UpgradeScreen(game));
            }
        }
    }

    private void handleInput(float delta) {
        // ✅ Pull current speed from stats
        float playerSpeed = game.stats.getSpeed();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += playerSpeed * delta;
        }

        float maxX = VIRTUAL_WIDTH - TILE_SIZE;
        float maxY = VIRTUAL_HEIGHT - TILE_SIZE;
        playerX = Math.max(0, Math.min(playerX, maxX));
        playerY = Math.max(0, Math.min(playerY, maxY));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        weaponTexture.dispose();
    }
}
