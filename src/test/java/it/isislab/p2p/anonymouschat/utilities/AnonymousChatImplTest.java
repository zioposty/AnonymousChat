package it.isislab.p2p.anonymouschat.utilities;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

            try{
                lock.lock();
                receivedMessages[peer].add((MessageP2P) obj);
                condition.notify();
            }
            finally {
                lock.unlock();
            }

        return "success";
    }
    }

    private static ArrayList<MessageP2P>[] receivedMessages = new ArrayList[4];
    private static AnonymousChatImpl p0, p1, p2, p3;
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

   // JFXPanel panel = new JFXPanel();

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

    /**
     * Dopo la creazione di una stanza, non posso crearne una con lo stesso nome
     */
    @Test
    void createRoomTest() {
        assertTrue(p1.createRoom("createRoom1"), "createRoom1 non creata");
        assertFalse(p2.createRoom("createRoom1"), "creata una stanza già presente");
        assertTrue(p3.createRoom("createRoom2"), "createRoom2 non creata");
    }

    @Test
    void joinRoomTest() {
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
    void leaveRoomTest() {
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
    void sendMessageTest() throws InterruptedException {

        clearMessages();

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

        checkMessages();

    }



    public void leaveChats(AnonymousChatImpl peer){
        for (String s:
            new ArrayList<>(peer.getChatJoined()) ) {

            peer.leaveRoom(s);
        }
    }

    private void checkMessages() throws InterruptedException {
        try {
            lock.lock();
            while (receivedMessages[0].size() <2) { condition.await(); }
            assertEquals(m0.getMessage(), receivedMessages[0].get(0).getMessage());
            assertEquals(m1.getMessage(), receivedMessages[0].get(1).getMessage());
        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while (receivedMessages[1].size() <2) { condition.await(); }
            assertEquals(m0.getMessage(), receivedMessages[1].get(0).getMessage());
            assertEquals(m1.getMessage(), receivedMessages[1].get(1).getMessage());
        }
        finally {
            lock.unlock();
        }


        try {
            lock.lock();
            while (receivedMessages[2].size() <2) { condition.await(); }
            assertEquals(m2.getMessage(), receivedMessages[2].get(0).getMessage());
            assertEquals(m3.getMessage(), receivedMessages[2].get(1).getMessage());
        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while (receivedMessages[3].size() <2) { condition.await(); }
            assertEquals(m2.getMessage(), receivedMessages[3].get(0).getMessage());
            assertEquals(m3.getMessage(), receivedMessages[3].get(1).getMessage());
        }
        finally {
            lock.unlock();
        }

        clearMessages();
    }

    void clearMessages(){
        for(int i = 0; i< 4; i++)
            receivedMessages[i].clear();
    }


    //----------
 /*   @Test
    void sendImageTest() throws InterruptedException {
        String r1 = "testSend";
        String imagePath1 = "testImages/testImage.png", imagePath2 = "testImages/testImage2.jpg";
        Image testImg1 = new Image(imagePath1);

        assertFalse(p0.sendImage(r1, "sms1", imagePath1), "messaggio inviato in una stanza mai creata!");
        p0.createRoom(r1);
        assertFalse(p0.sendImage(r1, "sms1", imagePath2), "messaggio inviato in una stanza in cui non partecipi!");
        p0.joinRoom(r1);
        p1.joinRoom(r1);
        p2.joinRoom(r1);

       clearMessages();


        //------
        assertTrue(p0.sendImage(r1, "sms1", imagePath1), "errore nell'inviare immagine");

        Image img0= null, img1 = null, img2 = null;
        String mss0 ="", mss1="", mss2="";
        try {
            lock.lock();
            while(receivedMessages[0].size() == 0) {condition.await();}
            img0 = receivedMessages[0].get(0).getImage();
            mss0 = receivedMessages[0].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while(receivedMessages[1].size() == 0){condition.await();}
            img1 = receivedMessages[1].get(0).getImage();
            mss1 = receivedMessages[1].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while(receivedMessages[2].size() == 0) {condition.await();};
            img2 = receivedMessages[2].get(0).getImage();
            mss2 = receivedMessages[2].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }
        //---
        assertEquals("sms1", mss0, "errore ricezione messaggio peer0");
        assertEquals("sms1", mss1, "errore ricezione messaggio peer1");
        assertEquals("sms1", mss2, "errore ricezione messaggio peer2");

        assertTrue(assertEqualsImage(testImg1, img0), "errore ricezione immagine peer0");
        assertTrue(assertEqualsImage(testImg1, img1), "errore ricezione immagine peer1");
        assertTrue(assertEqualsImage(testImg1, img2), "errore ricezione immagine peer2");

        //------

        leaveChats(p0);
        leaveChats(p1);
        leaveChats(p2);
    }   */
    private boolean assertEqualsImage(Image expected, Image current) {
        if(expected.getWidth() != current.getWidth() || expected.getHeight() != current.getHeight()) {
            return false;
        }

        int width  = (int) expected.getWidth();
        int height = (int) current.getHeight();

        PixelReader p1 = expected.getPixelReader();
        PixelReader p2 = expected.getPixelReader();

        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                if (p1.getArgb(j, i) != p2.getArgb(j, i)) return false;
        }
        return true;
    }


    @Test
    void broadCastMessageTest() throws InterruptedException {
        String r1= "broad1", r2 = "broad2";
        String sms = "testMessage";
        p0.createRoom(r1);
        p0.createRoom(r2);
        p0.joinRoom(r1);
        p1.joinRoom(r1);

        p0.joinRoom(r2);
        p2.joinRoom(r2);
        p3.joinRoom(r2);

        receivedMessages[0].clear();
        receivedMessages[1].clear();
        receivedMessages[2].clear();
        receivedMessages[3].clear();

        //----

        assertTrue(p0.broadcast(sms), "Errore durante il broadcast del peer0");

        String mss0_0 ="", mss0_1 ="", mss1="", mss2="", mss3 ="";

        try {
            lock.lock();
            while(receivedMessages[0].size() < 2) {condition.await();}
            mss0_0 = receivedMessages[0].get(0).getMessage();
            mss0_1 = receivedMessages[0].get(1).getMessage();
        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while(receivedMessages[1].size() == 0){condition.await();}
            mss1 = receivedMessages[1].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while(receivedMessages[2].size() == 0){condition.await();}
            mss2 = receivedMessages[2].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }

        try {
            lock.lock();
            while(receivedMessages[3].size() == 0){condition.await();}
            mss3 = receivedMessages[3].get(0).getMessage();

        }
        finally {
            lock.unlock();
        }


        assertEquals(sms, mss0_0, "errore ricezione messaggio peer0");
        assertEquals(sms, mss0_1, "errore ricezione messaggio peer0");
        assertEquals(sms, mss1, "errore ricezione messaggio peer1");
        assertEquals(sms, mss2, "errore ricezione messaggio peer2");
        assertEquals(sms, mss3, "errore ricezione messaggio peer3");



        leaveChats(p0);
        leaveChats(p1);
        leaveChats(p2);
        leaveChats(p3);
    }


    @AfterEach
    public void leaveAll(){
        leaveChats(p0);
        leaveChats(p1);
        leaveChats(p2);
        leaveChats(p3);
    }


    @AfterAll
    static void leaveNetwork() {
        assertTrue(p1.leaveNetwork(), "impossibile abbandonare la rete!");
        assertTrue(p2.leaveNetwork(), "impossibile abbandonare la rete!");
        assertTrue(p3.leaveNetwork(), "impossibile abbandonare la rete!");
        assertTrue(p0.leaveNetwork(), "impossibile abbandonare la rete!");

        assertEquals(0 , p0.getChatJoined().size());
        assertEquals(0 , p1.getChatJoined().size());
        assertEquals(0 , p2.getChatJoined().size());
        assertEquals(0 , p3.getChatJoined().size());


    }


}