package it.isislab.p2p.anonymouschat.utilities;

import java.io.Serializable;

public class MessageP2P implements Serializable {

    private int peerSend;
    private String room;
    private String message;

    public MessageP2P(int peerSend, String room, String message) {
        this.peerSend = peerSend;
        this.room = room;
        this.message = message;
    }
}
