package it.isislab.p2p.anonymouschat;

import it.isislab.p2p.anonymouschat.utilities.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {



    //iniettato tramite id
    @FXML
    public Button beginButton;

    public void buttonClicked(ActionEvent actionEvent) throws IOException {

      /*  VoiceFreeTTS voice = VoiceFreeTTS.getInstance();

        voice.speak("Welcome");
        */
        SceneManager screenController = SceneManager.getInstance();
//        screenController.activate("choicemenu", (Stage) beginButton.getScene().getWindow());
        screenController.activate("chatview", (Stage) beginButton.getScene().getWindow());

    }

}
