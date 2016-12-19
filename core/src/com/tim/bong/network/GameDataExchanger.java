package com.tim.bong.network;

import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.world.GameWorldNetworkManager;
import com.tim.bong.util.ByteStuff;

import java.io.IOException;
import java.net.*;

public class GameDataExchanger {
    private DatagramSocket socket;
    private DatagramPacket packetA;

    private Thread receiverThread;
    private int myId;
    private Frame frame;

    private final PlayerStick playerStick;
    private final Ball ball;
    private final GameWorldNetworkManager gameWorldNetworkManager;

    private int lastFrameReceived;
    private boolean running;

    public GameDataExchanger(int myId, GameWorldNetworkManager gameWorldNetworkManager) {
        this.myId = myId;
        this.playerStick = gameWorldNetworkManager.getBottomPlayer();
        this.ball = gameWorldNetworkManager.getBall();
        this.gameWorldNetworkManager = gameWorldNetworkManager;
        lastFrameReceived = 0;

        frame = new Frame(myId);

        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("tv_test.dd-dns.de");
            packetA = new DatagramPacket(new byte[0], 0, 0, address, 3844);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        receiverThread = getReceiverThread();
        receiverThread.start();
    }

    public void sendUpdate() {
        try {
            packetA.setData(frame.build(lastFrameReceived, playerStick, ball, gameWorldNetworkManager.getGameState()));
            System.out.println("Sending data");
            socket.send(packetA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread getReceiverThread() {
        final DatagramPacket packet = new DatagramPacket(new byte[44], 44);

        running = true;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        socket.receive(packet);
                        byte[] data = packet.getData();
                        int senderId = ByteStuff.readInt(data, 0);
                        int frameId = ByteStuff.readInt(data, 4);
                        int lastAck = ByteStuff.readInt(data, 8);

                        if (senderId == -myId && frameId > lastFrameReceived) {
                            System.out.println("Received new data");
                            gameWorldNetworkManager.newData(data);
                            lastFrameReceived = frameId;
                        } else if (senderId == myId) {
                            System.out.println("Waiting for other player");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void close() {
        running = false;
        socket.close();
    }
}