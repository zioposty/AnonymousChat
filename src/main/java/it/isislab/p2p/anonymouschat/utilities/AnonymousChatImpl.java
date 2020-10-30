package it.isislab.p2p.anonymouschat.utilities;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;


public class AnonymousChatImpl implements AnonymousChat {

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT=4000;

    final private ArrayList<String> chat_joined =new ArrayList<String>();

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

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty())
                _dht.put(Number160.createHash(_room_name)).data(new Data(new HashSet<PeerAddress>())).start().awaitUninterruptibly();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean joinRoom(String _room_name) {

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if(futureGet.isEmpty() ) return false;
                HashSet<PeerAddress> peers_on_topic;
                peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
                peers_on_topic.add(_dht.peer().peerAddress());
                _dht.put(Number160.createHash(_room_name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
                chat_joined.add(_room_name);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean leaveRoom(String _room_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if(futureGet.isEmpty() ) return false;
                HashSet<PeerAddress> peers_on_topic;
                peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
                peers_on_topic.remove(_dht.peer().peerAddress());
                _dht.put(Number160.createHash(_room_name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
                chat_joined.remove(_room_name);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean sendMessage(String _room_name, Object _text_message) {

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                HashSet<PeerAddress> peers_on_topic;
                peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
                for(PeerAddress peer:peers_on_topic)
                {
                    FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(_text_message).start();
                    futureDirect.awaitUninterruptibly();
                }

                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean leaveNetwork() {

        for(String room: new ArrayList<String>(chat_joined)) leaveRoom(room);
        _dht.peer().announceShutdown().start().awaitUninterruptibly();
        return true;
    }

}
