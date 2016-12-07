package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.StickAnchor;

public abstract class PlayerController {
    StickAnchor anchor;
    Ball ball;

    public PlayerController(StickAnchor anchor, Ball ball) {
        this.anchor = anchor;
        this.ball = ball;
    }

    public void update(float delta) {

    }
}
