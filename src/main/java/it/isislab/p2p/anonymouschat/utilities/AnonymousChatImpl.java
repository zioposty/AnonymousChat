package it.isislab.p2p.anonymouschat.utilities;

import java.net.InetAddress;
import java.util.ArrayList;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class AnonymousChatImpl implements AnonymousChat {



    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT=4000;

    final private ArrayList<String> s_topics=new ArrayList<String>();

    public AnonymousChatImpl( int _id, String _master_peer, final MessageListener _listener) throws Exception
    {
        //preparazione a poter entrare nella rete
        peer= new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT+_id).start();
        _dht = new PeerBuilderDHT(peer).start();

        //Inizio tentativo di connessione ad uno dei master
        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if(fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }else {
            throw new Exception("Error in master peer bootstrap.");
        }

        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                return _listener.parseMessage(request);
            }
        });
    }

    @Override
    public boolean createRoom(String _room_name) {
        return false;
    }

    @Override
    public boolean joinRoom(String _room_name) {
        return false;
    }

    @Override
    public boolean leaveRoom(String _room_name) {
        return false;
    }

    @Override
    public boolean sendMessage(String _room_name, String _text_message) {
        return false;
    }
}
