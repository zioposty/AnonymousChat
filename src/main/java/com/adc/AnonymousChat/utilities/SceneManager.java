package com.adc.AnonymousChat.utilities;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

/*
    Componente per gestire tutto ciò che serve per il 'cambio di scena'
 */
public class SceneManager {

    private HashMap<String, Pane> screenMap;

    private static SceneManager instance = null;

    public static SceneManager getInstance()
    {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    private SceneManager() {
        screenMap = new HashMap<>();
    }


    public void addScene(String name, Pane pane){
        screenMap.put(name, pane);
    }

    public void removeScene(String name){
        screenMap.remove(name);
    }

    public void activate(String name, Stage stage){
        Pane p = screenMap.get(name);
        Parent root = p;
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }
}