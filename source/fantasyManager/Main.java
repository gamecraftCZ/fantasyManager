/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fantasyManager/ui/menuBar.fxml"));

        stage.setTitle("Tvorba nového území");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 1000, 700));
        // save and then close
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Closing application");
                if (!FileManager.save()) {
                    Global.yesNoDialogForNotSaved("Cant save!", "Saving error, do you want to close without saving?");
                    if (!FileManager.saved) {
                        event.consume();
                    }
                }
            }
        });

        stage.show();
    }


    public static void main(String[] args) {
        System.out.println("Starting fantasy manager...");
        System.out.println("Java version: " + System.getProperty("java.version"));
        launch(args);
        System.out.println("Fantasy manager closed");
    }
}
