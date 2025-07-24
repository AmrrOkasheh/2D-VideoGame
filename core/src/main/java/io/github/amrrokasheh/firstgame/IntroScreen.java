package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.amrrokasheh.firstgame.Main;
import io.github.amrrokasheh.firstgame.OptionsScreen;

public class IntroScreen extends ScreenAdapter {
    private final Main game;
    private Stage stage;

    public IntroScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.font.getData().setScale(2f); // Make it bigger


        Label introLabel = new Label("A   G a m e   M a d e   B y\n\nA m r r   a n d   D a n i e l", labelStyle);
        introLabel.setAlignment(Align.center);

        introLabel.setColor(1, 1, 1, 0); // start transparent
        introLabel.setPosition(
            (Gdx.graphics.getWidth() - introLabel.getWidth()) / 2,
            (Gdx.graphics.getHeight()) / 2
        );

        introLabel.addAction(Actions.sequence(
            Actions.fadeIn(2f),
            Actions.delay(1.5f),
            Actions.fadeOut(2f),
            Actions.run(() -> game.setScreen(new TitleScreen(game)))
        ));

        stage.addActor(introLabel);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
