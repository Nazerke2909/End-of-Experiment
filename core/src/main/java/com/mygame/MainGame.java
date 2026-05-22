package com.mygame;
import com.badlogic.gdx.Game;
import com.mygame.view.FirstScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
