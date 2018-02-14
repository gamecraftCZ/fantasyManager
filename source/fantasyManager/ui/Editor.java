/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.BasicSlideInfo;
import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Editor {

    private static final double buttonWidth = 220;
    private static final double leftButtonsOffsetFromLeft = 16;
    private static final double rightButtonsOffsetFromLeft = 250;

    @FXML private TextField name;
    @FXML private ImageView image;
    @FXML private Group imageLeft;
    @FXML private Group imageRight;
    @FXML private Group addImage;
    @FXML private Group deleteImage;
    @FXML private Button leftPlus;
    @FXML private Button rightPlus;
    @FXML private Pane buttonEditorRoot;
    @FXML private Pane buttonEditorPane;
    @FXML private AnchorPane scrollAnchor;

    @FXML private Label upButtonTarget;
    @FXML private Button slideUp;
    @FXML private Button editUpSlideButton;
    @FXML private Pane selectUpSlideRoot;
    @FXML private Pane selectUpSlidePane;

    private int currentImage;
    private boolean selectFileWindowOpened = false;
    private boolean editingButton = false;

    @FXML public void initialize() {
        System.out.println(".Initializing editor");
        try {

            name.setText(Global.slide.name);
            setUpNavigationButtons();
            loadImageControlButtonsAndFirstImage();
            loadButtons();

            System.out.println("Editor initialized.");
        } catch (Exception ex) {
            System.out.println("Cant initialize editor controller, error: " + ex.toString());
        }
    }
    private void loadImageControlButtonsAndFirstImage() {
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
            deleteImage.setVisible(false);
        }
    }
    private void loadButtons() {
        // left buttons
        for (UserButton button : Global.slide.leftButtons) {
            EditorManager.addButtonToScene(button, Global.slide.leftButtons,
                    leftPlus, scrollAnchor, buttonWidth, leftButtonsOffsetFromLeft, buttonEditorPane);
        }
        // right buttons
        for (UserButton button : Global.slide.rightButtons) {
            EditorManager.addButtonToScene(button, Global.slide.rightButtons,
                    rightPlus, scrollAnchor, buttonWidth, rightButtonsOffsetFromLeft, buttonEditorPane);
        }
    }

    private void setUpNavigationButtons() {
        // is root -> no up slide
        if (Global.slide.path.length() < 10) {
            slideUp.setVisible(false);
            editUpSlideButton.setVisible(false);
            upButtonTarget.setVisible(false);
        } else {
            slideUp.setVisible(true);
            editUpSlideButton.setVisible(true);
            upButtonTarget.setVisible(true);

            // set upButtonTarget
            int pos = Global.getSlidePositionInSlidesListByPath(Global.slide.upSlide);
            if (pos == -1) {
                System.out.println("Up slide name: Hlavní rozcestník");
                upButtonTarget.setText("Hlavní rozcestník");
            } else {
                System.out.println("Up slide name: " + Global.slidesList.get(pos).name);
                upButtonTarget.setText(Global.slidesList.get(pos).name);
            }
            addSelectUpSlideButtonClickListener();
        }
    }
    private void addSelectUpSlideButtonClickListener() {
        selectUpSlideRoot.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observableValue, final Boolean aBoolean,
                                final Boolean aBoolean2) {
                if (aBoolean) newUpSlideSelected(); // set invisible -> path was selected
            }
        });
    }

    public void nameChanged() {
        String nameString = name.getText();
        if (!Global.slide.name.equals(nameString)) {
            Global.slide.name = nameString;
            FileManager.saved = false;
            System.out.println("Name changed to: " + nameString);
        }
    }
    public void infoButton() {
        System.out.println("Showing info");
        openButtonScene("editorSlideInfo.fxml");

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
                if (Global.slide.images.size() == 1) {
                    currentImage = 0;
                    Image imageVar = FileManager.getImage(Global.slide.images.get(currentImage));
                    image.setImage(imageVar);
                } else {
                    imageRightButton();
                }
                deleteImage.setVisible(true);
                FileManager.saved = false;

                System.out.println("Image loaded, new image id: " +imageId);
                // image loaded
            } else {
                System.out.println("Cant add image: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        selectFileWindowOpened = false;

        System.out.println("Image added");
    }
    public void deleteImage() {
        System.out.println("Deleting image");
        int imageToDeleteId = Global.slide.images.get(currentImage);
        Global.slide.images.remove(currentImage);
        System.out.println("Opening new image");
        if (currentImage > 0) {
            currentImage -= 1;
        }

        if (Global.slide.images.size() == 0) {
            System.out.println("No images left");
            image.setImage(null);
            imageLeft.setVisible(false);
            imageRight.setVisible(false);
            addImage.setVisible(true);
            deleteImage.setVisible(false);
        } else {
            System.out.println("Now image position is " + currentImage);
            Image imageVar = FileManager.getImage(Global.slide.images.get(currentImage));
            image.setImage(imageVar);

            if (currentImage == 0) {
                imageLeft.setVisible(false);
            } else {
                imageLeft.setVisible(true);
            }

            if (currentImage == Global.slide.images.size() - 1) {
                imageRight.setVisible(false);
                addImage.setVisible(true);
            } else {
                imageRight.setVisible(true);
            }
        }

        FileManager.deleteImage(imageToDeleteId);
        System.out.println("Image deleted");
    }

    public void addLeftButton() {
        System.out.println(".Adding button to left");
        FileManager.saved = false;

        UserButton button = EditorManager.addNewButton(Global.slide.leftButtons,
                leftPlus, scrollAnchor, buttonWidth, leftButtonsOffsetFromLeft, buttonEditorPane);
        EditorManager.openButtonEditor(button, buttonEditorPane);

        System.out.println("Button added to left.");
    }
    public void addRightButton() {
        System.out.println(".Adding button to right");
        FileManager.saved = false;

        UserButton button = EditorManager.addNewButton(Global.slide.rightButtons,
                rightPlus, scrollAnchor, buttonWidth, rightButtonsOffsetFromLeft, buttonEditorPane);
        EditorManager.openButtonEditor(button, buttonEditorPane);

        System.out.println("Button added to right.");
    }

    public void closeButtonsPane() {
        buttonEditorRoot.setVisible(false);
        editingButton = false;
    }
    private void openButtonScene(String path) {
        System.out.println("Opening scene in button pane: " + path);
        editingButton = true;
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(path));
            buttonEditorPane.getChildren().removeAll(buttonEditorPane.getChildren());
            buttonEditorPane.getChildren().addAll(pane);
            buttonEditorRoot.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are in available
        }
    }

    public void slideBack() {
        if (Global.lastVisitedSlides.size() > 0) {
            System.out.println("Going one slide back");
            int lastItemIndex = Global.lastVisitedSlides.size() - 1;
            Global.openNewSlide(Global.lastVisitedSlides.get(lastItemIndex));
            Global.lastVisitedSlides.remove(lastItemIndex); // remove current slide added by openNewSlide function
            Global.lastVisitedSlides.remove(lastItemIndex); // remove slide currently opened
            System.out.println("Opened recent slide");
        } else {
            System.out.println("No slides to go back");
        }
    }
    public void slideUp() {
        System.out.println("Going to up slide: " + Global.slide.upSlide);
        Global.openNewSlide(Global.slide.upSlide);
    }
    public void editUpSlide() {
        System.out.println("Editing up slide");
        selectUpSlideRoot.setVisible(true);

        Global.selectSlide_Prompt = "Nadřazený slide";
        Global.selectSlide_Pane = selectUpSlideRoot;
        Global.selectSlide_SearchAlsoForCurrentSlide = false;
        openEditUpSlideMenu();

        System.out.println("Edit up slide menu opened");
    }
    private void openEditUpSlideMenu() {
        String menuPath = "selectSlide.fxml";
        System.out.println("Opening pop out menu: " + menuPath);
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(menuPath));
            selectUpSlidePane.getChildren().removeAll(selectUpSlidePane.getChildren());
            selectUpSlidePane.getChildren().addAll(pane);
            selectUpSlidePane.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }
    private void newUpSlideSelected() {
        System.out.println("Path selected: " + Global.selectSlide_Selected);
        if (Global.selectSlide_Selected != null) {
            Global.slide.upSlide = Global.selectSlide_Selected;
            if (Global.slide.upSlide.isEmpty()) {
                upButtonTarget.setText("Hlavní rozcestník");
            } else {
                for (BasicSlideInfo slide : Global.slidesList) {
                    if (slide.path.equals(Global.slide.upSlide)) {
                        upButtonTarget.setText(slide.name);
                        break;
                    }
                }
            }
        }
    }

}
