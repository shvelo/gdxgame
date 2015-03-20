package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class PanningMap implements Screen {
    private final MyGdxGame game;
    private OrthogonalTiledMapRenderer renderer;
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
    private float xSpeedM = 2f;
    private float ySpeedM = 2f;
    private float xSpeed = 1;
    private float ySpeed = 1;
    private float x;
    private float y;
    private RectangleMapObject player;
    private Animation walkAnimation;
    private float stateTime = 0f;
    private TextureRegion currentFrame;
    private HashMap<String, Animation> walkAnimations;

    private boolean checkCollision(float x, float y) {
        if(((TiledMapTileLayer)map.getLayers().get("collision")).getCell( (int)Math.floor(x / tilePixelWidth), (int)Math.floor(y / tilePixelHeight)) != null) {
            return false;
        } else {
            return true;
        }
    }

    private void calculateSpeed() {
        boolean moving = false;

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && checkCollision(x, y+2)) {
            ySpeed = 1;
            moving = true;
            walkAnimation = walkAnimations.get("up");
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && checkCollision(x, y-8)) {
            ySpeed = -1;
            moving = true;
            walkAnimation = walkAnimations.get("down");
        } else {
            ySpeed = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && checkCollision(x-6, y)) {
            xSpeed = -1;
            walkAnimation = walkAnimations.get("left");
            moving = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && checkCollision(x+6, y)) {
            xSpeed = 1;
            walkAnimation = walkAnimations.get("right");
            moving = true;
        } else {
            xSpeed = 0;
        }

        if(!moving) walkAnimation = walkAnimations.get("stand");
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

        player = (RectangleMapObject)map.getLayers().get("objects").getObjects().get("player");
        x = player.getRectangle().getX();
        y = player.getRectangle().getY();

        return map;
    }

    private void loadCharacter() {
        walkAnimations = new HashMap<String, Animation>();
        Texture characterSheet = new Texture("character.png"); //13x21
        TextureRegion[][] tmp = TextureRegion.split(characterSheet, 64, 64);
        TextureRegion[] walkFramesUp = new TextureRegion[8];
        TextureRegion[] walkFramesLeft = new TextureRegion[8];
        TextureRegion[] walkFramesRight = new TextureRegion[8];
        TextureRegion[] walkFramesDown = new TextureRegion[8];

        TextureRegion[] standFrames = new TextureRegion[1];

        System.arraycopy(tmp[8], 0, walkFramesUp, 0, 8);
        walkAnimations.put("up", new Animation(0.05f, walkFramesUp));

        System.arraycopy(tmp[9], 0, walkFramesLeft, 0, 8);
        walkAnimations.put("left", new Animation(0.05f, walkFramesLeft));

        System.arraycopy(tmp[10], 0, walkFramesRight, 0, 8);
        walkAnimations.put("down", new Animation(0.05f, walkFramesRight));

        System.arraycopy(tmp[11], 0, walkFramesDown, 0, 8);
        walkAnimations.put("right", new Animation(0.05f, walkFramesDown));

        System.arraycopy(tmp[2], 0, standFrames, 0, 1);
        walkAnimations.put("stand", new Animation(1f, standFrames));

        walkAnimation = walkAnimations.get("stand");
    }

    public PanningMap(MyGdxGame gam) {
        game = gam;
    }

    @Override
    public void show() {
        assetManager = new AssetManager();

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        map = loadMap();
        loadCharacter();

        tileW = w / tilePixelWidth;
        tileH = h / tilePixelHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileW, tileH);
        camera.zoom = 1;
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map, 4f / tilePixelWidth);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //game.batch.setProjectionMatrix(camera.combined);

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        renderer.render(new int[]{0, 1, 2, 3});
        game.batch.begin();
        game.batch.draw(currentFrame, w / 2 - 32, h / 2 - 32);
        game.batch.end();
        renderer.render(new int[]{4, 5, 6});

        calculateSpeed();

        if(!stopX) x += xSpeed * xSpeedM;
        if(!stopY) y += ySpeed * ySpeedM;

        camera.position.x = x/4;
        camera.position.y = y/4;
        camera.update();

        renderer.setView(camera);
    }

    @Override
    public void resize(int width, int height) {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        tileW = w / tilePixelWidth;
        tileH = h / tilePixelWidth;
        camera.setToOrtho(false, tileW, tileH);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        renderer.dispose();
        map.dispose();
    }
}
