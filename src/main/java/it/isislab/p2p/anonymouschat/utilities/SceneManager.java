package it.isislab.p2p.anonymouschat.utilities;

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
    private  Stage openedStage;

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
        Scene scene = new Scene(p);
        stage.setMaxHeight(768.0);
        stage.setMaxWidth(1024.0);
        stage.setMinHeight(768.0);
        stage.setMinWidth(1024.0);
        stage.setScene(scene);
        stage.show();
        openedStage  = stage;
    }

    public Stage getOpenedStage() {
        return openedStage;
    }
}