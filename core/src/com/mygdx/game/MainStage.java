package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainStage extends Stage {
    private final Table table;
    private final Skin uiSkin;
    private final MyGdxGame context;
    private TextButton startButton;

    public MainStage(MyGdxGame newContext) {
        super();

        context = newContext;

        uiSkin = context.uiSkin;

        table = new Table(uiSkin);
        table.setFillParent(true);
        addActor(table);

        startButton = new TextButton("Start", uiSkin);
        TextButton optionsButton = new TextButton("Options", uiSkin);
        TextButton exitButton = new TextButton("Exit", uiSkin);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.started = true;
                context.setStage("game");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.setStage("options");
            }
        });

        table.center();
        table.add(new Image(new Texture("logo.png"))).spaceBottom(20);
        table.row();
        table.add(startButton).width(200).spaceBottom(10);
        table.row();
        table.add(optionsButton).width(200).spaceBottom(10);
        table.row();
        table.add(exitButton).width(200).spaceBottom(10);
    }

    @Override
    public void draw() {
        super.draw();
        if(context.started) startButton.setText("Resume");
    }
}
