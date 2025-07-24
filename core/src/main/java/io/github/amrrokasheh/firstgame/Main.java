package io.github.amrrokasheh.firstgame;
import io.github.amrrokasheh.firstgame.IntroScreen;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new IntroScreen(this));
    }

}

