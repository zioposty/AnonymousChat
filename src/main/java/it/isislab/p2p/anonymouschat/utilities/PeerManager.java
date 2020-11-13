package it.isislab.p2p.anonymouschat.utilities;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
/*
*   Singleton per mantenere le informazioni del peer
 */
public class PeerManager {


    class MessageListenerImpl implements MessageListener {

        public Object parseMessage(Object obj) {

            MessageP2P mss = (MessageP2P) obj;

            //To edit UI, i'm to be sure to work on FX-main-thread
            Platform.runLater(() -> {
                TextFlow chat = chatJoined.get(mss.getRoom());
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, HH:mm");
                Text message = new Text(mss.getMessage() + "\n");
                message.setFont(Font.font(20.0));
                Label hour = new Label(formatter.format(date));
                hour.setAlignment(Pos.CENTER_RIGHT);
                hour.setFont(Font.font(15.0));
                hour.setPrefWidth(chat.getWidth() - 30.0);
                // Separator
                final Separator separator = new Separator(Orientation.HORIZONTAL);
                separator.prefWidthProperty().bind(chat.widthProperty());
                separator.setStyle("-fx-background-color: #484848;");
                chat.getChildren().addAll(message, hour, separator);



            });

            return "success";
        }


    }

    private static PeerManager instance;
    private AnonymousChatImpl peer = null;

    static {
        try {
            instance = new PeerManager();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred!", e);
        }
    }

    private int id = -1;
    private String master = null;

    private PeerManager() { }

    final private HashMap<String, TextFlow> chatJoined = new HashMap<>();

    public boolean init(int _id, String _master) throws Exception {
        if( id != -1  && master != null && peer != null) return false;
        id = _id;
        master = _master;

        peer = new AnonymousChatImpl(id, master, new MessageListenerImpl());


        return true;
    }

    public static PeerManager getInstance(){
        return instance;
    }

    public AnonymousChatImpl getPeer(){ return peer;}

    public HashMap getChat() { return chatJoined; }
    public void addChat(String roomName, TextFlow chat) { chatJoined.put(roomName, chat); }
    public boolean isChatJoined(String chatName){   return chatJoined.containsKey((chatName)); }
    public void removeChat(String roomName){ chatJoined.remove(roomName); }
}
