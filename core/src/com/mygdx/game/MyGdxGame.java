package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.stages.GameStage;
import com.mygdx.game.stages.MainStage;
import com.mygdx.game.stages.OptionsStage;

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    private Stage stage;
    private PanningMap panningMap;
    private HashMap<String, Stage> stages = new HashMap<>();
    public Skin uiSkin;
    public boolean started = false;
    private int w;
    private int h;

    @Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

        uiSkin = new Skin();
        uiSkin.addRegions(new TextureAtlas("data/uiskin.atlas"));
        uiSkin.addRegions(new TextureAtlas("data/yellow.atlas"));
        uiSkin.load(Gdx.files.getFileHandle("data/uiskin.json", Files.FileType.Internal));

        stages.put("main", new MainStage(this));
        stages.put("options", new OptionsStage(this));
        stages.put("game", new GameStage(this));

        panningMap = new PanningMap();

        Gdx.input.setCatchBackKey(true);

        setStage("main");
	}

    public void setStage(String stageName) {
        Stage newStage = stages.get(stageName);
        if(newStage != null) {
            stage = newStage;
            Gdx.input.setInputProcessor(newStage);
            stage.getViewport().update(w, h, true);
        }
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        panningMap.draw();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

    @Override
    public void resize(int width, int height) {
        w = width;
        h = height;
        panningMap.resetCamera();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        stages.clear();
        panningMap.dispose();
    }
}
