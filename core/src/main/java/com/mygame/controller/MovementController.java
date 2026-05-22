package com.mygame.controller;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MovementController {

    public interface MovementCommand {
        void execute(float delta);
    }

    public class MoveUpCommand implements MovementCommand {
        @Override
        public void execute(float delta) {
            heroY += speed * delta;
        }
    }

    public class MoveDownCommand implements MovementCommand {
        @Override
        public void execute(float delta) {
            heroY -= speed * delta;
        }
    }

    public class MoveLeftCommand implements MovementCommand {
        @Override
        public void execute(float delta) {
            heroX -= speed * delta;
        }
    }

    public class MoveRightCommand implements MovementCommand {
        @Override
        public void execute(float delta) {
            heroX += speed * delta;
        }
    }

    public class NoOpCommand implements MovementCommand {
        @Override
        public void execute(float delta) {
        }
    }

    public enum Direction { NONE, UP, DOWN, LEFT, RIGHT }

    private float heroX, heroY;
    private float speed = 200f;

    private final MovementCommand upCommand;
    private final MovementCommand downCommand;
    private final MovementCommand leftCommand;
    private final MovementCommand rightCommand;
    private final MovementCommand noOpCommand;

    public MovementController(float startX, float startY) {
        this.heroX = startX;
        this.heroY = startY;

        this.upCommand = new MoveUpCommand();
        this.downCommand = new MoveDownCommand();
        this.leftCommand = new MoveLeftCommand();
        this.rightCommand = new MoveRightCommand();
        this.noOpCommand = new NoOpCommand();
    }

    public float getX() { return heroX; }
    public float getY() { return heroY; }

    public void update(float delta) {
        getCommandForCurrentInput().execute(delta);
    }

    private MovementCommand getCommandForCurrentInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) return upCommand;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) return downCommand;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) return leftCommand;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return rightCommand;
        return noOpCommand;
    }

    public static Direction getCurrentDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) return Direction.UP;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) return Direction.DOWN;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) return Direction.LEFT;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return Direction.RIGHT;
        return Direction.NONE;
    }
}