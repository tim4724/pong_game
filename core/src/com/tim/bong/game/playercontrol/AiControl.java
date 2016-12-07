package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.StickAnchor;

public class AiControl extends PlayerController {
    public AiControl(StickAnchor anchor, Ball ball) {
        super(anchor, ball);
    }

    @Override
    public void update(float delta) {
        anchor.setPos(ball.getX(), anchor.getY());
    }
}
