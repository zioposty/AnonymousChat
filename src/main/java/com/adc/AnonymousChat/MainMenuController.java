package com.adc.AnonymousChat;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private static final String VOICENAME_kevin = "kevin16";

    private void speak(String text) {
        Voice voice;
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.allocate();

        voice.speak(text);
    }


    //iniettato tramite id
    @FXML
    public Button beginButton;

    public void buttonClicked(ActionEvent actionEvent) throws IOException {
        speak("Welcome");

        SceneController screenController = SceneController.getInstance();
        screenController.activate("choicemenu", (Stage) beginButton.getScene().getWindow());
    }

}
