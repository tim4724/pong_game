package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.StickAnchor;

public abstract class PlayerController {
    final StickAnchor anchor;
    final float width, height;

    public PlayerController(StickAnchor anchor, float width, float height) {
        this.anchor = anchor;
        this.width = width;
        this.height = height;
    }

    public void update(float deltaT) {
    }
}
