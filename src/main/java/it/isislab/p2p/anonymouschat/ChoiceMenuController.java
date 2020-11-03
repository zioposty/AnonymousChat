package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.PeerManager;
import it.isislab.p2p.anonymouschat.utilities.SceneManager;
import it.isislab.p2p.anonymouschat.utilities.VoiceFreeTTS;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class ChoiceMenuController {

    //private static VoiceFreeTTS voice = VoiceFreeTTS.getInstance();
    private SceneManager screenController = SceneManager.getInstance();
    private PeerManager peer =  PeerManager.getInstance();
    public TextField roomName_txt;

    @SuppressWarnings("Duplicates")
    public void createRoom(ActionEvent actionEvent) {
        String roomName = roomName_txt.getText();
        if (isEmptyField(roomName)) return;

        if ( peer.getPeer().createRoom(roomName))
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Join room");
            alert.setHeaderText("Do you wonna join the created room: " + roomName);
            alert.setContentText("Choose your option");

            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");

            alert.getButtonTypes().setAll(yes, no);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == yes){

            }
        }
        else{

        }


        Button button = (Button) actionEvent.getSource();
        screenController.activate("chatview", (Stage) button.getScene().getWindow());
        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/
    }

    public void joinRoom(ActionEvent actionEvent) {
        String roomName = roomName_txt.getText();
        if ( isEmptyField(roomName)) return;
        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/

    }

    private boolean  isEmptyField(String roomName) {

        if( roomName.trim().equals("") ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Operation failed");
            alert.setContentText("Room name cant't be empty");
            alert.showAndWait();
            return true;
        }

        return false;
    }
}
