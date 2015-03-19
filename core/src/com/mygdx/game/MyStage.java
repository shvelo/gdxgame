package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyStage extends Stage {
    public final MyGdxGame context;
    public String backStage = "main";

    public MyStage(MyGdxGame newContext) {
        super(new ScreenViewport());

        context = newContext;
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch(keyCode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                if(backStage != null) context.setStage(backStage);
                return true;
            default:
                return super.keyDown(keyCode);
        }
    }
}
