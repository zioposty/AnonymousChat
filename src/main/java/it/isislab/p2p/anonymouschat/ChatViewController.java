package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.MessageP2P;
import it.isislab.p2p.anonymouschat.utilities.PeerManager;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

public class ChatViewController {

    public MenuItem menuQuit;
    private PeerManager manager = PeerManager.getInstance();

    public void publishMessage(TextArea chat, MessageP2P message){}

    public void closeApplication(ActionEvent actionEvent) {

        //manager.getPeer().leaveNetwork();
        System.exit(0);
    }
}
