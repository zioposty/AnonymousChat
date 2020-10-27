package com.adc.AnonymousChat;

import com.adc.AnonymousChat.utilities.SceneManager;
import com.adc.AnonymousChat.utilities.VoiceFreeTTS;
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

        VoiceFreeTTS voice = VoiceFreeTTS.getInstance();

        voice.speak("Welcome");

        SceneManager screenController = SceneManager.getInstance();
        screenController.activate("choicemenu", (Stage) beginButton.getScene().getWindow());
    }

}
