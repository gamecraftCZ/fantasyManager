package fantasyManager.ui;

import fantasyManager.Global;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class EditorController {

    @FXML
    private TextField name;
    @FXML
    private ImageView image;
    @FXML
    private Group imageLeft;
    @FXML
    private Group imageRight;
    @FXML
    private Group addImage;

//    @FXML
//    public void initialize() {
//        System.out.println("Initializing editor");
//    }

    public void nameChanged() {
        String nameString = name.getText();
        Global.slide.name = nameString;
        System.out.println("Name changed to: " + nameString);
    }

    public void imageLeftButton() {
        System.out.println("Going one image left");
    }
    public void imageRightButton() {
        System.out.println("Going one image right");
    }
    public void addImage() {
        System.out.println("adding image...");
    }

}
