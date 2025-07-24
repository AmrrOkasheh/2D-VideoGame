package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        this.setScreen(new TitleScreen(this)); // Pass instance of Main to TitleScreen
    }
}
