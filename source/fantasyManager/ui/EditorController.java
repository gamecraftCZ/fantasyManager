package fantasyManager.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class EditorController {

    @FXML
    private TextField name;
    @FXML
    private ImageView image;

//    @FXML
//    public void initialize() {
//        System.out.println("Initializing editor");
//    }

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
