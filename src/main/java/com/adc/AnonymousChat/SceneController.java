package com.adc.AnonymousChat;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneController {

    private HashMap<String, Pane> screenMap;

    private static SceneController instance = null;

    public static SceneController getInstance()
    {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    private SceneController() {
        screenMap = new HashMap<>();
    }


    protected void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    protected void activate(String name, Stage stage){
        Pane p = screenMap.get(name);
        Parent root = p;
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }
}