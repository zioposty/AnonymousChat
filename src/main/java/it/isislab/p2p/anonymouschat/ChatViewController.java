package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.MessageP2P;
import it.isislab.p2p.anonymouschat.utilities.PeerManager;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.Optional;

public class ChatViewController {

    public MenuItem menuQuit;
    public TabPane chatTabs;

    private PeerManager manager = PeerManager.getInstance();
    private final double[] CHAT_SIZE = { 392.0, 418.0 };  //height, width

    //----------

    public void publishMessage(TextArea chat, MessageP2P message){}


    public void closeApplication(ActionEvent actionEvent) {

        //manager.getPeer().leaveNetwork();
        System.exit(0);
    }

    private void removeChat(Event e){
        Tab tab1 = (Tab) e.getSource();
        TextArea t = (TextArea) tab1.getContent().lookup("TextArea");
        manager.removeChat(t.getId());
        //manager.getPeer().leaveRoom(t.getId());
        System.out.println("Leaving " + t.getId());
        System.out.println(manager.getChat().toString());
    }

    public void joinRoom(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("roomName");
        dialog.setTitle("Join room");
        dialog.setHeaderText("Insert the name of an existing room that you wonna join");
        dialog.setContentText("Write here the room name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String roomName = result.get();
            //if(manager.getPeer().joinRoom(roomName))
            if (true)
                addTabChat(roomName);
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Operation failed");
                alert.setContentText("Something went wrong, maybe the room doesn't exist");

                alert.showAndWait();
            }
        }
    }


    public boolean confirmJoinRoom(String name){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Join room");
        alert.setHeaderText("Do you wonna join the created room: " + name);
        alert.setContentText("Choose your option");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");

        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == yes;

    }

    public void createRoom(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("roomName");
        dialog.setTitle("Create room");
        dialog.setHeaderText("Insert the name for the room that you wonna create (it must be unique)");
        dialog.setContentText("Write here the room name:");

        Optional<String> result =  dialog.showAndWait();
        if(result.isPresent()) {
            String roomName = result.get();
                //if(manager.getPeer().createRoom(roomName))
                if(false)
                    if(confirmJoinRoom(roomName)){
                        addTabChat(roomName);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Room created");
                        alert.setHeaderText(null);
                        alert.setContentText("The room was created. You can join it later");

                        alert.showAndWait();
                    }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Operation failed");
                    alert.setContentText("Something went wrong, maybe the name isn't unique");
                    alert.showAndWait();
                }


        }

    }

    private void addTabChat(String roomName) {
        TextArea chat = new TextArea();
        chat.setId(roomName);
        manager.addChat(roomName, chat);
        chat.setPrefHeight(CHAT_SIZE[0]);
        chat.setPrefWidth(CHAT_SIZE[1]);
        Tab tab = new Tab(roomName, chat);

        tab.setOnCloseRequest(e -> removeChat(e));
        chatTabs.getTabs().add(tab);


                /*
                Version with no Lamba expression

                tab.setOnCloseRequest(new EventHandler<Event>()
                {
                    @Override
                    public void handle(Event e)
                    {
                        Tab tab = (Tab) e.getSource();
                        TextArea t = (TextArea) tab.getContent().lookup("TextArea");
                        manager.removeChat(t.getId());
                        //manager.getPeer().leaveRoom(t.getId());
                        System.out.println("Leaving " + t.getId());
                        System.out.println(manager.getChat().toString());
                    }
                });
                */


    }
}
