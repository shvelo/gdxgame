package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class PanningMap {
    private final OrthogonalTiledMapRenderer renderer;
    private int w;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private int h;
    private int tileW;
    private int tileH;
    private OrthographicCamera camera;
    private AssetManager assetManager;
    private TiledMap map;
    private Integer mapWidth;
    private Integer mapHeight;
    private boolean stopX = false;
    private boolean stopY = false;
    private float xSpeed = 0.2f;
    private float ySpeed = 0.2f;
    private float x;
    private float y;

    public PanningMap() {
        assetManager = new AssetManager();

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
    }

    public void draw () {
        calculateSpeed();

        if(!stopX) x += xSpeed;
        if(!stopY) y += ySpeed;

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
