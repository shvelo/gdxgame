package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyStage;

public class GameStage extends MyStage {
    private int thresholdX;
    private int thresholdY;
    private int screenWidth;
    private int screenHeight;

    public GameStage(MyGdxGame newContext) {
        super(newContext);
    }

    private void resetPressed() {
        context.rightPressed = false;
        context.leftPressed = false;
        context.downPressed = false;
        context.upPressed = false;
    }

    private void calculateTouch(int screenX, int screenY) {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        thresholdX = screenWidth / 8;
        thresholdY = screenHeight / 8;

        if(screenX - thresholdX > screenWidth / 2) {
            context.rightPressed = true;
            context.leftPressed = false;
        } else if(screenX + thresholdX < screenWidth / 2) {
            context.rightPressed = false;
            context.leftPressed = true;
        } else {
            context.rightPressed = false;
            context.leftPressed = false;
        }

        if(screenY - thresholdY > screenHeight / 2) {
            context.downPressed = true;
            context.upPressed = false;
        } else if(screenY + thresholdY < screenHeight / 2) {
            context.upPressed = true;
            context.downPressed = false;
        } else {
            context.upPressed = false;
            context.downPressed = false;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        calculateTouch(screenX, screenY);

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        resetPressed();

        calculateTouch(screenX, screenY);

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        resetPressed();
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch(keyCode) {
            case Input.Keys.LEFT:
                context.leftPressed = true;
                break;
            case Input.Keys.RIGHT:
                context.rightPressed = true;
                break;
            case Input.Keys.UP:
                context.upPressed = true;
                break;
            case Input.Keys.DOWN:
                context.downPressed = true;
                break;
        }

        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {
        switch(keyCode) {
            case Input.Keys.LEFT:
                context.leftPressed = false;
                break;
            case Input.Keys.RIGHT:
                context.rightPressed = false;
                break;
            case Input.Keys.UP:
                context.upPressed = false;
                break;
            case Input.Keys.DOWN:
                context.downPressed = false;
                break;
        }

        return super.keyUp(keyCode);
    }
}
