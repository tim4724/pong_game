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
    private final static float defaultSpeed = 40;
    private final static float maimumSpeed = 60;

    private float radius;
    private float a;
    private float speed;
    private boolean active;

    public Ball() {
        super();
        radius = defaultRadius;
        speed = defaultSpeed;
        a = 1.005f;
        setBody(createBallBody(radius));
        worldService.registerUpdatable(this);
    }

    public void start() {
        setPos(worldService.getWidth() / 2, worldService.getHeight() / 2);

        speed = defaultSpeed;
        int vY = MathUtils.random(0, 1) == 0 ? 1 : -1;
        Vector2 v = getBody().getLinearVelocity().set(0, vY).setLength(speed);
        getBody().applyLinearImpulse(v, getPos(), true);
        active = true;
    }

    public void stop() {
        speed = 0;
        active = false;
    }

    public void preSimUpdate(float delta) {
        if (active) {
            speed += a * delta;
            speed = Math.min(speed, maimumSpeed);
            Gdx.app.debug("Ball", "speed: " + speed);
        }
        correctSpeed();
    }

    private void correctSpeed() {
        Vector2 v = getBody().getLinearVelocity();
        float dif = speed - v.len();
        if (Math.abs(dif) > 0.0001f) {
            if (dif < 0) v.set(-v.x, -v.y);  //because set len to a negative value is not possible
            getBody().applyLinearImpulse(v.setLength(dif), getPos(), true);
        }
    }

    public void setPos(float newX, float newY) {
        super.setPos(newX, newY);
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

    public float getRadius() {
        return radius;
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
