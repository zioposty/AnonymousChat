package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.SceneManager;
import it.isislab.p2p.anonymouschat.utilities.VoiceFreeTTS;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChoiceMenuController {

    private static VoiceFreeTTS voice = VoiceFreeTTS.getInstance();
    private static SceneManager screenController = SceneManager.getInstance();

    public void createRoom(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        screenController.activate("chatview", (Stage) button.getScene().getWindow());
        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/
    }

    public void joinRoom(ActionEvent actionEvent) {

        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/

    }
}
