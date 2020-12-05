| AnonymousChat | Luca Postiglione | Architetture Distribuite per il Cloud |
| --- | --- | ---|  
     
     

# AnonymousChat: che cos'è?
Si tratta di una applicazione P2P che permette di unirsi o creare una o più chatroom in cui conversare, il tutto in maniera anonima.  

<img src="src/main/resources/Images/incognito.png" style="height: 150px; display: block;
    margin-left: auto;
    margin-right: auto;">  

## Come è stato sviluppato
AnonymousChat è sviluppato con il linguaggio Java, facendo uso di Maven come tool di build automation. La rete P2P è realizzata usufrunedo della libreria/framework [TomP2P](https://tomp2p.net/): nello specifico essa mette a disposizione una **DHT** (Distributed Hash Table), ovvero una tabella hash distribuita, la quale funziona con la classica infrastruttura chiave-valore. Per rendere l'applicazione più *user-friendly* è stato utilizzato [OpenJFX](https://openjfx.io) (noto in passato come JavaFX), una piattaforma opensource per la realizzazione di applicazioni client su diversi sistemi: desktop, mobile ma anche embedded che si basano su Java.

## Idea
Il progetto è stato realizzato per il corso di Architetture Distribuite per il Cloud, un esame del secondo anno della magistrale di informatica all'Università degli Studi di Salerno, usando come modello di riferimento l'esempio del professor Carmine Spagnuolo sul [paradigma Publish/Subscribe](https://github.com/spagnuolocarmine/p2ppublishsubscribe).  
    
    
# Panoramica della soluzione

A ciascun progetto è stata fornita una *Java interface* con i metodi obbligatori da implementare. Nel caso specifico, le operazioni richieste sono le seguenti:
```java
public interface AnonymousChat {
    /**
     * Creates new room.
     * @param _room_name a String the name identify the public chat room.
     * @return true if the room is correctly created, false otherwise.
     */
    public boolean createRoom(String _room_name);
    /**
     * Joins in a public room.
     * @param _room_name the name identify the public chat room.
     * @return true if join success, false otherwise.
     */
    public boolean joinRoom(String _room_name);
    /**
     * Leaves in a public room.
     * @param _room_name the name identify the public chat room.
     * @return true if leave success, false otherwise.
     */
    public boolean leaveRoom(String _room_name);
    /**
     * Sends a string message to all members of a  a public room.
     * @param _room_name the name identify the public chat room.
     * @param _text_message a message String value.
     * @return true if send success, false otherwise.
     */
    public boolean sendMessage(String _room_name, String _text_message);
}
```

Di base la realizzazione di una chat distribuita non si discosta molto da quella della realizzazione del *paradigma Publish/Subscribe*, di conseguenza molto del focus è stato sull'aggiunta di proprietà, grafica e metodi aggiuntivi:  
- Non limitarsi ad un programma in esecuzione all'interno di un terminale, ma realizzare una interfaccia grafica per poter velocizzare/migliorare l'esperienza durante l'uso di **AnonymousChat**;  
<img src="readmeImg/main.PNG">  
  
- Permettere una corretta visualizzazione delle chat, permettendo all'utente di scegliere quale visualizzare, ma allo stesso tempo essere sempre aggiornato di nuovi messaggi giunti nelle altre;
- Possibilità di inviare un messaggio a tutte le chat di cui fa parte;
- Integrare il messaggio con emoji ed eventualmente allegare anche delle immagini;
  <img src="readmeImg/chat.PNG">  
  
# Struttura della soluzione