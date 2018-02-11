package fantasyManager.ui;

import fantasyManager.BasicSlideInfo;
import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.SlideHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Add {

    private String upSlidePath = "";

    @FXML private Label upSlideName;

    @FXML private ChoiceBox typeOfSlide;
    @FXML private TextField slideName;
    @FXML private Pane selectSlidePathRoot;

    @FXML public void initialize() {
        System.out.println("Initializing addition of new slide");
        typeOfSlide.setValue(Global.whatToAdd);
        selectSlidePathRoot.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observableValue, final Boolean aBoolean,
                                final Boolean aBoolean2) {
                if (aBoolean) pathSelected(); // set invisible -> path was selected
            }
        });
    }

    public void add() {
        System.out.println("Adding new slide");
        String name = slideName.getText();
        String type = getTypeFromString(typeOfSlide.getValue().toString());
        System.out.print("name: " + name);
        System.out.print(" , type: " + type);
        System.out.println(" , upSlide: " + upSlidePath);
        // Create new slide
        String slidePath = FileManager.addSlide(type, name, upSlidePath);
        // Open new slide
        Global.openNewSlide(slidePath);
        MenuBar.popOutMenuRoot.setVisible(false);
    }
    public void cancel() {
        System.out.println("Adding canceled");
        MenuBar.popOutMenuRoot.setVisible(false);
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
        String scenePath = "selectSlide.fxml";
        System.out.println("Opening up slide ask window: " + scenePath);
        try {
            Global.selectSlide_Prompt = "Zvol nadřazené";
            Global.selectSlide_Pane = selectSlidePathRoot;
            Global.selectSlide_SearchAlsoForCurrentSlide = true;
            Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
            selectSlidePathRoot.getChildren().removeAll(selectSlidePathRoot.getChildren());
            selectSlidePathRoot.getChildren().addAll(pane);
            selectSlidePathRoot.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }

    private void pathSelected() {
        System.out.println("Path selected: " + Global.selectSlide_Selected);
        if (Global.selectSlide_Selected != null) {
            upSlidePath = Global.selectSlide_Selected;
            if (upSlidePath.isEmpty()) {
                upSlideName.setText("Hlavní rozcestník");
            } else {
                for (BasicSlideInfo slide : Global.slidesList) {
                    if (slide.path.equals(upSlidePath)) {
                        upSlideName.setText(slide.name);
                        break;
                    }
                }
            }
        }
    }

}
