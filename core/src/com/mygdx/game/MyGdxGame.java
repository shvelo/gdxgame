package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.stages.GameStage;
import com.mygdx.game.stages.MainStage;
import com.mygdx.game.stages.OptionsStage;

import java.util.HashMap;

/**
 * Main game class
 */
public class MyGdxGame extends Game {
    private Stage stage;
    private PanningMap panningMap;
    private HashMap<String, Stage> stages = new HashMap<>();
    public Skin uiSkin;
    public boolean started = false;
    private int w;
    private int h;
    public SpriteBatch batch;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean upPressed = false;
    public boolean downPressed = false;

    @Override
	public void create () {
        batch = new SpriteBatch();

        w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

        uiSkin = new Skin();
        uiSkin.addRegions(new TextureAtlas("data/uiskin.atlas"));
        uiSkin.addRegions(new TextureAtlas("data/yellow.atlas"));
        uiSkin.load(Gdx.files.getFileHandle("data/uiskin.json", Files.FileType.Internal));

        stages.put("main", new MainStage(this));
        stages.put("options", new OptionsStage(this));
        stages.put("game", new GameStage(this));

        panningMap = new PanningMap(this);

        Gdx.input.setCatchBackKey(true);

        setStage("main");

        setScreen(panningMap);
	}

    /**
     * Set Scene2D UI stage
     * @param stageName Stage name in stages
     */
    public void setStage(String stageName) {
        Stage newStage = stages.get(stageName);
        if(newStage != null) {
            stage = newStage;
            Gdx.input.setInputProcessor(newStage);
            stage.getViewport().update(w, h, true);
        }
    }

	@Override
	public void render() {
        super.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        w = width;
        h = height;
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        stages.clear();
        panningMap.dispose();
    }
}
