package com.tim.bong.util.convenience;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Provides some convenient constructors for com.badlogic.gdx.physics.box2d.FixtureDef
 */
public class MyFixtureDef extends FixtureDef {
    public MyFixtureDef(Shape shape, float density, float friction, float restitution) {
        super();
        super.shape = shape;
        super.density = density;
        super.friction = friction;
        super.restitution = restitution;
    }
}
