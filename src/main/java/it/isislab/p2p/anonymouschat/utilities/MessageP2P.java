package it.isislab.p2p.anonymouschat.utilities;

import java.io.Serializable;

public class MessageP2P implements Serializable {

    private String room;
    private String message;

    public MessageP2P(){}

    public MessageP2P(String room, String message) {
        this.room = room;
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
