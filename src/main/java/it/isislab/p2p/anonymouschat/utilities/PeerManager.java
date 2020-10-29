package it.isislab.p2p.anonymouschat.utilities;

import javafx.scene.control.TextArea;

import java.util.Dictionary;
import java.util.HashMap;

/*
*   Singleton per mantenere le informazioni del peer
 */
public class PeerManager {


    class MessageListenerImpl implements MessageListener {
        int peerid;

        public MessageListenerImpl(int peerid)
        {
            this.peerid=peerid;
        }
        public Object parseMessage(Object obj) {

            /* TODO: Ricevere messaggio ---> convertire in stringa ---> restituire messaggio
             *  formattato al chat manager
             */
            /*TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal terminal = textIO.getTextTerminal();
            terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
            return "success";*/
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

    final private HashMap<String, TextArea> chatJoined = new HashMap<>();

    public boolean init(int _id, String _master)
    {
        if( id != -1  && master != null && peer != null) return false;
        id = _id;
        master = _master;
        try {
            peer = new AnonymousChatImpl(id, master, new MessageListenerImpl(id));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static PeerManager getInstance(){
        return instance;
    }

    public AnonymousChat getPeer(){ return peer;}

    public HashMap getChat() { return chatJoined; }
    public void addChat(String roomName, TextArea chat) { chatJoined.put(roomName, chat); }
    public void removeChat(String roomName){ chatJoined.remove(roomName); }
}
