package io.github.amrrokasheh.firstgame;
import io.github.amrrokasheh.firstgame.IntroScreen;

import com.badlogic.gdx.Game;

public class Main extends Game {
    public Player stats;
    public int currentRound = 1;

    @Override
    public void create() {
        stats = new Player();
        setScreen(new IntroScreen(this));
    }
}


