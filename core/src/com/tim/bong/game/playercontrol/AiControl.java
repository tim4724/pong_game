package com.tim.bong.game.playercontrol;

import com.badlogic.gdx.math.Vector2;
import com.tim.bong.game.actor.PublicBall;
import com.tim.bong.game.actor.StickAnchor;

public class AiControl extends PlayerController {
    private final PublicBall ball;
    private Vector2 temp;

    public AiControl(StickAnchor anchor, PublicBall ball, float width, float height) {
        super(anchor, width, height);
        this.ball = ball;
    }

    /**
     * Regulary called.
     *
     * @param deltaT how much time was spent in sec since the last call.
     *               newPosX = oldPosX + (v * delta); (ds = v * dt)
     */
    @Override
    public void update(float deltaT) {
        //values you can work with
        float gameWidth = width;
        float gameHeight = height;

        float stickLen = anchor.getStickLen();
        float xPos = anchor.getX();
        float yPos = anchor.getY();
        Vector2 position = anchor.getPos();

        float ballX = ball.getX();
        float ballY = ball.getY();
        //velocity in x and y direction
        Vector2 ballVelocity = ball.getBallVelocity(temp);
        float ballSpeed = ball.getSpeed();

        //chuck norris ai
        //Das Wichtigste Position des Sticks setzen!!!
        anchor.updatePos(ball.getX(), anchor.getY());
    }
}
