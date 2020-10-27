package it.isislab.p2p.anonymouschat.utilities;

public class PeerManager {

    static PeerManager instance;

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

    public boolean init(int _id, String _master)
    {
        if( id != -1  && master != null) return false;
        id = _id;
        master = _master;

        return true;
    }

    public static PeerManager getInstance(){
        return instance;
    }



}
