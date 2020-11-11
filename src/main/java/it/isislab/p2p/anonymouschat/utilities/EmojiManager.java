package it.isislab.p2p.anonymouschat.utilities;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class EmojiManager {

    private static HashMap<String, byte[]> emojiList;

    static {
        try {
            emojiList = new HashMap<>();

            emojiList.put("smile", new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81});  // grinning face with smiling eyes
            emojiList.put("laugh", new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x82});  // laughing
            emojiList.put("heart", new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x8D});  // heart eyes
            emojiList.put("angry", new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0xA0});  // angry
            emojiList.put("cry", new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0xAD});  // crying
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static String getEmoji(String nameEmoji)
    {
        if(!emojiList.containsKey(nameEmoji)) return null;
        return new String(emojiList.get(nameEmoji), StandardCharsets.UTF_8);
    }

    public static ArrayList<String> getAll(){
        ArrayList<String> list = new ArrayList<>();
        emojiList.forEach((s, bytes) -> {
            list.add(new String(bytes, StandardCharsets.UTF_8));
        });

        return list;
    }
}
