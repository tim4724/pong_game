package com.tim.bong.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tim.bong.util.convenience.MyBodyDef;
import com.tim.bong.util.convenience.MyFixtureDef;

public class Ball extends BasicActor implements PublicBall {
    private final static float defaultRadius = 0.8f;
    private final static float defaultSpeed = 35;
    private final static float maimumSpeed = 60;

    private float radius;
    private float a;
    private float speed;
    private Color color;

    public Ball() {
        super();
        radius = defaultRadius;
        speed = defaultSpeed;
        a = 1.005f;
        setBody(createBallBody(radius));
        setPos(worldService.getWidth() / 2, worldService.getHeight() / 2);
        worldService.registerUpdatable(this);
        color = new Color(0.145f, 0.168f, 1f, 1f);
    }

    public void start() {
        setPos(worldService.getWidth() / 2, worldService.getHeight() / 2);

        speed = defaultSpeed;
        Vector2 v = getBody().getLinearVelocity().set(0, 1).setLength(speed);
        getBody().applyLinearImpulse(v, getPos(), true);
    }

    public void preSimUpdate(float delta) {
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
    public float getVelX() {
        return getBody().getLinearVelocity().x;
    }

    @Override
    public float getVelY() {
        return getBody().getLinearVelocity().y;
    }

    public void setBallVelocity(float vX, float vY) {
        getBody().setLinearVelocity(vX, vY);//TODO: test
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }
}
