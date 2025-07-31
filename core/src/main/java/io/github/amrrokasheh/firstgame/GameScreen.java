package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    // Bullets
    private Texture bulletTexture;
    private ArrayList<Bullet> bullets;
    private float shootCooldown = 0f;

    // Enemies
    private ArrayList<Enemy> enemies;
    private float spawnTimer;
    private int enemiesSpawnedThisRound;
    private int enemiesToSpawn;

    // Camera
    private OrthographicCamera camera;
    private Viewport viewport;

    // HUD
    private BitmapFont font;

    // Map size
    private static final int TILE_SIZE = 32;
    private static final int TILES_WIDE = 20;
    private static final int TILES_TALL = 12;
    private static final int VIRTUAL_WIDTH = TILE_SIZE * TILES_WIDE;   // 640
    private static final int VIRTUAL_HEIGHT = TILE_SIZE * TILES_TALL; // 384

    // Player pos
    private float playerX;
    private float playerY;

    // Round
    private int currentRound = 1;
    private boolean waveComplete = false;
    private float roundEndTimer = 0f;

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
        enemies = new ArrayList<>();

        spawnTimer = 0f;
        enemiesSpawnedThisRound = 0;
        enemiesToSpawn = 5;

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        playerX = (VIRTUAL_WIDTH - TILE_SIZE) / 2f;
        playerY = (VIRTUAL_HEIGHT - TILE_SIZE) / 2f;

        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    @Override
    public void render(float delta) {
        shootCooldown -= delta;
        spawnTimer += delta;
        handleInput(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerTexture, playerX, playerY);
        batch.draw(weaponTexture, playerX + 24, playerY + 8);

        // Bullets
        for (Bullet bullet : bullets) bullet.render(batch);

        // Enemies
        for (Enemy enemy : enemies) enemy.render(batch);

        // HUD
        font.draw(batch, "Round: " + currentRound, 10, VIRTUAL_HEIGHT - 10);
        font.draw(batch, "Health: " + game.stats.getHealth(), 10, VIRTUAL_HEIGHT - 30);
        font.draw(batch, "Upgrade Points: " + game.stats.getUpgradePoints(), 10, VIRTUAL_HEIGHT - 50);

        batch.end();

        // Update bullets
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update(delta);
            if (bullet.isOutOfBounds(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)) bulletIterator.remove();
        }

        // Spawn enemies
        if (spawnTimer > 2f && enemiesSpawnedThisRound < enemiesToSpawn) {
            enemies.add(new Enemy("EnemyMob1.png",
                (float) Math.random() * (VIRTUAL_WIDTH - TILE_SIZE),
                VIRTUAL_HEIGHT, 100));
            enemiesSpawnedThisRound++;
            spawnTimer = 0f;
        }

        // Update enemies
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(delta);
            if (enemy.isOffScreen()) enemyIterator.remove();
        }

        // Round check
        if (!waveComplete) {
            if (enemiesSpawnedThisRound >= enemiesToSpawn && enemies.isEmpty()) {
                roundEndTimer += delta;
                if (roundEndTimer >= 3f) {
                    waveComplete = true;
                    game.stats.addUpgradePoints(1);
                    game.setScreen(new UpgradeScreen(game));
                }
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

        // Shoot
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootCooldown <= 0) {
            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldMousePos = viewport.unproject(mousePos);

            float playerCenterX = playerX + playerTexture.getWidth() / 2f;
            float playerCenterY = playerY + playerTexture.getHeight() / 2f;

            float dirX = worldMousePos.x - playerCenterX;
            float dirY = worldMousePos.y - playerCenterY;

            Bullet newBullet = new Bullet(playerCenterX, playerCenterY, dirX, dirY, bulletTexture);
            bullets.add(newBullet);
            shootCooldown = game.stats.getAttackSpeed();
        }
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        weaponTexture.dispose();
        bulletTexture.dispose();
        font.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
    }

    // Advance to next round
    public void nextRound() {
        currentRound++;
        waveComplete = false;
        roundEndTimer = 0f;
        enemiesSpawnedThisRound = 0;
        enemiesToSpawn = 5 + (currentRound - 1) * 3;
        spawnTimer = 0f;
        playerX = (VIRTUAL_WIDTH - TILE_SIZE) / 2f;
        playerY = (VIRTUAL_HEIGHT - TILE_SIZE) / 2f;
    }
}
