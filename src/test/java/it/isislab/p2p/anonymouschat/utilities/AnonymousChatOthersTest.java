package it.isislab.p2p.anonymouschat.utilities;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.PixelReader;
import org.junit.jupiter.api.*;
import javafx.scene.image.Image;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class AnonymousChatOthersTest {


    static class MessageListenerImpl implements MessageListener
    {
        int peer;
        public MessageListenerImpl(int peer){
            this.peer = peer;
        }

        @Override
        public Object parseMessage(Object obj) {

            receivedMessages[peer].add(((MessageP2P) obj));
            return "success";
        }
    }


   // private JFXPanel panel = new JFXPanel();

    private static ArrayList<MessageP2P>[] receivedMessages = new ArrayList[4];
    private static AnonymousChatImpl p0, p1, p2, p3;

    @BeforeAll
    static void createPeers() throws Exception {
        p0 = new AnonymousChatImpl(0, "127.0.0.1", new MessageListenerImpl(0));
        p1 = new AnonymousChatImpl(1, "127.0.0.1", new MessageListenerImpl(1));
        p2 = new AnonymousChatImpl(2, "127.0.0.1", new MessageListenerImpl(2));
        p3 = new AnonymousChatImpl(3, "127.0.0.1", new MessageListenerImpl(3));

        assertThrows(Exception.class, () -> {new AnonymousChatImpl(0, "127.0.0.2", new AnonymousChatImplTest.MessageListenerImpl(0));});

        receivedMessages[0] = new ArrayList<>();
        receivedMessages[1] = new ArrayList<>();
        receivedMessages[2] = new ArrayList<>();
        receivedMessages[3] = new ArrayList<>();

    }

    @Test
   /* void sendImageTest() throws InterruptedException{
        String r1 = "testSend";
        String imagePath1 = "testImages/testImage.png", imagePath2 = "testImages/testImage2.jpg";
        Image testImg1 = new Image(imagePath1);

        assertFalse(p0.sendImage(r1, "sms1", imagePath1), "messaggio inviato in una stanza mai creata!");
        p0.createRoom(r1);
        assertFalse(p0.sendImage(r1, "sms1", imagePath2), "messaggio inviato in una stanza in cui non partecipi!");
        p0.joinRoom(r1);
        p1.joinRoom(r1);
        p2.joinRoom(r1);

        //------
        assertTrue(p0.sendImage(r1, "sms1", imagePath1) ,"errore nell'inviare immagine");
        sleep(1000);

        Image img0 = receivedMessages[0].get(0).getImage();
        String mss0 = receivedMessages[0].get(0).getMessage();

        Image img1 = receivedMessages[1].get(0).getImage();
        String mss1 = receivedMessages[1].get(0).getMessage();

        Image img2 = receivedMessages[2].get(0).getImage();
        String mss2 = receivedMessages[2].get(0).getMessage();

        assertEquals("sms1", mss0, "errore ricezione messaggio peer0");
        assertEquals("sms1", mss1, "errore ricezione messaggio peer1");
        assertEquals("sms1", mss2, "errore ricezione messaggio peer2");

        assertTrue(assertEqualsImage(testImg1, img0), "errore ricezione immagine peer0");

        //------

    }
*/
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
