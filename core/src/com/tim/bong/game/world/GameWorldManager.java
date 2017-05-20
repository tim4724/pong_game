package com.tim.bong.game.world;

import com.badlogic.gdx.Gdx;
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
    //world
    private float width, height;
    private OrthographicCamera worldCamera;
    private World world;

    private Box2DDebugRenderer debugRenderer;
    //actors
    private Goal bottomGoal, topGoal;
    private Ball ball;
    private PlayerStick bottomPlayer, topPlayer;
    private StickAnchor bottomAnchor, topAnchor;
    private List<UpdatAble> updatAbles;

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
        bottomGoal = new Goal(false);
        topGoal = new Goal(true);
        ball = new Ball();
        bottomPlayer = new PlayerStick(false);
        topPlayer = new PlayerStick(true);
        bottomAnchor = new StickAnchor(bottomPlayer);
        topAnchor = new StickAnchor(topPlayer);
    }

    public void show() {
        startDelayed(2000);
    }

    public void startGame() {
        ball.start();
        System.out.println("start game B");
    }

    public void update(float deltaTime) {
        for (UpdatAble updatAble : updatAbles) {
            updatAble.preSimUpdate(deltaTime);
        }

        doPhysicsStep(deltaTime);

        for (UpdatAble updatAble : updatAbles) {
            updatAble.postSimUpdate(deltaTime);
        }
    }

    public void reset() {
        resetBallPosition();
        startDelayed(2000);
    }

    public void startDelayed(long delay) {
        System.out.println("start game delayed " + delay);
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println("start game A");
                startGame();
            }
        }, delay);
    }

    public void resetBallPosition() {
        //do it before the next frame is rendered, because setting the position within the contactlistener doesn't work for any reason
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                ball.setSpeed(0);
                ball.setPos(width / 2, height / 2);
            }
        });
    }

    public void onBallStickCollission(PlayerStick p) {
    }

    public void onBallGoalCollission(Goal g) {
        reset();
        g.goalScored();
    }

    private void doPhysicsStep(float deltaTime) {
        world.step(deltaTime, 10, 5);
    }

    public void renderDebug() {
        debugRenderer.render(world, worldCamera.combined);
    }

    private void createWallBodys() {
        BodyDef bodyDef = new MyBodyDef(BodyDef.BodyType.StaticBody, 0, height / 2);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0, height * 1.5f);   //*1.5f just to be sure because bottomGoal is outside of the playing field

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

    public PlayerStick getBottomPlayer() {
        return bottomPlayer;
    }

    public PlayerStick getTopPlayer() {
        return topPlayer;
    }

    public Goal getBottomGoal() {
        return bottomGoal;
    }

    public Goal getTopGoal() {
        return topGoal;
    }

    public World getWorld() {
        return world;
    }
}
