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
        // do you want to close without saving? dialog
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Closing application");
                if (FileManager.saved) {
                    // saved, can be closed
                    System.out.println("Nothing to save, can be closed");
                } else {
                    // project is not saved, ask for saving
                    System.out.println("Do you want to save this project?");
                }
            }
        });

        stage.show();
    }



    public static void main(String[] args) {
        System.out.println("Starting fantasy manager...");
        launch(args);
        System.out.println("Fantasy manager closed");
    }
}
