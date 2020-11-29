package it.isislab.p2p.anonymouschat.utilities;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;
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

            receivedMessages[peer].add(((MessageP2P) obj).getMessage());
            return "success";
        }
    }

    private static ArrayList<String>[] receivedMessages = new ArrayList[4];
    private static AnonymousChatImpl p0, p1, p2, p3;

    private MessageP2P m0, m1, m2, m3;

    @BeforeAll
    static void createPeers() throws Exception {
        p0 = new AnonymousChatImpl(0, "127.0.0.1", new MessageListenerImpl(0));
        p1 = new AnonymousChatImpl(1, "127.0.0.1", new MessageListenerImpl(1));
        p2 = new AnonymousChatImpl(2, "127.0.0.1", new MessageListenerImpl(2));
        p3 = new AnonymousChatImpl(3, "127.0.0.1", new MessageListenerImpl(3));

        assertThrows(Exception.class, () -> {new AnonymousChatImpl(0, "127.0.0.2", new MessageListenerImpl(0));});

        receivedMessages[0] = new ArrayList<>();
        receivedMessages[1] = new ArrayList<>();
        receivedMessages[2] = new ArrayList<>();
        receivedMessages[3] = new ArrayList<>();

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
        assertTrue(p1.createRoom("createRoom1"), "createRoom1 non creata");
        assertFalse(p2.createRoom("createRoom1"), "creata una stanza già presente");
        assertTrue(p3.createRoom("createRoom2"), "createRoom2 non creata");
    }

    @Test
    void joinRoom() {
        assertFalse(p1.joinRoom("room1"), "unito ad una stanza non creata!");
        p1.createRoom("room1");
        assertTrue(p1.joinRoom("room1"), "Errore nel joinare una stanza");

        ArrayList<String> rooms = p1.getChatJoined();
        assertTrue(rooms.contains("room1"), "Lista delle chat non aggiornata correttamente");

        assertTrue(p2.joinRoom("room1"), "Errore nel joinare una stanza");
        rooms = p2.getChatJoined();
        assertTrue(rooms.contains("room1"), "Lista chat non aggiornata correttamente");

        assertFalse(p2.joinRoom("room1"));

    }

    @Test
    void leaveRoom() {
        //non puoi uscire da una stanza in cui non sie mai entrato
        assertFalse(p1.leaveRoom("roomToLeave"), "uscito da una stanza mai creata/joinata");
        p1.createRoom("roomToLeave");
        p1.joinRoom("roomToLeave");
        ArrayList<String> chatJoined = p1.getChatJoined();
        assertTrue(chatJoined.contains("roomToLeave"), "Lista chat non aggiornata");
        System.out.println(chatJoined.toString());
        assertTrue(p1.leaveRoom("roomToLeave"), "Errore nell'uscire da una stanza");
        assertFalse(chatJoined.contains("roomToLeave"), "Lista chat non aggiornata");

    }

    @Test
    void sendMessage() throws InterruptedException {
        String r1 = "testSend", r2 = "testSend2";
        assertFalse(p0.sendMessage(r1, "sms1"), "messaggio inviato in una stanza mai creata!");
        p0.createRoom(r1);
        assertFalse(p0.sendMessage(r1, "sms1"), "messaggio inviato in una stanza in cui non partecipi!");
        p0.joinRoom(r1);
        p1.joinRoom(r1);

        p2.createRoom(r2);
        p2.joinRoom(r2);
        p3.joinRoom(r2);


        m0 = new MessageP2P(r1, "sms0");
        m1 = new MessageP2P(r1, "sms1");
        m2 = new MessageP2P(r2, "sms2");
        m3 = new MessageP2P(r2, "sms3");


        assertTrue(p0.sendMessage(r1, m0.getMessage()), "errore nell'invio del messagio");
        assertTrue(p1.sendMessage(r1, m1.getMessage()), "errore nell'invio del messagio");
        assertTrue(p2.sendMessage(r2, m2.getMessage()), "errore nell'invio del messagio");
        assertTrue(p3.sendMessage(r2, m3.getMessage()), "errore nell'invio del messagio");

        //sleep per essere certo della ricezione dei messaggi
        sleep(2000);
        checkMessages();

    }

    private void checkMessages() {

        assertTrue(receivedMessages[1].contains(m0.getMessage()));
        assertTrue(receivedMessages[1].contains(m1.getMessage()));

        assertTrue(receivedMessages[1].contains(m0.getMessage()));
        assertTrue(receivedMessages[1].contains(m1.getMessage()));

        assertTrue(receivedMessages[2].contains(m2.getMessage()));
        assertTrue(receivedMessages[2].contains(m3.getMessage()));

        assertTrue(receivedMessages[3].contains(m2.getMessage()));
        assertTrue(receivedMessages[3].contains(m3.getMessage()));

    }

    @Test
    void leaveNetwork() {
        p1.leaveNetwork();
        p2.leaveNetwork();
        p3.leaveNetwork();
        p0.leaveNetwork();
    }
}