package com.adc.AnonymousChat;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

    public void buttonClicked(ActionEvent actionEvent) {
        speak("Welcome");
    }

}
