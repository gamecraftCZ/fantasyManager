package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.SlideHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.File;

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

    private int currentImage;

    @FXML
    public void initialize() {
        System.out.println("Initializing editor");
        // set name
        name.setText(Global.slide.name);
        // set image buttons
        imageLeft.setVisible(false);
        if (!Global.slide.images.isEmpty()) {
            image.setImage(FileManager.getImage(Global.slide.images.get(0)));
            if (Global.slide.images.size() > 1) {
                imageRight.setVisible(true);
                addImage.setVisible(false);
            } else {
                imageRight.setVisible(false);
                addImage.setVisible(true);
            }
        } else {
            imageRight.setVisible(false);
            addImage.setVisible(true);
        }

        System.out.println("Editor initialized");
    }

    public void nameChanged() {
        String nameString = name.getText();
        Global.slide.name = nameString;
        FileManager.saved = false;
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
