package com.tim.bong.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tim.bong.util.convenience.MyBodyDef;
import com.tim.bong.util.convenience.MyFixtureDef;

public class Ball extends BasicActor implements PublicBall {

    private final static float defaultRadius = 0.8f;
    private final static float defaultSpeed = 50;

    private float radius;
    private float speed;

    public Ball() {
        super();
        radius = defaultRadius;
        speed = defaultSpeed;
        setBody(createBallBody(radius));
        setPos(worldService.getWidth() / 2, worldService.getHeight() / 2);
        worldService.registerUpdatable(this);
    }

    public void setPos(float newX, float newY) {
        super.setPos(newX, newY);
    }

    public void reset() {
        getBody().setLinearVelocity(0, 0);
        getBody().applyLinearImpulse(0, 0, getX(), getY(), true);
    }

    public void start() {
        setPos(worldService.getWidth() / 2, worldService.getHeight() / 2);
        int vY = (int) Math.pow(-1, MathUtils.random(0, 1)) * MathUtils.random(1, 2);
        Vector2 startV = new Vector2(0, vY);//TODO: new???
        startV.setLength(speed);
        getBody().applyLinearImpulse(startV, getPos(), true);
    }

    public void preSimUpdate(float delta) {
        //correct the speed
        Vector2 v = getBody().getLinearVelocity();
        float dif = speed - v.len();
        if (dif != 0 && Math.abs(dif) > 0f) {
            if (dif < 0) {
                v.set(-v.x, -v.y);
            }
            Gdx.app.debug("Ball", "ballspeed : " + v.len() + " -> correcting speed of ball");
            getBody().applyLinearImpulse(v.setLength(dif), getPos(), true);
        }
    }

    private Body createBallBody(float radius) {
        BodyDef bodyDef = new MyBodyDef(BodyDef.BodyType.DynamicBody);
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        FixtureDef fixtureDef = new MyFixtureDef(circle, 0.5f, 0f, 1);

        Body body = worldService.createBody(bodyDef);
        body.createFixture(fixtureDef);

        circle.dispose();
        return body;
    }

    @Override
    public Vector2 getBallVelocity(Vector2 temp) {
        return temp.set(getBody().getLinearVelocity());
    }

    @Override
    public float getSpeed() {
        return speed;
    }
}
