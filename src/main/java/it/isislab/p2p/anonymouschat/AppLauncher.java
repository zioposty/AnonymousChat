package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.PeerManager;
import javafx.application.Application;


/**
 * AppLauncher
 */
public class AppLauncher {

    public static void main(String[] args) {
        String master = "127.0.0.1";
        int id = 0;

        PeerManager peer = PeerManager.getInstance();

        if(args.length!=0) {
            if (args.length == 2) {
                id = Integer.parseInt(args[0]);
                master = args[1];
            } else System.exit(-1);
        }
        try {
            peer.init(id, master);
        } catch (Exception e) {
            System.out.println("Error with connection. App will be closed");
            System.exit(-1);
        }
        System.out.println(master + " " + id);

        Application.launch(InitialManager.class, args);
    }
}

