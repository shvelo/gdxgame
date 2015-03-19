package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameStage extends Stage {
    private final MyGdxGame context;

    public GameStage(MyGdxGame newContext) {
        super(new ScreenViewport());

        context = newContext;
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch(keyCode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                context.setStage("main");
                return true;
            default:
                return super.keyDown(keyCode);
        }
    }
}
