package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TitleScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Texture swordTexture;
    private Image swordImage;
    private TextButton startButton;
    private TextButton optionsButton;

    // Camera + Viewport
    private OrthographicCamera camera;
    private Viewport viewport;

    // Fixed virtual resolution
    private static final int VIRTUAL_WIDTH = 960;
    private static final int VIRTUAL_HEIGHT = 540;

    public TitleScreen(Main game) {
        this.game = game;

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Load skin and assets
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        swordTexture = new Texture(Gdx.files.internal("Pixel Sword.png"));

        // Sword Image
        swordImage = new Image(swordTexture);
        swordImage.setSize(128, 128);
        swordImage.setPosition(
            (VIRTUAL_WIDTH - swordImage.getWidth()) / 2f,
            (VIRTUAL_HEIGHT + 50) / 2f
        );
        stage.addActor(swordImage);

        // Start Button
        startButton = new TextButton("Start Game", skin);
        startButton.setSize(200, 60);
        startButton.setPosition(
            (VIRTUAL_WIDTH - startButton.getWidth()) / 2f,
            (VIRTUAL_HEIGHT - 60) / 2f - 80
        );
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start button clicked!");
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(startButton);

        // Options Button (below start)
        optionsButton = new TextButton("Options", skin);
        optionsButton.setSize(200, 60);
        optionsButton.setPosition(
            (VIRTUAL_WIDTH - optionsButton.getWidth()) / 2f,
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
        Gdx.gl.glClearColor(0, 0, 0, 1); // black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // handles auto-resizing
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        swordTexture.dispose();
    }
}
