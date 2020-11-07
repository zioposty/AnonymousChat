package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.MessageP2P;
import it.isislab.p2p.anonymouschat.utilities.PeerManager;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ChatViewController {

    public MenuItem menuQuit;
    public TabPane chatTabs;
    public TextArea messageField;

    private PeerManager manager = PeerManager.getInstance();
    private final double[] CHAT_SIZE = { 392.0, 418.0 };  //height, width

    //----------


    public void closeApplication(ActionEvent actionEvent) {

        manager.getPeer().leaveNetwork();
        System.exit(0);
    }

    private void removeChat(Event e){
        Tab tab1 = (Tab) e.getSource();
        TextArea t = (TextArea) tab1.getContent().lookup("TextArea");
        manager.removeChat(t.getId());
        manager.getPeer().leaveRoom(t.getId());
        //System.out.println("Leaving " + t.getId());
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
            if(manager.isChatJoined(roomName)){

                for(Tab t: chatTabs.getTabs())
                {
                    if(t.getText().equals(roomName)) {
                        chatTabs.getSelectionModel().select(t);
                        break;
                    }

                }

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ALREADY JOINED");
                alert.setHeaderText("Operation failed");
                alert.setContentText("You have already joined  the chat! You can't do it double");

                alert.showAndWait();
                return;
            }

            if(manager.getPeer().joinRoom(roomName))
            //if (true)
                addTabChat(roomName);
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Operation failed");
                alert.setContentText("Something went wrong, maybe the room doesn't exist");

                alert.showAndWait();
            }
        }
    }


    @SuppressWarnings("Duplicates")
    private boolean confirmJoinRoom(String name){
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
                if(manager.getPeer().createRoom(roomName))
                //if(true)
                    if(confirmJoinRoom(roomName)){
                        manager.getPeer().joinRoom(roomName);
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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
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
        chat.setEditable(false);
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

    public void sendMessage(ActionEvent actionEvent) {
        String mss = messageField.getText();
        MessageP2P message = new MessageP2P(chatTabs.getSelectionModel().getSelectedItem().getText(), mss);
        System.out.println("Sending: " + message.getMessage() + " to " + message.getRoom());
        System.out.println( manager.getPeer().sendMessage(message.getRoom(), message));
    }

    public void broadcastMessage(ActionEvent actionEvent) {

        MessageP2P message = new MessageP2P();
        message.setMessage(messageField.getText());

        System.out.println("Broadcasting: " + message.getMessage());


        for (Tab t: chatTabs.getTabs()) {
            message.setRoom(t.getText());
            manager.getPeer().sendMessage(t.getText(), message);
        }


    }
}
