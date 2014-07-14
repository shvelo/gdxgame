package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends ApplicationAdapter {
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private float w;
	private float h;
	private float tileW;
	private float tileH;
	float xSpeed = 0.1f;
	float ySpeed = 0.05f;
	float x;
	float y;
	int mapWidth;
	int mapHeight;
	int tilePixelWidth;
	int tilePixelHeight;
	
	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		assetManager = new AssetManager();
		
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
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(viewResized()) resetCamera();

		calculateSpeed();

		x += xSpeed;
		y += ySpeed;

		camera.position.x = x;
		camera.position.y = y;
		camera.update();

		renderer.setView(camera);
		renderer.render();
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
	}

	public boolean viewResized() {
		return (w != Gdx.graphics.getWidth() || h != Gdx.graphics.getHeight());
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
