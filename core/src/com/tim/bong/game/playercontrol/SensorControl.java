package com.tim.bong.game.playercontrol;

import com.badlogic.gdx.Gdx;
import com.tim.bong.game.actor.StickAnchor;

public class SensorControl extends PlayerController {
    private float sensitivity;

    public SensorControl(StickAnchor anchor, float sensitivity) {
        super(anchor);
        this.sensitivity = sensitivity;
    }

    @Override
    public void update(float deltaT) {
        float deltaX = Gdx.input.getAccelerometerX() * deltaT * -0.14f * sensitivity;
        anchor.updatePos(anchor.getX() + deltaX);
    }
}
