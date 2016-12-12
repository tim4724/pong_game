package com.tim.bong.game.actor;

import com.badlogic.gdx.math.Vector2;

public interface PublicBall {
    float getX();

    float getY();

    Vector2 getBallVelocity(Vector2 temp);

    float getSpeed();
}
