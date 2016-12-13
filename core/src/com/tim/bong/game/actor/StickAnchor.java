package com.tim.bong.game.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.tim.bong.util.convenience.MyBodyDef;
import com.tim.bong.util.convenience.MyFixtureDef;
import com.tim.bong.util.convenience.MyRevoluteJointDef;

public class StickAnchor extends BasicActor {

    private PlayerStick playerStick;
    private RevoluteJoint joint;

    private final float initialAngle;

    public StickAnchor(PlayerStick playerStick) {
        super();
        setBody(createAnchorBody(playerStick));
        this.playerStick = playerStick;
        initialAngle = playerStick.getBody().getAngle();
        joint = joinPlayerAndAnchor(playerStick.getBody(), getBody());
        worldService.registerUpdatable(this);
    }

    @Override
    public void preSimUpdate(float delta) {
        super.preSimUpdate(delta);

        float angleDif = playerStick.getBody().getAngle() - initialAngle;
        joint.setMotorSpeed(angleDif);
        postSimUpdate(delta);
    }

    public void updatePos(float newX) {
        //limit anchor movement in x direction
        newX = Math.min(newX, worldService.getWidth() - playerStick.getLen() / 2);
        newX = Math.max(newX, playerStick.getLen() / 2);

        super.setPos(newX, getY());
    }

    private Body createAnchorBody(PlayerStick player) {
        BodyDef anchorBodyDef = new MyBodyDef(BodyDef.BodyType.StaticBody, player.getPos());
        CircleShape circle = new CircleShape();
        circle.setRadius(0.3f);
        FixtureDef anchorCircle = new MyFixtureDef(circle, 1, 0, 0);
        anchorCircle.isSensor = true;

        Body anchor = worldService.createBody(anchorBodyDef);
        anchor.createFixture(anchorCircle);

        circle.dispose();
        return anchor;
    }

    private RevoluteJoint joinPlayerAndAnchor(Body pBody, Body aBody) {
        MyRevoluteJointDef jointDef = new MyRevoluteJointDef(pBody, aBody, pBody.getPosition());
        jointDef.setLimits(-0.1f * MathUtils.PI, 0.1f * MathUtils.PI);
        jointDef.setMotor(100, 0);

        return ((RevoluteJoint) worldService.createJoint(jointDef));
    }

    public float getStickLen() {
        return playerStick.getLen();
    }
}
