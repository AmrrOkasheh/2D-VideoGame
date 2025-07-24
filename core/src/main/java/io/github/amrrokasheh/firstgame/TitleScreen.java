package io.github.amrrokasheh.firstgame;

import io.github.amrrokasheh.firstgame.TitleScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class TitleScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Texture swordTexture;
    private Image swordImage;
    private TextButton startButton;
    private TextButton optionsButton;

    public TitleScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        swordTexture = new Texture(Gdx.files.internal("Pixel Sword.png"));
        swordImage = new Image(swordTexture);
        swordImage.setSize(128, 128);
        swordImage.setPosition(
            (Gdx.graphics.getWidth() - swordImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() + 50) / 2
        );
        stage.addActor(swordImage);

        startButton = new TextButton("Start Game", skin);
        startButton.setSize(200, 60);
        startButton.setPosition(
            (Gdx.graphics.getWidth() - startButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - 60) / 2 - 80
        );
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start button clicked!");
                game.setScreen(new GameScreen(game)); // Switch to GameScreen
            }
        });
        stage.addActor(startButton);
        //
        optionsButton = new TextButton("Options", skin);
        optionsButton.setSize(200, 60);
        optionsButton.setPosition(
            (Gdx.graphics.getWidth() - optionsButton.getWidth()) / 2,
            startButton.getY() - 80
        );
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Options button clicked!");
                game.setScreen(new OptionsScreen(game));
            }
        });
        stage.addActor(optionsButton);

    }

    @Override
    public void show() {
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction(Actions.fadeIn(1f));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        swordTexture.dispose();
    }
}
