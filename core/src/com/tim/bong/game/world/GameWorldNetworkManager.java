package com.tim.bong.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.playercontrol.NetworkControl;
import com.tim.bong.network.Frame;
import com.tim.bong.util.ByteStuff;
import com.tim.bong.util.MathUtils2;

public class GameWorldNetworkManager extends GameWorldManager {

    private NetworkControl networkControl;
    private boolean host;
    private int gameState;

    public GameWorldNetworkManager(float width, float height, int id) {
        super(width, height);
        this.host = (id > 0);
        gameState = 0;
    }

    //override start because we need to wait for the network connection
    @Override
    public void start() {
        //do nothing
    }

    @Override
    public void reset() {
        getBall().stop();
        timer = System.currentTimeMillis() + 1500;
        System.out.println("game resetted");
    }

    public void newData(byte[] data) {
        //always update the other player stick
        float playerX = ByteStuff.readFloat(data, Frame.playerXIndex);
        networkControl.newData(playerX);
        updateStickAngle(data);

        if (host && timer > System.currentTimeMillis()) {
            return;
        }

        // game not started
        if (!running) {
            if (host) {
                // we received data -> the connection seems to work -> start the game
                gameState++;
                System.out.println("start game");
                super.start();
            }
        }

        int newGameState = ByteStuff.readInt(data, Frame.gameStateIndex);
        if (gameState < newGameState) {
            System.out.println("Gamestate changed");
            running = true;
            //something dramatic happened
            updateBall(data);
            //updateStickAngle(data);
            gameState = newGameState;
        }
    }

    @Override
    public void onBallStickCollission(PlayerStick p) {
        //ball collided with the own stick -> increment game state
        if (p.getY() > getHeight() / 2) {
            gameState++;
        }
    }

    private void updateBall(byte[] data) {
        float ballX = ByteStuff.readFloat(data, Frame.ballXIndex);
        float ballY = ByteStuff.readFloat(data, Frame.ballYIndex);
        ballY = super.getHeight() / 2 + (super.getHeight() / 2 - ballY);//mirror the y position
        float ballVelocityX = ByteStuff.readFloat(data, Frame.ballVelocityXIndex);
        float ballVelocityY = -ByteStuff.readFloat(data, Frame.ballVelocityYIndex);
        float ballSpeed = ByteStuff.readFloat(data, Frame.ballSpeedIndex);

        getBall().setPos(ballX, ballY);
        getBall().setBallVelocity(ballVelocityX, ballVelocityY);
        getBall().setSpeed(ballSpeed);
    }

    private void updateStickAngle(byte[] data) {
        float stickAngle = -ByteStuff.readFloat(data, Frame.playerAngleIndex);
        getTopPlayer().setAngle(stickAngle);
    }

    public void registerNetworkControl(NetworkControl networkControl) {
        this.networkControl = networkControl;
    }

    public int getGameState() {
        return gameState;
    }
}
