package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.EmojiManager;
import it.isislab.p2p.anonymouschat.utilities.MessageP2P;
import it.isislab.p2p.anonymouschat.utilities.PeerManager;

import it.isislab.p2p.anonymouschat.utilities.SceneManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ChatViewController {

    public MenuItem menuQuit;
    public TabPane chatTabs;
    public TextArea messageField;
    public Label titleChat;
    public ListView<String> notificationList;

    private final PeerManager peerManager = PeerManager.getInstance();
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final double[] CHAT_SIZE = { 620.0, 430.0 };  //height, width
    private final String NOTIF_MSS = "New message in ";
    private final int NOTIF_MAX_NUMBER = 10;

    private final ReentrantLock notificationLock = new ReentrantLock();
    public ComboBox<String> emojiSelector;
    public ImageView previewImage;
    public Button cancelImgButton;
    public TextArea roomField;
    public Label opInfo;

    private String imagePath = "";

    //----------


    @FXML
    public void initialize() {
        chatTabs.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab oldT, Tab newT) {
                    if (newT == null) return;

                    newT.setStyle(null);
                    titleChat.setText(newT.getText());
                }
            }
        );

        emojiSelector.getItems().addAll(EmojiManager.getAll());
        emojiSelector.getSelectionModel().selectFirst();
        messageField.setWrapText(true);

    }

    public void closeApplication(ActionEvent actionEvent) {

        peerManager.getPeer().leaveNetwork();
        System.exit(0);
    }

    private void removeChat(Event e){
        Tab tab1 = (Tab) e.getSource();
        TextFlow t = (TextFlow) tab1.getContent().lookup("TextFlow");
        peerManager.removeChat(t.getId());
        peerManager.getPeer().leaveRoom(t.getId());
        //System.out.println("Leaving " + t.getId());
        System.out.println(peerManager.getChat().toString());
    }

    public void joinRoom(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("roomName");
        dialog.setTitle("Join room");
        dialog.setHeaderText("Insert the name of an existing room that you wonna join");
        dialog.setContentText("Write here the room name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String roomName = result.get();
            if(peerManager.isChatJoined(roomName)){

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

            if(peerManager.getPeer().joinRoom(roomName))
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
                if(peerManager.getPeer().createRoom(roomName))
                //if(true)
                    if(confirmJoinRoom(roomName)){
                        peerManager.getPeer().joinRoom(roomName);
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
        /*
        TextArea chat = new TextArea();
        chat.setId(roomName);
        chat.setEditable(false);
        peerManager.addChat(roomName, chat);
        chat.setPrefHeight(CHAT_SIZE[0]);
        chat.setPrefWidth(CHAT_SIZE[1]);
        chat.setWrapText(true);
        Tab tab = new Tab(roomName, chat);
        */

        TextFlow chat = new TextFlow();
        chat.setId(roomName);
        chat.setPrefHeight(CHAT_SIZE[0]);
        chat.setPrefWidth(CHAT_SIZE[1]);

        chat.setPadding(new Insets(0,20 , 0, 5));
        peerManager.addChat(roomName, chat);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(chat);
        Tab tab = new Tab(roomName, scrollPane);

        tab.setOnCloseRequest(e -> removeChat(e));

        chat.getChildren().addListener((ListChangeListener<? super Node>) (list) -> {
            if (!tab.isSelected()) {
                tab.setStyle("-fx-border-color: #CF0000; -fx-font-weight: bold;");


                notificationLock.lock();
                try {
                    notificationList.getItems().add(NOTIF_MSS + roomName);                    if (notificationList.getItems().size() > NOTIF_MAX_NUMBER)
                        notificationList.getItems().remove(0);
                }
                finally {
                    notificationLock.unlock();
                }
            }

        });

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

    public void sendMessage(ActionEvent actionEvent) throws FileNotFoundException {
        String mss = messageField.getText().trim();
        if((mss.isBlank() && imagePath.isBlank()) || peerManager.getChat().size() == 0) return;
        messageField.setText("");
        MessageP2P message;
        if (imagePath.isBlank()) message = new MessageP2P(chatTabs.getSelectionModel().getSelectedItem().getText(), mss);
        else
            message = new MessageP2P(chatTabs.getSelectionModel().getSelectedItem().getText(), mss, imagePath);
        cancelImgButton.fire();
        System.out.println("Sending: " + message.getMessage() + " to " + message.getRoom());
        System.out.println( peerManager.getPeer().sendMessage(message.getRoom(), message));
    }

    public void broadcastMessage(ActionEvent actionEvent) {

        MessageP2P message = new MessageP2P();
        String mss = messageField.getText().trim();
        if((mss.isBlank() && imagePath.isBlank()) || peerManager.getChat().size() == 0) return;

        messageField.setText("");

        message.setMessage(mss);
        if(!imagePath.isBlank()) {
            try {
                message.setImage(imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            cancelImgButton.fire();
        }
            System.out.println("Broadcasting: " + message.getMessage());


        for (Tab t: chatTabs.getTabs()) {
            message.setRoom(t.getText());
            peerManager.getPeer().sendMessage(t.getText(), message);
        }


    }

    public void selectNotification(MouseEvent click) {
        if (click.getClickCount() == 2) {
            String notification = notificationList.getSelectionModel().getSelectedItem();

            String room = notification.replace(NOTIF_MSS, "");

            while(notificationList.getItems().remove(notification));

            for (Tab t: chatTabs.getTabs()) {
                if (t.getText().equals(room))  {
                    chatTabs.getSelectionModel().select(t);
                    return;
                }
            }
        }
    }

    public void addEmoji(ActionEvent actionEvent) {

        messageField.appendText(emojiSelector.getSelectionModel().getSelectedItem());
    }

    public void selectImage(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Add image to a message");


        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = chooser.showOpenDialog(sceneManager.getOpenedStage());
        if (file != null) {
            imagePath = file.getAbsolutePath();
            previewImage.setImage(new Image(new FileInputStream(imagePath)));
            cancelImgButton.setVisible(true);
        }

    }

    public void removeImage(ActionEvent actionEvent) {
        imagePath = "";
        previewImage.setImage(null);
        cancelImgButton.setVisible(false);
    }

    public void createRoomButton(ActionEvent actionEvent) {
        String roomName = roomField.getText();
        if(peerManager.getPeer().createRoom(roomName))
            opInfo.setText(roomName +  " created");
        else
            opInfo.setText(roomName + " already exists");
    }

    public void joinRoomButton(ActionEvent actionEvent) {
        String roomName = roomField.getText();

        if(peerManager.isChatJoined(roomName)){
            for(Tab t: chatTabs.getTabs())
            {
                if(t.getText().equals(roomName)) {
                    chatTabs.getSelectionModel().select(t);
                    break;
                }

            }

            opInfo.setText("You have already joined " + roomName);
        }
        else
            if(peerManager.getPeer().joinRoom(roomName))    addTabChat(roomName);
            else opInfo.setText(roomName + "doesn't exist");


    }
}

