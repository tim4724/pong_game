package com.tim.bong.network;

import com.badlogic.gdx.math.Vector2;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.util.ByteStuff;

public class Frame {
    public static final int sessionIdIndex = 0;
    public static final int gameStateIndex = 4;
    public static final int frameIdIndex = 8;
    public static final int lastAckIndex = 12;

    public static final int playerXIndex = 16;
    public static final int playerAngleIndex = 20;
    public static final int ballXIndex = 24;
    public static final int ballYIndex= 28;
    public static final int ballVelocityXIndex= 32;
    public static final int ballVelocityYIndex = 36;
    public static final int ballSpeedIndex = 40;

    private final int sessionId;
    private int frameId;
    private byte[] buffer;
    private Vector2 temp;

    public Frame(int sessionId) {
        this.sessionId = sessionId;

        buffer = new byte[44];
        temp = new Vector2();
    }

    public byte[] build(int lastAck, PlayerStick playerStick, Ball ball, int gameState) {
        //meta data
        ByteStuff.putInt(sessionId, buffer, sessionIdIndex);
        ByteStuff.putInt(gameState, buffer, gameStateIndex);
        ByteStuff.putInt(frameId++, buffer, frameIdIndex);
        ByteStuff.putInt(lastAck, buffer, lastAckIndex);
        //game data
        ByteStuff.putFloat(playerStick.getX(), buffer, playerXIndex);
        //detailed game data
        ByteStuff.putFloat(playerStick.getAngle(), buffer,playerAngleIndex);
        ByteStuff.putFloat(ball.getX(), buffer, ballXIndex);
        ByteStuff.putFloat(ball.getY(), buffer, ballYIndex);
        Vector2 ballVelocity = ball.getBallVelocity(temp);
        ByteStuff.putFloat(ballVelocity.x, buffer, ballVelocityXIndex);
        ByteStuff.putFloat(ballVelocity.y, buffer, ballVelocityYIndex);
        ByteStuff.putFloat(ball.getSpeed(), buffer, ballSpeedIndex);
        return buffer;
    }
}
