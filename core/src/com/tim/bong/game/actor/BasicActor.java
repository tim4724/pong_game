package com.tim.bong.game.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tim.bong.game.world.UpdatAble;
import com.tim.bong.game.world.WorldService;

public abstract class BasicActor implements UpdatAble {
    private Body body;
    static WorldService worldService;

    public BasicActor() {
        if (worldService == null) {
            worldService = WorldService.getInst();
        }
        body = null;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void setBody(Body body) {
        this.body = body;
        body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    public void setPos(float newX, float newY) {
        body.setTransform(newX, newY, body.getAngle());
    }

    public Vector2 getPos() {
        return body.getPosition();
    }


    @Override
    public void preSimUpdate(float delta) {
    }

    @Override
    public void postSimUpdate(float delte) {
    }
}
