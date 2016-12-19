package com.tim.bong.network;

import com.tim.bong.util.ByteStuff;

import java.io.IOException;
import java.net.*;

public class PeerConnectorGuest {
    private DatagramSocket socket;

    private Thread receiverThread, senderThread;
    private int myId;
    private final DatagramPacket packet;

    private boolean running;
    private boolean receievedAddresses;
    private int timeout = 30000;

    public PeerConnectorGuest(int myId) {
        this.myId = myId;
        System.out.println("GUEST");
        packet = new DatagramPacket(new byte[0], 0);
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("tv_test.dd-dns.de");
            packet.setAddress(serverAddress);
            packet.setPort(3845);
            int localAddress = ByteStuff.readInt(InetAddress.getLocalHost().getAddress(), 0);

            System.out.println("Trying to connect to " + packet.getAddress().getHostAddress());

            senderThread = getSenderThread(localAddress);
            senderThread.start();
            receiverThread = getReceiverThread();
            receiverThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public Thread getSenderThread(final int localAddress) {
        final byte[] data = new byte[16];

        ByteStuff.putInt(myId, data, 0);
        ByteStuff.putInt(-1, data, 4);
        ByteStuff.putInt(localAddress, data, 8);
        ByteStuff.putInt(socket.getLocalPort(), data, 12);
        packet.setData(data);

        running = true;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int sleep = 250;
                while (running) {
                    try {
                        socket.send(packet);
                        Thread.sleep(sleep);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void establishePeerToPeerConnection(InetSocketAddress peerAddress){
        System.out.println("Trying to connect to " + peerAddress.getAddress().getHostAddress());
        synchronized (packet) {
            ByteStuff.putInt(-4, packet.getData(), 4);
            packet.setSocketAddress(peerAddress);
        }
    }

    private void startGame() {
        System.out.println("Starting game");
        running = false;
    }

    public Thread getReceiverThread() {
        final DatagramPacket packet = new DatagramPacket(new byte[24], 24);

        receievedAddresses = false;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        socket.receive(packet);
                        InetSocketAddress senderAddress = (InetSocketAddress) packet.getSocketAddress();
                        byte[] data = packet.getData();
                        int senderId = ByteStuff.readInt(data, 0);
                        int state = ByteStuff.readInt(data, 4);
                        System.out.println("Received packet from " + senderAddress.getAddress().getHostAddress());

                        if (senderId == -myId) {
                            System.out.println("state: " + state);
                            if(state == -2 || state == -3) {
                                //this is a packet directly from the other player
                                establishePeerToPeerConnection(senderAddress);
                            }
                            if(state == -5){
                                startGame();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}