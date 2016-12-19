package com.tim.bong.network;

import com.tim.bong.util.ByteStuff;

import java.io.IOException;
import java.net.*;

public class PeerConnectorHost {
    private DatagramSocket socket;
    private Thread receiverThread, senderThread;
    private int myId;

    private boolean running;
    private boolean receievedAddresses;

    private InetSocketAddress peerSocketAddressA, peerSocketAddressB;

    private int timeout = 30000;

    public PeerConnectorHost(int myId) {
        this.myId = myId;
        System.out.println("HOST");
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("tv_test.dd-dns.de");
            int localAddress = ByteStuff.readInt(InetAddress.getLocalHost().getAddress(), 0);
            DatagramPacket packet = new DatagramPacket(new byte[0], 0, 0, serverAddress, 3845);

            System.out.println("Trying to connect to " + packet.getAddress().getHostAddress());

            senderThread = getSenderThread(packet, localAddress);
            senderThread.start();
            receiverThread = getReceiverThread();
            receiverThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public Thread getSenderThread(final DatagramPacket packet, final int localAddress) {
        final byte[] data = new byte[16];

        ByteStuff.putInt(myId, data, 0);
        ByteStuff.putInt(0, data, 4);
        ByteStuff.putInt(localAddress, data, 8);
        ByteStuff.putInt(socket.getLocalPort(), data, 12);
        packet.setData(data);

        running = true;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                int sleep = 250;
                while (running) {
                    if (receievedAddresses && myId > 0) {
                        //try to connect directly to peer
                        if (i == 0) {
                            packet.setSocketAddress(peerSocketAddressA);
                            ByteStuff.putInt(-2, packet.getData(), 4);
                            System.out.println("state: " + -2);
                            System.out.println("Trying to connect to " + packet.getAddress().getHostAddress());
                        }
                        if (i == 30000 / sleep) {
                            packet.setSocketAddress(peerSocketAddressB);
                            ByteStuff.putInt(-3, packet.getData(), 4);
                            System.out.println("state: " + -3);
                            System.out.println("Trying to connect to " + packet.getAddress().getHostAddress());
                        }
                        if (i == 60000 / sleep) {
                            //failed();
                            break;
                        }
                        i++;
                    }

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
                while (true) {
                    try {
                        socket.receive(packet);
                        InetSocketAddress senderAddress = (InetSocketAddress) packet.getSocketAddress();

                        byte[] data = packet.getData();
                        int senderId = ByteStuff.readInt(data, 0);
                        int state = ByteStuff.readInt(data, 4);

                        System.out.println("Received packet from " + senderAddress.getAddress().getHostAddress());

                        if (senderId == -myId) {
                            System.out.println("state: " + state);
                            if(state == -1 && !receievedAddresses) {
                                //packet from opponnent
                                //parse addresses
                                byte[] temp = ByteStuff.subBytes(data, 8, 12);
                                InetAddress privateAddress = InetAddress.getByAddress(temp);
                                int privatePort = ByteStuff.readInt(data, 12);
                                peerSocketAddressA = new InetSocketAddress(privateAddress, privatePort);

                                temp = ByteStuff.subBytes(data, 16, 20);
                                InetAddress publicAddress = InetAddress.getByAddress(temp);
                                int publicPort = ByteStuff.readInt(data, 20);
                                peerSocketAddressB = new InetSocketAddress(publicAddress, publicPort);

                                System.out.println("Received addresses");
                                receievedAddresses = true;
                            }

                            if(state == -4) {
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