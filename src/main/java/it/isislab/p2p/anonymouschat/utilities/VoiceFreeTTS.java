package it.isislab.p2p.anonymouschat.utilities;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceFreeTTS {
    private static VoiceFreeTTS ourInstance = new VoiceFreeTTS();
    private static final String VOICENAME_kevin = "kevin16";

    public static VoiceFreeTTS getInstance() { return ourInstance; }


    public  void speak(String text) {
        Voice voice;
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.allocate();

        voice.speak(text);
    }


    private VoiceFreeTTS() {
    }
}
