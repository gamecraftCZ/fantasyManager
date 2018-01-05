package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.SlideHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static fantasyManager.FileManager.addSlide;

public class addController {

    public static String upPath = "";

    @FXML public static TextField upSlideName;

    @FXML private ChoiceBox typeOfSlide;
    @FXML private TextField slideName;
    @FXML private Pane selectSlidePathRoot;

    private String upSlide;

    @FXML public void initialize() {
        System.out.println("Initializing addition of new slide");
        typeOfSlide.setValue(Global.whatToAdd);
    }

    public void cancel() {
        System.out.println("Adding canceled");
        MenuBar.popOutMenuRoot.setVisible(false);
    }

    public void add() {
        System.out.println("Adding new slide");
        String name = slideName.getText();
        String type = getTypeFromString(typeOfSlide.getValue().toString());
        // Create new slide
        String slidePath = FileManager.addSlide(type, name, upSlide);
        // Open new slide
        Global.slide = new SlideHandler(slidePath);
        System.out.println("Opening scene: " + "editing.xml");
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("editing.xml"));
            MenuBar.windowRoot.getChildren().removeAll(MenuBar.windowRoot.getChildren());
            MenuBar.windowRoot.getChildren().addAll(pane);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }

    public void getUpSlide() {
        System.out.println("Getting up slide for new slide");
        openUpSlideAskWindow();
    }



    private String getTypeFromString(String typeString) {
        switch (typeString) {
            case "Postava":
                return "characters";
            case "Místo":
                return "places";
            case "Organizace":
                return "organisations";
            default:
                return "other";
        }
    }
    private void openUpSlideAskWindow() {
        String scenePath = "getSlidePath.fxml";
        System.out.println("Opening up slide ask window: " + scenePath);
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
            selectSlidePathRoot.getChildren().removeAll(selectSlidePathRoot.getChildren());
            selectSlidePathRoot.getChildren().addAll(pane);
            selectSlidePathRoot.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }

}
