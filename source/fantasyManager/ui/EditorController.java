package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.SlideHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.annotation.XmlElementDecl;
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
    private boolean selectFileWindowOpened = false;

    @FXML
    public void initialize() {
        System.out.println("Initializing editor");

        // set name
        name.setText(Global.slide.name);

        // set image buttons and image
        imageLeft.setVisible(false);
        if (!Global.slide.images.isEmpty()) {
            currentImage = 0;
            image.setImage(FileManager.getImage(Global.slide.images.get(0)));
            if (Global.slide.images.size() > 1) {
                imageRight.setVisible(true);
                addImage.setVisible(false);
            } else {
                imageRight.setVisible(false);
                addImage.setVisible(true);
            }
        } else {
            currentImage = -1;
            imageRight.setVisible(false);
            addImage.setVisible(true);
        }

        //

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
        currentImage -= 1;
        System.out.println("Now image position is " +currentImage);
        Image imageVar = FileManager.getImage(Global.slide.images.get(currentImage));
        image.setImage(imageVar);
        imageRight.setVisible(true);
        addImage.setVisible(false);
        if (currentImage == 0) {
            System.out.println("This image is 0");
            imageLeft.setVisible(false);
        }
    }
    public void imageRightButton() {
        System.out.println("Going one image right");
        currentImage += 1;
        System.out.println("Now image position is " +currentImage);
        image.setImage(FileManager.getImage(Global.slide.images.get(currentImage)));
        imageLeft.setVisible(true);
        if ((currentImage + 1) == Global.slide.images.size()) {
            System.out.println("Image " +currentImage+ " is last image, image on position " +Global.slide.images.size());
            imageRight.setVisible(false);
            addImage.setVisible(true);
        }
    }
    public void addImageButton() {
        System.out.println("Adding image...");

        if (selectFileWindowOpened) {
            // System dialog for file open already opened
            System.out.println("System dialog for file open already opened");
            return;
        }
        selectFileWindowOpened =  true;
        Stage stage = new Stage();
        System.out.println("Select file in file choose dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Přidání obrázku");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Soubor obrázku", "*.png", "*.jpg", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Všechny soubory", "*"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("Opening file: " + file);
            int imageId = FileManager.addImage(file);
            if (imageId != -1) {
                // load image
                System.out.println("Image added to zip");

                Global.slide.images.add(imageId);
                imageRightButton();

                System.out.println("Image loaded, new image id: " +imageId);
                // image loaded
            } else {
                System.out.println("Cant add image: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        FileManager.saved = false;
        selectFileWindowOpened = false;

        System.out.println("Image added");
    }

}
