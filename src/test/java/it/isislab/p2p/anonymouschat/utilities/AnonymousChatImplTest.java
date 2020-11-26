package it.isislab.p2p.anonymouschat.utilities;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class AnonymousChatImplTest {

    static class MessageListenerImpl implements MessageListener
    {
        int peer;
        public MessageListenerImpl(int peer){
            this.peer = peer;
        }

        @Override
        public Object parseMessage(Object obj) {
            return "success";
        }
    }

    private static AnonymousChatImpl p0, p1, p2, p3;

    @BeforeAll
    static void createPeers() throws Exception {
        p0 = new AnonymousChatImpl(0, "127.0.0.1", new MessageListenerImpl(0));
        p1 = new AnonymousChatImpl(1, "127.0.0.1", new MessageListenerImpl(1));
        p2 = new AnonymousChatImpl(2, "127.0.0.1", new MessageListenerImpl(2));
        p3 = new AnonymousChatImpl(3, "127.0.0.1", new MessageListenerImpl(3));
    }
    @AfterAll
    static void disconnectAll()
    {
        p1.leaveNetwork();
        p2.leaveNetwork();
        p3.leaveNetwork();
        p0.leaveNetwork();
    }
    /**
     * Dopo la creazione di una stanza, non posso crearne una con lo stesso nome
     */
    @Test
    void createRoom() {
        assertTrue(p1.createRoom("createRoom1"), "stanza creata");
        assertFalse(p2.createRoom("createRoom1"), "stanza già presente");
        assertTrue(p3.createRoom("createRoom2"), "stanza creata");
    }

    @Test
    void joinRoom() {
        assertFalse(p1.joinRoom("room1"), "Non puoi unirti ad una stanza non creata");
        p1.createRoom("room1");
        assertTrue(p1.joinRoom("room1"));

        ArrayList<String> rooms = p1.getChatJoined();
        assertTrue(rooms.contains("room1"), "Unito correttamente alla stanza");

        assertTrue(p2.joinRoom("room1"));
        rooms = p2.getChatJoined();
        assertTrue(rooms.contains("room1"), "Unito correttamente alla stanza");

        assertFalse(p2.joinRoom("room1"));

    }

    @Test
    void leaveRoom() {
    }

    @Test
    void sendMessage() {
    }



    @Test
    void leaveNetwork() {
        p1.leaveNetwork();
        p2.leaveNetwork();
        p3.leaveNetwork();
        p0.leaveNetwork();
    }
}