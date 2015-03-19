package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsStage extends Stage {
    private final Table table;
    private final Skin uiSkin;
    private final MyGdxGame context;

    public OptionsStage(MyGdxGame newContext) {
        super();

        context = newContext;

        uiSkin = context.uiSkin;

        table = new Table(uiSkin);
        table.setFillParent(true);
        addActor(table);

        TextButton exitButton = new TextButton("Back", uiSkin);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.setStage("main");
            }
        });

        table.center();
        table.add("Not implemented");
        table.row();
        table.add(exitButton).width(200).spaceBottom(10);
    }
}
