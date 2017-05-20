package com.tim.bong.game.world;

import com.badlogic.gdx.Gdx;
import com.tim.bong.game.actor.Goal;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.actor.PublicBall;
import com.tim.bong.game.playercontrol.NetworkControl;
import com.tim.bong.network.GameDataExchanger;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class GameWorldNetworkManager extends GameWorldManager {

    private boolean host;
    private NetworkControl networkPlayerControl;
    private GameDataExchanger gameDataExchanger;
    private boolean connectionLost;
    private boolean ballMovingAwayFromMe;
    private int version, gameState;
    private boolean initialStart;

    public GameWorldNetworkManager(float width, float height) {
        super(width, height);
        connectionLost = false;
    }

    public void init(DatagramSocket socketToUse, InetSocketAddress target, int myId, int opponnentId) {
        gameDataExchanger = new GameDataExchanger(this, socketToUse, target, myId, opponnentId);
        host = myId > opponnentId;
    }

    @Override
    public void show() {
        //not start the game!! - just do nothing
    }

    @Override
    public void startGame() {
        super.startGame();
        gameState++;
    }

    @Override
    public void update(float deltaTime) {
        //simulate game
        super.update(deltaTime);

        int newVersion = gameDataExchanger.getStats().getLastReceivedId();
        int newGameState = gameDataExchanger.getRemoteGameState();
        if (newVersion > version && newGameState >= gameState) {
            networkPlayerControl.updateStickPosition(gameDataExchanger.getRemotePlayerX());
            PublicBall remoteBall = gameDataExchanger.getRemoteBall();

            if (ballMovingAwayFromMe || newGameState > gameState) {
                boolean smooth = getBall().getSpeed() != 0;
                adjustBallPosition(smooth, remoteBall.getX(), remoteBall.getY());

                getBall().setBallVelocity(remoteBall.getVelX(), remoteBall.getVelY());
                getBall().setSpeed(remoteBall.getSpeed());
            }
            ballMovingAwayFromMe = remoteBall.getVelY() < 0;

            version = newVersion;
            gameState = newGameState;
        }

        if (gameDataExchanger.getRemoteScore() > getTopGoal().getScore()) {
            getTopGoal().goalScored();
            resetBallPosition();
        }


        gameDataExchanger.update(getBottomPlayer(), getBall(), getBottomGoal(), gameState);
    }

    private void adjustBallPosition(boolean smooth, float targetX, float targetY) {
        if (!smooth) {
            getBall().setPos(targetX, targetY);
        } else {
            float dif2 = (targetX - getBall().getX()) * (targetX - getBall().getX()) + (targetY - getBall().getY()) * (targetY - getBall().getY());
            System.out.println("dif: " + Math.sqrt(dif2));
            float deltaX = (targetX - getBall().getX());
            float deltaY = (targetY - getBall().getY());
            float newX = getBall().getX() + deltaX * 0.1f;
            float newY = getBall().getY() + deltaY * 0.1f;

            getBall().setPos(newX, newY);
        }
    }

    public void connectionLost() {
        Gdx.app.debug("GameWorldNetwork", "Connection lost; pause the game");
        connectionLost = true;
        if (host) {
            gameState = gameState + 2;
        } else {
            gameState++;
        }
        getBall().setSpeed(0);
    }

    public void connected() {
        Gdx.app.debug("GameWorldNetwork", "Opponnent is ready");
        if (host) {
            if (!initialStart) {
                startDelayed(2000);
                initialStart = true;
            }
        }
        //the connection is not lost
        connectionLost = false;
    }

    @Override
    public void onBallStickCollission(PlayerStick p) {
        if (getBottomPlayer() == p) {
            gameState++;
        }
    }

    @Override
    public void onBallGoalCollission(Goal g) {
        getBall().setSpeed(0);
        getBall().setBallVelocity(0, 0);

        if (g == getBottomGoal()) {
            g.goalScored();
            resetBallPosition();
            startDelayed(2000);
            gameState++;
        }
    }

    public void registerNetworkControl(NetworkControl networkControl) {
        this.networkPlayerControl = networkControl;
    }

    public GameDataExchanger.Stats getStats() {
        return gameDataExchanger.getStats();
    }
}
