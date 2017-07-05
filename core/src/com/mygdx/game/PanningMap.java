package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

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
    private final float xSpeedM = 2f;
    private final float ySpeedM = 2f;
    private float xSpeed = 1;
    private float ySpeed = 1;
    public float x;
    public float y;
    private Animation walkAnimation;
    private float stateTime = 0f;
    private HashMap<String, Animation> walkAnimations;
    private byte[][] collisionLayer;
    public String lastTeleport = "";
    public String lastMap = "";
    public float lastX = -1;
    public float lastY = -1;
    public String mapName;
    public Rectangle playerRect;

    private void makeCollisionLayer() {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        collisionLayer = new byte[layer.getWidth()][layer.getHeight()];
        for(int i = 0; i < layer.getWidth(); ++i) {
            for(int j = 0; j < layer.getHeight(); ++j) {
                if(layer.getCell(i, j) == null) {
                    collisionLayer[i][j] = 0;
                } else {
                    collisionLayer[i][j] = 1;
                }
            }
        }
    }

    private boolean checkCollision(float x, float y) {
        try {
            if (collisionLayer[(int) Math.floor(x / tilePixelWidth)][(int) Math.floor(y / tilePixelHeight)] != 0) {
                return false;
            } else {
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }
    }

    private void checkTeleport() {
        boolean teleport_overlaps = false;
        for(MapObject obj : map.getLayers().get("objects").getObjects()) {
            if(obj.getProperties().get("type").equals("teleport")
                    && Intersector.overlaps(
                        new Rectangle(x, y, 1, 1),
                        ((RectangleMapObject) obj).getRectangle())
                    ) {
                teleport_overlaps = true;
                if(lastTeleport.equals(obj.getName())) {

                } else {
                    float cX = x;
                    float cY = y;
                    String cMapName = mapName;
                    map = loadMap(obj.getName());
                    lastTeleport = obj.getName();
                    lastX = cX;
                    lastY = cY;
                    lastMap = cMapName;
                }
            }
        }
        if(!teleport_overlaps) lastTeleport = "";
    }

    private void calculateSpeed() {
        boolean moving = false;

        if(game.upPressed && checkCollision(x, y+2)) {
            ySpeed = 1;
            moving = true;
            walkAnimation = walkAnimations.get("up");
        } else if(game.downPressed && checkCollision(x, y-12)) {
            ySpeed = -1;
            moving = true;
            walkAnimation = walkAnimations.get("down");
        } else {
            ySpeed = 0;
        }

        if(game.leftPressed && checkCollision(x-6, y)) {
            xSpeed = -1;
            walkAnimation = walkAnimations.get("left");
            moving = true;
        } else if(game.rightPressed && checkCollision(x+6, y)) {
            xSpeed = 1;
            walkAnimation = walkAnimations.get("right");
            moving = true;
        } else {
            xSpeed = 0;
        }

        if(!moving) walkAnimation = walkAnimations.get("stand");
    }

    private TiledMap loadMap(String mapName) {
        this.mapName = mapName;

        if(!assetManager.isLoaded(mapName)) {
            assetManager.load(mapName, TiledMap.class);
            assetManager.finishLoading();
        }

        map = assetManager.get(mapName);

        MapProperties prop = map.getProperties();

        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);

        RectangleMapObject player = (RectangleMapObject) map.getLayers().get("objects").getObjects().get("player");
        if(lastMap.equals(mapName)) {
            x = lastX;
            y = lastY;
            lastX = -1;
            lastY = -1;
            lastMap = "";
        } else {
            x = player.getRectangle().getX();
            y = player.getRectangle().getY();
        }

        playerRect = new Rectangle(x/4f - 2.5f, y/4f - 2.5f, 5, 5);


        tileW = w / tilePixelWidth;
        tileH = h / tilePixelHeight;

        if(camera == null) camera = new OrthographicCamera();

        camera.setToOrtho(false, tileW, tileH);
        camera.zoom = 1;
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map, 4f / tilePixelWidth);
        renderer.getBatch().enableBlending();
        makeCollisionLayer();

        return map;
    }

    private void loadCharacter() {
        walkAnimations = new HashMap<>();
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
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        map = loadMap("map.tmx");
        loadCharacter();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        stateTime += delta;
        TextureRegion currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

        renderer.render(new int[]{0, 1, 2, 3});
        game.batch.begin();
        game.batch.draw(currentFrame, playerRect.x, playerRect.y, playerRect.width, playerRect.height);
        game.batch.end();
        renderer.render(new int[]{4, 5, 6});

        checkTeleport();
        calculateSpeed();

        x += xSpeed * xSpeedM;
        y += ySpeed * ySpeedM;

        camera.position.x = x/4f;
        camera.position.y = y/4f;
        camera.update();

        playerRect.setX(x/4f - 2.5f);
        playerRect.setY(y/4f - 2.5f);

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
