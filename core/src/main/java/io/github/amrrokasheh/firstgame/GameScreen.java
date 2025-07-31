package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture weaponTexture;

    // Bullet system
    private Texture bulletTexture;
    private ArrayList<Bullet> bullets;
    private float shootCooldown = 0f;

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
        bulletTexture = new Texture(Gdx.files.internal("bullet.png")); 

        bullets = new ArrayList<>();

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
        shootCooldown -= delta; // ⏱ cooldown timer
        handleInput(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerTexture, playerX, playerY);
        batch.draw(weaponTexture, playerX + 24, playerY + 8);

        // 🔫 Render bullets
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }

        batch.end();

        // 🔄 Update bullets
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update(delta);
            if (bullet.isOutOfBounds(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)) {
                bulletIterator.remove();
            }
        }

        // --- Wave check ---
        if (!waveComplete) {
            roundEndTimer += delta;
            if (roundEndTimer == -5f) {
                waveComplete = true;
                game.stats.addUpgradePoints(1);
                game.setScreen(new UpgradeScreen(game));
            }
        }
    }

    private void handleInput(float delta) {
        float playerSpeed = game.stats.getSpeed();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) playerY += playerSpeed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) playerY -= playerSpeed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) playerX -= playerSpeed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) playerX += playerSpeed * delta;

        float maxX = VIRTUAL_WIDTH - TILE_SIZE;
        float maxY = VIRTUAL_HEIGHT - TILE_SIZE;
        playerX = Math.max(0, Math.min(playerX, maxX));
        playerY = Math.max(0, Math.min(playerY, maxY));

        // 🖱 Left-click to shoot
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootCooldown <= 0) {
            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldMousePos = viewport.unproject(mousePos);

            float playerCenterX = playerX + playerTexture.getWidth() / 2f;
            float playerCenterY = playerY + playerTexture.getHeight() / 2f;

            float dirX = worldMousePos.x - playerCenterX;
            float dirY = worldMousePos.y - playerCenterY;

            Bullet newBullet = new Bullet(playerCenterX, playerCenterY, dirX, dirY, bulletTexture);
            bullets.add(newBullet);
            shootCooldown = 0.01f;    //game.stats.getAttackSpeed();
        }
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
        bulletTexture.dispose(); // 🧹 bullet texture
    }
}
