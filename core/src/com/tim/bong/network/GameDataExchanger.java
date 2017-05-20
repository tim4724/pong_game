package com.tim.bong.network;

import com.badlogic.gdx.math.MathUtils;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.Goal;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.actor.PublicBall;
import com.tim.bong.game.world.GameWorldNetworkManager;
import com.tim.bong.network.payload.GamePayload;
import com.tim.bong.network.payload.Payload;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class GameDataExchanger {

    private final GameWorldNetworkManager gameWorldNetworkManager;
    private final Thread receiver;
    private final Sender sender;
    private boolean connected;

    private Stats stats;
    private PublicBall remoteBall;
    private float remotePlayerX;
    private float remotePlayerAngle;
    private int remoteScore;
    private int remoteGameState;

    public GameDataExchanger(GameWorldNetworkManager gameWorldNetworkManager, DatagramSocket socket, InetSocketAddress targetAddress, int myId, int opponentId) {
        this.gameWorldNetworkManager = gameWorldNetworkManager;
        sender = new Sender(socket, targetAddress, myId, opponentId);
        remoteBall = new RemoteBall();
        receiver = new Receiver(socket, myId, opponentId);
        receiver.start();
        stats = new Stats();
    }

    public void update(PlayerStick playerStick, Ball ball, Goal goal, int gameState) {
        sender.send(playerStick, ball, goal, gameState);
    }

    private class Sender {
        private final DatagramSocket socket;
        private final DatagramPacket packet;
        private final GamePayload gamePayload;

        Sender(DatagramSocket socket, InetSocketAddress targetAddress, int myId, int otherId) {
            this.socket = socket;
            gamePayload = new GamePayload(myId, otherId);

            byte[] data = gamePayload.getBuffer();
            packet = new DatagramPacket(data, data.length);
            packet.setSocketAddress(targetAddress);
        }

        void send(PlayerStick playerStick, Ball ball, Goal goal, int gameState) {
            try {
                long pongTime = stats.lastPacketReceivedSendTime + (System.currentTimeMillis() - stats.lastPacketReceivedTime);

                byte[] data = gamePayload.build(playerStick, ball, goal, gameState, ++stats.lastSentId, stats.lastReceivedId, pongTime);
                //packet.setData(data);
                socket.send(packet);

                stats.packetsSent++;
                //stats.lastSentId = gamePayload.getPacketId();
            } catch (IOException e) {
                connectionLost(e);
            }
        }
    }

    private class Receiver extends Thread {
        private final DatagramSocket socket;
        private final int myId, opponenntId;

        Receiver(DatagramSocket socket, int myId, int opponenntId) {
            this.socket = socket;
            this.myId = myId;
            this.opponenntId = opponenntId;
        }

        @Override
        public void run() {
            System.out.println("receiver start");
            GamePayload gamePayload = new GamePayload();
            byte[] buffer = gamePayload.getBuffer();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                try {
                    socket.receive(packet);
                    gamePayload.parse(packet.getData());
                    if (gamePayload.getPacketId() > stats.lastReceivedId
                            && gamePayload.getSenderId() == opponenntId && gamePayload.getReceiverId() == myId) {
                        connected();
                        stats.packetsReceived++;
                        parseData(gamePayload);
                        parseGameData(gamePayload);
                    }
                } catch (Exception e) {
                    connectionLost(e);
                }
            }
        }
    }

    private void parseData(final Payload payload) {
        stats.lastReceivedId = payload.getPacketId();
        stats.lastAckReceived = payload.getAck();
        stats.lastPacketReceivedSendTime = payload.getSendTime();
        stats.lastPacketReceivedTime = System.currentTimeMillis();

        stats.rtt = (int) (System.currentTimeMillis() - payload.getPongTime());
        stats.packetsDropped += (payload.getPacketId() - stats.lastReceivedId);
    }

    private void parseGameData(final GamePayload gamePayload) {
        remotePlayerX = gameWorldNetworkManager.getWidth() - gamePayload.getStickX();//mirror the player position
        remotePlayerAngle = gamePayload.getStickAngle() + MathUtils.PI;//adjust stick rotation
        //mirror the ball coordinates and invert the ball velocity
        float ballX = gameWorldNetworkManager.getWidth() - gamePayload.getBallX();
        float ballY = gameWorldNetworkManager.getHeight() - gamePayload.getBallY();
        ((RemoteBall) remoteBall).set(ballX, ballY, -gamePayload.getBallVelX(), -gamePayload.getBallVelY(), gamePayload.getBallSpeed());
        remoteScore = gamePayload.getScore();
        remoteGameState = gamePayload.getGamestate();
    }

    private void connected() {
        if (!connected) {
            connected = true;
            gameWorldNetworkManager.connected();
        }
    }

    private void connectionLost(Exception e) {
        e.printStackTrace();
        connected = false;
        stats.rtt = 999;
        gameWorldNetworkManager.connectionLost();
    }

    public Stats getStats() {
        return stats;
    }

    public PublicBall getRemoteBall() {
        return remoteBall;
    }

    public float getRemotePlayerX() {
        return remotePlayerX;
    }

    public float getRemotePlayerAngle() {
        return remotePlayerAngle;
    }

    public int getRemoteScore() {
        return remoteScore;
    }

    public int getRemoteGameState() {
        return remoteGameState;
    }

    public class Stats {
        private int lastReceivedId, rtt, packetsSent, packetsReceived, packetsDropped, lastAckReceived, lastSentId;
        private long lastPacketReceivedSendTime, lastPacketReceivedTime;

        public int getRtt() {
            return rtt;
        }

        public int getPacketsSent() {
            return packetsSent;
        }

        public int getPacketsReceived() {
            return packetsReceived;
        }

        public int getPacketsDropped() {
            return packetsDropped;
        }

        public int getLastReceivedId() {
            return lastReceivedId;
        }

        public int getLastSentId() {
            return lastSentId;
        }

        public int getLastAckReceived() {
            return lastAckReceived;
        }
    }

    private class RemoteBall implements PublicBall {
        float x, y, velX, velY, speed;

        public void set(float x, float y, float velX, float velY, float speed) {
            this.x = x;
            this.y = y;
            this.velX = velX;
            this.velY = velY;
            this.speed = speed;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getVelX() {
            return velX;
        }

        @Override
        public float getVelY() {
            return velY;
        }

        @Override
        public float getSpeed() {
            return speed;
        }
    }
}