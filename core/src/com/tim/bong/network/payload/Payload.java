package com.tim.bong.network.payload;

import com.tim.bong.util.ByteStuff;

import static com.tim.bong.util.ByteStuff.*;

public abstract class Payload {
    private static final int senderIdIndex = 0, receiverIdIndex = 4, packetIdIndex = 8, ackIndex = 12, sendTimeIndex = 16, pongTimeIndex = 24;
    static final int length = 32;

    byte[] buffer;

    private int senderId, receiverId, ack, packetId;
    private long sendTime, pongTime;

    Payload() {
    }

    Payload(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public void parse(byte[] data) {
        senderId = ByteStuff.readInt(data, senderIdIndex);
        receiverId = ByteStuff.readInt(data, receiverIdIndex);
        packetId = readInt(data, packetIdIndex);
        ack = readInt(data, ackIndex);
        sendTime = readLong(data, sendTimeIndex);
        pongTime = readLong(data, pongTimeIndex);
    }

    public byte[] build(int packetId, int ack, long pongTime) {
        putInt(senderId, buffer, senderIdIndex);
        putInt(receiverId, buffer, receiverIdIndex);
        putInt(packetId, buffer, packetIdIndex);
        putInt(ack, buffer, ackIndex);
        putLong(System.currentTimeMillis(), buffer, sendTimeIndex);
        putLong(pongTime, buffer, pongTimeIndex);
        return buffer;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getPacketId() {
        return packetId;
    }

    public int getAck() {
        return ack;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public long getPongTime() {
        return pongTime;
    }

    public long getSendTime() {
        return sendTime;
    }
}
