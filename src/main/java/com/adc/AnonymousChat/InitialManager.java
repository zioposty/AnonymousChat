package com.adc.AnonymousChat;

import com.adc.AnonymousChat.utilities.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class InitialManager extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/MainMenu.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        primaryStage.setTitle("Anonymous Chat - Luca Postiglione");
        primaryStage.setMaxHeight(600.0);
        primaryStage.setMaxWidth(800.0);
        primaryStage.setMinHeight(600.0);
        primaryStage.setMinWidth(800.0);
        Scene scene = new Scene(root);

        SceneController screenController = SceneController.getInstance();
        screenController.addScene("mainmenu", FXMLLoader.load(getClass().getResource( "/MainMenu.fxml" )));
        screenController.addScene("choicemenu", FXMLLoader.load(getClass().getResource( "/ChoiceMenu.fxml" )));
        screenController.addScene("chatview", FXMLLoader.load(getClass().getResource("/ChatView.fxml")));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
