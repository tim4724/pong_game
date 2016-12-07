package com.tim.bong.util.convenience;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Provides some convenient constructors for com.badlogic.gdx.physics.box2d.BodyDef
 */
public class MyBodyDef extends BodyDef {

    public MyBodyDef(BodyType type) {
        super();
        super.type = type;
    }

    public MyBodyDef(BodyType type, float posX, float posY) {
        this(type);
        super.position.set(posX, posY);
    }

    public MyBodyDef(BodyType type, Vector2 pos) {
        this(type);
        super.position.set(pos);
    }
}
