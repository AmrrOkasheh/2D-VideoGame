package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.amrrokasheh.firstgame.Player.UpgradeType;


public class UpgradeScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Label pointsLabel;

    public UpgradeScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Title label
        Label label = new Label("Upgrade time!", skin);
        label.setPosition(100, 380);
        stage.addActor(label);

        // Points label
        pointsLabel = new Label("Upgrade Points: " + game.stats.getUpgradePoints(), skin);
        pointsLabel.setPosition(100, 340);
        stage.addActor(pointsLabel);

        // --- Buttons for upgrades ---
        createUpgradeButton("Increase Health", UpgradeType.HEALTH, 20f, 1, skin, 280);
        createUpgradeButton("Increase Speed", UpgradeType.SPEED, 30f, 1, skin, 220);
        createUpgradeButton("Increase Attack Speed", UpgradeType.ATTACK_SPEED, -0.1f, 1, skin, 160);

        // Continue button (next round)
        TextButton continueButton = new TextButton("Next Round", skin);
        continueButton.setPosition(100, 100);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.currentRound++;
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(continueButton);
    }

    // Helper method to create upgrade buttons
    private void createUpgradeButton(String text, UpgradeType type, float amount, int cost, Skin skin, float y) {
        TextButton button = new TextButton(text, skin);
        button.setPosition(100, y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.stats.upgrade(type, amount, cost)) {
                    pointsLabel.setText("Upgrade Points: " + game.stats.getUpgradePoints());
                }
            }
        });
        stage.addActor(button);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override public void dispose() { stage.dispose(); }
}
