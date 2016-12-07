package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.StickAnchor;

public class AiControl extends PlayerController {
    private Ball ball;

    public AiControl(StickAnchor anchor, Ball ball) {
        super(anchor);
        this.ball = ball;
    }

    @Override
    public void update(float delta) {
        //chuck norris ai
        anchor.setPos(ball.getX(), anchor.getY());
    }
}
