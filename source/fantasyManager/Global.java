package fantasyManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Global {

    public static SlideHandler slide;
    public static UserButton getButtonById(ArrayList<UserButton> buttons, int buttonId) {
        for (UserButton button : buttons) {
            if (button.buttonId == buttonId) {
                return button;
            }
        }
        return new UserButton(buttons.get(buttons.size()).buttonId + 1);
    }
    public static ArrayList<String> lastVisitedSlides = new ArrayList<>();
    public static ArrayList<BasicSlideInfo> slidesList = new ArrayList<>();
    public static int getSlidePositionInSlidesListByPath(String path) {
        int pos = -1;
        for (int i = 0; i < slidesList.size(); i++) {
            if (slidesList.get(i).path.equals(path)) {
                pos = i;
            }
        }
        return pos;
    }

    public static boolean isOpeningButtonRight;
    public static int openingButtonId;

    public static String whatToAdd;
    public static String addTempString;

    public static String selectSlidePrompt;
    public static Pane selectSlidePane;
    public static String selectSlideSelected;

    public static void showError(String title, String text) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());
        closeButton.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if(keyEvent.getCode() == KeyCode.ENTER)
                {
                    window.close();
                }
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    public static void yesNoDialogFOrNotSaved(String title, String text) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                window.close();
                FileManager.saved = false;
            }
        });
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                window.close();
                FileManager.saved = true;
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, noButton, yesButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
