package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        // Use a Table for layout
        Table table = new Table();
        table.setFillParent(true); // fills the whole screen
        table.center();
        stage.addActor(table);

        pointsLabel = new Label("Upgrade Points: " + game.stats.getUpgradePoints(), skin);

        // --- Buttons ---
        TextButton healthButton = new TextButton("Increase Health", skin);
        healthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.stats.upgrade(UpgradeType.HEALTH, 20f, 1)) updatePointsLabel();
            }
        });

        TextButton speedButton = new TextButton("Increase Speed", skin);
        speedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.stats.upgrade(UpgradeType.SPEED, 30f, 1)) updatePointsLabel();
            }
        });

        TextButton attackButton = new TextButton("Increase Attack Speed", skin);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.stats.upgrade(UpgradeType.ATTACK_SPEED, -0.1f, 1)) updatePointsLabel();
            }
        });

        TextButton continueButton = new TextButton("Next Round", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen gameScreen = game.getGameScreen();
                gameScreen.nextRound();
                game.setScreen(gameScreen);
            }
        });

        // --- Add everything to table ---
        table.add(pointsLabel).padBottom(20).colspan(2).row();
        table.add(healthButton).width(200).padBottom(10).row();
        table.add(speedButton).width(200).padBottom(10).row();
        table.add(attackButton).width(200).padBottom(20).row();
        table.add(continueButton).width(200);
    }

    private void updatePointsLabel() {
        pointsLabel.setText("Upgrade Points: " + game.stats.getUpgradePoints());
    }

    @Override
    public void show() { Gdx.input.setInputProcessor(stage); }
    @Override
    public void render(float delta) { stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); stage.draw(); }
    @Override
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { Gdx.input.setInputProcessor(null); }
    @Override public void dispose() { stage.dispose(); }
}
