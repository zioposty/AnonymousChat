package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.PeerManager;
import javafx.application.Application;

import org.kohsuke.args4j.Option;

/**
 * AppLauncher
 */
public class AppLauncher {

    @Option(name="-m", aliases="--masterip", usage="the master peer ip address", required=true)
    private static String master;

    @Option(name="-id", aliases="--identifierpeer", usage="the unique identifier for this peer", required=true)
    private static int id;


    public static void main(String[] args) {

        PeerManager peer = PeerManager.getInstance();
        //if(args.length > 0){ id = Integer.parseInt(args[0]); master = args[1]; }
        peer.init(id, master);
        System.out.println(master + " " + id);

        Application.launch(InitialManager.class, args);
    }
}

