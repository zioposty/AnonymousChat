package it.isislab.p2p.anonymouschat.utilities;

import javax.swing.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageP2P implements Serializable {

    private String room;
    private String message;
    private ImageIcon image;

    public MessageP2P(){}

    public MessageP2P(String room, String message) {
        this.room = room;
        this.message = message;
    }

    public MessageP2P(String room, String message, String path) {
        this.room = room;
        this.message = message;
        image = new ImageIcon(path);
    }


    public void setImage(String path)
    {
        image = new ImageIcon(path);
    }

    public String getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageFormatted(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M HH:mm");
        return message + "\n\t\t\t\t\t\t" + formatter.format(date) + "\n----------------\n";
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
