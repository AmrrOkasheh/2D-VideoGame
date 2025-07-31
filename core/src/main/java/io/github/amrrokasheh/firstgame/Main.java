package io.github.amrrokasheh.firstgame;

import com.badlogic.gdx.Game;

public class Main extends Game {
    public Player stats;
    public int currentRound = 1;

    private GameScreen gameScreen; // keep one instance

    @Override
    public void create() {
        stats = new Player();
        gameScreen = new GameScreen(this); // initialize once
        setScreen(new IntroScreen(this));
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
