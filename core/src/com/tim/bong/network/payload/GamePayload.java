package com.tim.bong.network.payload;

import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.Goal;
import com.tim.bong.game.actor.PlayerStick;

import static com.tim.bong.util.ByteStuff.*;

public class GamePayload extends Payload {
    private static final int length = Payload.length + 36;

    private static final int stickXIndex = Payload.length;
    private static final int stickAngleIndex = Payload.length + 4;
    private static final int ballXIndex = Payload.length + 8;
    private static final int ballYIndex = Payload.length + 12;
    private static final int ballVelocityXIndex = Payload.length + 16;
    private static final int ballVelocityYIndex = Payload.length + 20;
    private static final int ballSpeedIndex = Payload.length + 24;
    private static final int scoreIndex = Payload.length + 28;
    private static final int gameStateIndex = Payload.length + 32;

    private float stickX, stickAngle, ballX, ballY, ballVelX, ballVelY, ballSpeed;
    private int score, gamestate;

    public GamePayload() {
        buffer = new byte[length];
    }

    public GamePayload(int senderId, int receiverId) {
        super(senderId, receiverId);
        buffer = new byte[length];
    }

    @Override
    public void parse(byte[] data) {
        super.parse(data);

        stickX = readFloat(data, stickXIndex);
        stickAngle = readFloat(data, stickAngleIndex);

        ballX = readFloat(data, ballXIndex);
        ballY = readFloat(data, ballYIndex);
        ballVelX = readFloat(data, ballVelocityXIndex);
        ballVelY = readFloat(data, ballVelocityYIndex);
        ballSpeed = readFloat(data, ballSpeedIndex);

        score = readInt(data, scoreIndex);
        gamestate = readInt(data, gameStateIndex);
    }

    public byte[] build(PlayerStick playerStick, Ball ball, Goal goal, int gamestate, int packetId, int ack, long pongTime) {
        super.build(packetId, ack, pongTime);
        stickX = playerStick.getX();
        stickAngle = playerStick.getAngle();

        ballX = ball.getX();
        ballY = ball.getY();
        ballVelX = ball.getVelX();
        ballVelY = ball.getVelY();
        ballSpeed = ball.getSpeed();
        score = goal.getScore();
        this.gamestate = gamestate;

        //game data
        putFloat(stickX, buffer, stickXIndex);
        putFloat(stickAngle, buffer, stickAngleIndex);
        putFloat(ballX, buffer, ballXIndex);
        putFloat(ballY, buffer, ballYIndex);
        putFloat(ballVelX, buffer, ballVelocityXIndex);
        putFloat(ballVelY, buffer, ballVelocityYIndex);
        putFloat(ballSpeed, buffer, ballSpeedIndex);
        putInt(score, buffer, scoreIndex);
        putInt(gamestate, buffer, gameStateIndex);
        return buffer;
    }

    public float getStickX() {
        return stickX;
    }

    public float getStickAngle() {
        return stickAngle;
    }

    public float getBallX() {
        return ballX;
    }

    public float getBallY() {
        return ballY;
    }

    public float getBallVelX() {
        return ballVelX;
    }

    public float getBallVelY() {
        return ballVelY;
    }

    public float getBallSpeed() {
        return ballSpeed;
    }

    public int getScore() {
        return score;
    }

    public int getGamestate() {
        return gamestate;
    }
}
