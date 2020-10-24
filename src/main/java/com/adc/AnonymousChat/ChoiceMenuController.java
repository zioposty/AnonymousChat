package com.adc.AnonymousChat;

import com.adc.AnonymousChat.utilities.SceneController;
import com.adc.AnonymousChat.utilities.VoiceFreeTTS;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChoiceMenuController {

    private static VoiceFreeTTS voice = VoiceFreeTTS.getInstance();
    private static SceneController screenController = SceneController.getInstance();

    public void createRoom(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        screenController.activate("chatview", (Stage) button.getScene().getWindow());
        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/
    }

    public void joinRoom(ActionEvent actionEvent) {

        /*TODO: nel caso di stanza da id già presente, rendere visibile messaggio di errore*/

    }
}
