package it.isislab.p2p.anonymouschat.utilities;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class MessageP2P implements Serializable {

    private String room;
    private String message;
    private SerializableImage image;

    public MessageP2P(){}

    public MessageP2P(String room, String message) {
        this.room = room;
        this.message = message;
    }

    public MessageP2P(String room, String message, String path) throws FileNotFoundException {
        this.room = room;
        this.message = message;
        setImage(path);
    }


    public void setImage(String path) throws FileNotFoundException {
        image = new SerializableImage();
        image.setImage(new Image(new FileInputStream(path)));

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

    public SerializableImage getImage() {
        return image;
    }
}
