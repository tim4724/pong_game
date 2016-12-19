package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.StickAnchor;

public class NetworkControl extends PlayerController {
    float x;

    public NetworkControl(StickAnchor anchor) {
        super(anchor);
    }

    public void newData(float x) {
        this.x = x;
    }

    @Override
    public void update(float deltaT) {
        anchor.updatePos(x);
    }
}
