package com.tim.bong.util.convenience;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class MyRevoluteJointDef extends RevoluteJointDef {
    public MyRevoluteJointDef(Body a, Body b, Vector2 anchor) {
        super();
        initialize(a, b, anchor);
    }

    public void setLimits(float lowerAngle, float upperAngle) {
        super.lowerAngle = lowerAngle;
        super.upperAngle = upperAngle;
        super.enableLimit = true;
    }

    public void setMotor(float maxMotorTorque, float motorSpeed) {
        super.maxMotorTorque = maxMotorTorque;
        super.motorSpeed = motorSpeed;
        super.enableMotor = true;
    }
}
