/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends Application {

    public static final boolean debugging = false;
    private static final boolean SAVE_LOG_TO_FILE = true;


    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    private static final String LOG_FILE_NAME = "FantasyManager_" + now() + ".log";
    private static final String SUB_DICTIONARY_FOR_LOGS = "logs";

    private static final String PATH_TO_LOG_FILE = System.getProperty("user.dir") +
            File.separator + SUB_DICTIONARY_FOR_LOGS + File.separator + LOG_FILE_NAME;


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fantasyManager/ui/menuBar.fxml"));

        stage.setTitle("Tvorba nového území");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 1300, 700));
        // save and then close
        stage.setOnCloseRequest(event -> {
            System.out.println("Closing application");
            if (!FileManager.save()) {
                Global.yesNoDialogForNotSaved("Cant save!",
                        "Saving error, do you want to close without saving?");
                if (!FileManager.saved) {
                    event.consume();
                }
            }
        });

        stage.show();
    }


    public static void main(String[] args) {
        System.out.println("Starting fantasy manager...");
        System.out.println("Java version: " + System.getProperty("java.version"));

        if (SAVE_LOG_TO_FILE)
            try {
                File logFile = new File(PATH_TO_LOG_FILE);
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
                PrintStream out = new PrintStream(new FileOutputStream(PATH_TO_LOG_FILE));
                System.setOut(out);
            } catch (Exception e) {
                System.out.println("Saving to log file can't be initialized, error: " + e.toString());
            }

        launch(args);
        System.out.println("Fantasy manager closed");
    }

    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(cal.getTime());
    }

}
