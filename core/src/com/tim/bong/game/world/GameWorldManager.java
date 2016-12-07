package com.tim.bong.game.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.Goal;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.actor.StickAnchor;
import com.tim.bong.util.convenience.MyBodyDef;

import java.util.ArrayList;
import java.util.List;

public class GameWorldManager {

    private float width, height;
    private OrthographicCamera worldCamera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    //actors
    private Goal goal, enemyGoal;
    private Ball ball;
    private PlayerStick bottomPlayer, topPlayer;
    private StickAnchor bottomAnchor, topAnchor;

    private List<UpdatAble> updatAbles;
    private int countDown;

    public GameWorldManager(float width, float height) {
        WorldService.createInst(this);
        this.width = width;
        this.height = height;
        worldCamera = new OrthographicCamera();
        worldCamera.setToOrtho(true, width, height);

        //box2d
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        updatAbles = new ArrayList<UpdatAble>();
        createWallBodys();
        //actors
        goal = new Goal(false);
        enemyGoal = new Goal(true);
        ball = new Ball();
        bottomPlayer = new PlayerStick(false);
        topPlayer = new PlayerStick(true);
        bottomAnchor = new StickAnchor(bottomPlayer);
        topAnchor = new StickAnchor(topPlayer);
    }

    public void start() {
        ball.start();
    }

    public void update(float deltaTime) {
        for (UpdatAble updatAble : updatAbles) {
            updatAble.preSimUpdate(deltaTime);
        }
        if (countDown != -1) {
            countDown--;
            //TODO: because setPos in ContactListener doesn't work
            ball.setPos(width / 2, height / 2);
            if (countDown == 0) {
                start();
                countDown = -1;
            }
        }
        doPhysicsStep(deltaTime);
        for (UpdatAble updatAble : updatAbles) {
            updatAble.postSimUpdate(deltaTime);
        }
    }

    public void reset() {
        ball.reset();
        countDown = 60;
    }

    private void doPhysicsStep(float deltaTime) {
        world.step(1 / 60f, 6, 2);
    }

    public void renderDebug() {
        debugRenderer.render(world, worldCamera.combined);
    }

    private void createWallBodys() {
        BodyDef bodyDef = new MyBodyDef(BodyDef.BodyType.StaticBody, 0, height / 2);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0, height * 1.5f);   //*1.5f just to be sure because goal is outside of the playing field

        Body leftWall = world.createBody(bodyDef);
        bodyDef.position.set(width, height / 2);
        Body rightWall = world.createBody(bodyDef);
        leftWall.createFixture(groundBox, 0.0f);
        rightWall.createFixture(groundBox, 0.0f);

        groundBox.dispose();
    }

    public void registerForUpdate(UpdatAble updatable) {
        if (!updatAbles.contains(updatable)) {
            updatAbles.add(updatable);
        }
    }

    //Getter
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Ball getBall() {
        return ball;
    }

    public StickAnchor getBottomAnchor() {
        return bottomAnchor;
    }

    public StickAnchor getTopAnchor() {
        return topAnchor;
    }

    public World getWorld() {
        return world;
    }

    public OrthographicCamera getWorldCamera() {
        return worldCamera;
    }
}
