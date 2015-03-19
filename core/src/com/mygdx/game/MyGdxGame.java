package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private final AssetManager assetManager = new AssetManager();
	private float w;
	private float h;
	private float tileW;
	private float tileH;
    private boolean stopX = false;
    private boolean stopY = false;
	float xSpeed = 0.1f;
	float ySpeed = 0.1f;
	float x;
	float y;
	int mapWidth;
	int mapHeight;
	int tilePixelWidth;
	int tilePixelHeight;
    private Stage stage;
    private HashMap<String, Stage> stages = new HashMap<>();
    public Skin uiSkin;
    public boolean started = false;

    @Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		map = loadMap();

		tileW = w / tilePixelWidth;
		tileH = h / tilePixelHeight;

		x = tileW;
		y = tileH;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, tileW, tileH);
		camera.zoom = 1;
		camera.update();

		renderer = new OrthogonalTiledMapRenderer(map, 1f / tilePixelWidth);

        uiSkin = new Skin();
        uiSkin.addRegions(new TextureAtlas("data/uiskin.atlas"));
        uiSkin.addRegions(new TextureAtlas("data/yellow.atlas"));
        uiSkin.load(Gdx.files.getFileHandle("data/uiskin.json", Files.FileType.Internal));

        stages.put("main", new MainStage(this));
        stages.put("options", new OptionsStage(this));
        stages.put("game", new GameStage(this));

        Gdx.input.setCatchBackKey(true);

        setStage("main");
	}

    public void setStage(String stageName) {
        Stage newStage = stages.get(stageName);
        if(newStage != null) {
            stage = newStage;
            Gdx.input.setInputProcessor(newStage);
            stage.getViewport().update((int)w, (int)h, true);
        }
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		calculateSpeed();

		if(!stopX) x += xSpeed;
        if(!stopY) y += ySpeed;

		camera.position.x = x;
		camera.position.y = y;
		camera.update();

		renderer.setView(camera);
		renderer.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

    @Override
    public void resize(int width, int height) {
        resetCamera();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        stages.clear();
        map.dispose();
    }

	private void calculateSpeed() {
		if( ((x >= mapWidth - tileW) && (xSpeed > 0)) ||
			((x <= tileW) && (xSpeed < 0)) ) {
			xSpeed = xSpeed * -1;
		}
		 
		if( ((y >= mapHeight - tileH) && ySpeed > 0) ||
			((y <= tileH) && (ySpeed < 0)) ) {
			ySpeed = ySpeed * -1;
		}

        stopX = w*2 >= mapWidth * tilePixelWidth;
        stopY = h*2 >= mapHeight * tilePixelHeight;
	}

	public void resetCamera() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		tileW = w / tilePixelWidth;
		tileH = h / tilePixelWidth;
		camera.setToOrtho(false, tileW, tileH);
	}

	private TiledMap loadMap() {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("map.tmx", TiledMap.class);

		assetManager.finishLoading();
		map = assetManager.get("map.tmx");

		MapProperties prop = map.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

		return map;
	}
}
