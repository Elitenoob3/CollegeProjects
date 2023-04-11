package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ShapeCache;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.mygdx.game.*;

import java.util.HashMap;

public class Instance implements Screen {

    Preferences prefs = Gdx.app.getPreferences("Prefs");

    // Background From https://www.youtube.com/watch?v=tQR6jyfK6Ps&list=PLdsGes2mFh92eHpOZVJQgoubb6rF0CcvU&index=3
    Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("RAS.mp3"));

    //Parent reference
    MyGdxGame parent;

    Stage stage;

    TiledMap map;
    World world;
    CollisionHandler collisionHandler;
    PhysicsShapeCache physicsBodies;

    OrthogonalTiledMapRenderer renderer;
    //To remove
    Box2DDebugRenderer debugRenderer;

    ExtendViewport viewport;
    OrthographicCamera camera;

    HashMap<String, Texture> animationTextureLibrary = new HashMap<String,Texture>();

    Player player;

    float accumulator = 0.0F;

    Vector2 cPos = new Vector2(0,0);

    public boolean winCondition = false;

    public Instance(MyGdxGame parent)
    {
        this.parent = parent;
        CommonBranch();

        player = new Player(createBody("ball",2,2,0,Constants.BIT_PLAYER,(short) (Constants.BIT_WALLS | Constants.BIT_LEVEL | Constants.BIT_END), true),
                this, animationTextureLibrary);
    }

    public Instance(MyGdxGame parent, Vector2 pos)
    {
        this.parent = parent;
        CommonBranch();

        float fx = prefs.getFloat("px");
        float fy = prefs.getFloat("py");

        if(2>=fx) fx = 2;
        if(2>=fy) fy = 2;

        player = new Player(createBody("ball", fx , fy ,0,Constants.BIT_PLAYER,(short) (Constants.BIT_WALLS | Constants.BIT_LEVEL | Constants.BIT_END), true),
                this, animationTextureLibrary);
        cPos.y = prefs.getFloat("cy");
    }

    private void CommonBranch()
    {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(20,20,camera);
        viewport.setScreenSize(1920,1080);

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f/16f);

        renderer.setView(camera);
        renderer.getBatch().setProjectionMatrix(camera.combined);

        //To remove
        //debugRenderer = new Box2DDebugRenderer();

        stage = new Stage(viewport);

        collisionHandler = new CollisionHandler(this);
        world = new World(new Vector2(0,-15),true);
        Box2D.init();
        world.setContactListener(collisionHandler);

        MapBodyBuilder.buildShapes(map,16f,world);
        physicsBodies = new PhysicsShapeCache("Ballpsx.xml");

        loadAnimationLibrary();

        System.out.println(animationTextureLibrary.get("playerIdle"));

        menuMusic.setLooping(true);
        menuMusic.setVolume(prefs.getFloat("musicLevel"));
        if(prefs.getBoolean("musicOn"))
        menuMusic.play();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /* DO DELETION AT THE END OF THE RENDER CYCLE */

    @Override
    public void render(float delta) {
        //Clear old frame
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(16384);

        float dt = Gdx.graphics.getDeltaTime();

        //Compute Movement
        if(player!=null)
        player.performActions(dt);

        //Do physics step
        stepWorld(dt);

        if(renderer!=null)
        {
            camera.position.set(cPos.x + 10f, cPos.y + 10f , 0f);
            camera.update();

            renderer.setView(camera);

            //Render Map
            renderer.render();

            //Load sprites
            renderer.getBatch().begin();

            player.packTextures(dt);

            //Finish Loading Sprites in the space above
            renderer.getBatch().end();
        }

        if(camera != null && debugRenderer!=null)
        debugRenderer.render(world,camera.combined);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        menu();

        if(winCondition) win();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if(camera != null && renderer != null)
        renderer.getBatch().setProjectionMatrix(camera.combined);
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

    @Override
    public void dispose() {
        prefs.putFloat("px",player.getBody().getPosition().x);
        prefs.putFloat("py",player.getBody().getPosition().y);
        prefs.putFloat("cy",cPos.y);
        prefs.flush();
        System.out.println("saved pos");

        menuMusic.dispose();

        parent = null;

        stage.dispose();
        renderer.dispose();

        //To remove
        if(debugRenderer!=null)
        debugRenderer.dispose();

        camera = null;
        viewport = null;

        animationTextureLibrary.clear();
        animationTextureLibrary = null;

        map.dispose();

        if(player!=null)
        {
            player.dispose();
            player = null;
        }

        //Dispose player before world
        world.clearForces();
        world.dispose();

        if(physicsBodies!=null)
        physicsBodies.dispose();

        cPos = null;

        System.out.println("Instance Disposed");
    }

    public void loadAnimationLibrary()
    {
        animationTextureLibrary.put("playerIdle", new Texture("playerIdle.png"));
        animationTextureLibrary.put("playerCharging", new Texture("playerCharging.png"));
        animationTextureLibrary.put("playerJumping", new Texture("playerJumping.png"));
        animationTextureLibrary.put("playerFalling", new Texture("playerFalling.png"));
    }

    public Body createBody(String name, float x, float y, float rotation, short cBits, short mBits) {
        Body body = this.physicsBodies.createBody(name, world, Constants.SCALE, Constants.SCALE);
        body.setTransform(x, y, rotation);
        Filter filter = new Filter();
        filter.categoryBits = cBits;
        filter.maskBits = mBits;
        filter.groupIndex = 0;
        Array<Fixture> bfx = body.getFixtureList();

        for(int i = 0; i < bfx.size; ++i) {
            ((Fixture)bfx.get(i)).setFilterData(filter);
        }
        return body;
    }

    public Body createBody(String name, float x, float y, float rotation, short cBits, short mBits, boolean GD) {
        Body body = this.physicsBodies.createBody(name, world, Constants.SCALE, Constants.SCALE);

        Filter filter = new Filter();
        filter.categoryBits = cBits;
        filter.maskBits = mBits;
        filter.groupIndex = 0;
        Array<Fixture> bfx = body.getFixtureList();

        for(int i = 0; i < bfx.size; ++i) {
            ((Fixture)bfx.get(i)).setFilterData(filter);
        }

        if(GD)
        {
            filter.categoryBits = Constants.BIT_SCANNER;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.3f,0.3f,new Vector2(0.8f,0.6f),0);
            Fixture fixture = body.createFixture(shape,0F);
            fixture.setFilterData(filter);
            fixture.setSensor(true);
            shape.dispose();
        }

        body.setTransform(x, y, rotation);

        return body;
    }


    private void stepWorld(float delta) {
        accumulator += Math.min(delta, 0.25F);
        if (accumulator >= 0.017F) {
            accumulator -= 0.017F;
            world.step(0.017F, 6, 2);
        }
    }

    public World getWorld()
    {
        if(world!=null)
        return world;
        return null;
    }

    public OrthogonalTiledMapRenderer getRenderer()
    {
        return renderer;
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }

    public void setCposy(float pos)
    {
        cPos.y = pos;
    }

    public float getCposy()
    {
        return cPos.y;
    }

    public void menu()
    {
        parent.changeScreen(Constants.MENU);
        parent.clearGameInstance();
        dispose();
    }

    public void win()
    {
        parent.changeScreen(Constants.WIN_SCREEN);
        parent.clearGameInstance();
        dispose();
    }
}
