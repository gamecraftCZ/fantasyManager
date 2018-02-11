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
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Editing {

    @FXML private TextField name;
    @FXML private ImageView image;
    @FXML private Group imageLeft;
    @FXML private Group imageRight;
    @FXML private Group addImage;
    @FXML private Group deleteImage;
    @FXML private Button leftPlus;
    @FXML private Button rightPlus;
    @FXML private Pane buttonRoot;
    @FXML private Pane buttonPane;
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
        System.out.println("Initializing editor");
        try {
            if (Global.slide.path.length() < 10) { // is root -> no up slide
                slideUp.setVisible(false);
                editUpSlideButton.setVisible(false);
                upButtonTarget.setVisible(false);
            } else {
                slideUp.setVisible(true);
                editUpSlideButton.setVisible(true);
                upButtonTarget.setVisible(true);
            }

            // set name
            name.setText(Global.slide.name);
            // set upButtonTarget
            int pos = Global.getSlidePositionInSlidesListByPath(Global.slide.upSlide);
            if (pos == -1) {
                System.out.println("Up slide name: Hlavní rozcestník");
                upButtonTarget.setText("Hlavní rozcestník");
            } else {
                System.out.println("Up slide name: " + Global.slidesList.get(pos).name);
                upButtonTarget.setText(Global.slidesList.get(pos).name);
            }
            // listener on new up button was selected
            selectUpSlideRoot.visibleProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(final ObservableValue<? extends Boolean> observableValue, final Boolean aBoolean,
                                    final Boolean aBoolean2) {
                    if (aBoolean) newUpSlideSelected(); // set invisible -> path was selected
                }
            });

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
                deleteImage.setVisible(false);
            }

            // load buttons \\
            // left buttons
            for (UserButton button : Global.slide.leftButtons) {
                addLeftButtonToScene(button.buttonId);
            }
            // right buttons
            for (UserButton button : Global.slide.rightButtons) {
                addRightButtonToScene(button.buttonId);
            }

        } catch (Exception ex) {
            System.out.println("Cant initialize editor controller, error: " + ex.toString());
        }
        System.out.println("Editor initialized");
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
        openButtonScene("editingSlideInfo.fxml");

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

    public void addLeftButton() throws IOException {
        System.out.println("Adding button to left");
        double buttonPositionY = leftPlus.getLayoutY();
        leftPlus.setLayoutY(buttonPositionY + 64);
        if (Global.slide.leftButtons.size() >= Global.slide.rightButtons.size()) {
            // resize scroll anchor
            double newAnchorSize = 64d + 74d + Global.slide.leftButtons.size() * 64d;
            System.out.println("New anchor size = " +newAnchorSize);
            scrollAnchor.setPrefHeight(newAnchorSize);
        }
        System.out.println("Add left button moved down");

        // get new button id
        int newButtonId;
        if (Global.slide.leftButtons.isEmpty()) {
            newButtonId = 0;
        } else {
            newButtonId = (Global.slide.leftButtons.get(Global.slide.leftButtons.size() - 1).buttonId + 1);
        }
        // add button to scene
        Pane newButton = FXMLLoader.load(getClass().getResource("editingButtonPrefab.fxml"));
        // set new Button Pane
        newButton.setLayoutX(16);
        newButton.setLayoutY(buttonPositionY);
        newButton.setId("leftButton" + (newButtonId));
        System.out.println("new left button id is: " + (newButtonId));
        // set new Button button
        Button newButLeftPart = new Button("left" + (newButtonId));
        newButLeftPart.setPrefSize(115, 50);
        newButLeftPart.setFont(new Font(23));
        newButton.getChildren().set(0, newButLeftPart);
        newButton.getChildren().get(0).setId("leftButtonButton" + (newButtonId));
        // set new Button edit
        newButton.getChildren().get(1).setId("leftButtonEdit" + (newButtonId));
        newButton.getChildren().get(1).setOnMouseClicked(event -> editLeftButton(newButton));
        // set new Button close
        newButton.getChildren().get(2).setId("leftButtonClose" + (newButtonId));
        newButton.getChildren().get(2).setOnMouseClicked(event -> deleteLeftButton(newButton));
        // add button to list of left buttons
        UserButton userButtonObj = new UserButton(newButtonId);
        userButtonObj.buttonPane = newButton;
        userButtonObj.buttonClickable = newButLeftPart;
        Global.slide.leftButtons.add(userButtonObj);
        Global.isOpeningButtonRight = false;
        Global.openingButtonId = Global.slide.leftButtons.get(Global.slide.leftButtons.size() - 1).buttonId;
        System.out.println("Button added to scene and slide object");
        scrollAnchor.getChildren().add(newButton);

        System.out.println("Button added to scene");
        FileManager.saved = false;
        openButtonScene("editingButton.fxml");
        System.out.println("Button added");
    }
    public void deleteLeftButton(Pane button) {
        System.out.println("deleting button: " + button.getId());
        // get button id
        String buttonIdString = button.getId();
        int buttonId = Integer.parseInt(buttonIdString.substring(10, buttonIdString.length()));
        // remove button element
        scrollAnchor.getChildren().removeAll(button);
        // move other buttons up
        UserButton curButton;
        int buttonIndex = 0;
        for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
            curButton = Global.slide.leftButtons.get(i);
            if (curButton.buttonId == buttonId) {
                buttonIndex = i;
                continue;
            }
            if (curButton.buttonId > buttonId) {
                curButton.buttonPane.setLayoutY(curButton.buttonPane.getLayoutY() - 64d);
            }
        }
        // remove button from left buttons list
        Global.slide.leftButtons.remove(buttonIndex);
        // set new anchor size
        double newAnchorSize = 64d + 0d + Global.slide.leftButtons.size() * 64d;
        System.out.println("New anchor size = " +newAnchorSize);
        scrollAnchor.setPrefHeight(newAnchorSize);
        // set new add button position
        double buttonPositionY = leftPlus.getLayoutY();
        leftPlus.setLayoutY(buttonPositionY - 64);
        FileManager.saved = false;
        System.out.println("Left button deleted");
    }
    public void editLeftButton(Pane button) {
        System.out.println("Opening edit button");
        String buttonIdString = button.getId();
        int buttonId = Integer.parseInt(buttonIdString.substring(10, buttonIdString.length()));
        Global.isOpeningButtonRight = false;
        Global.openingButtonId = buttonId;
        openButtonScene("editingButton.fxml");
        System.out.println("Editing button");
    }
    public void addRightButton() throws IOException {
        System.out.println("Adding button to right");
        double buttonPositionY = rightPlus.getLayoutY();
        rightPlus.setLayoutY(buttonPositionY + 64);
        if (Global.slide.rightButtons.size() >= Global.slide.leftButtons.size()) {
            // resize scroll anchor
            double newAnchorSize = 64d + 74d + Global.slide.rightButtons.size() * 64d;
            System.out.println("New anchor size = " +newAnchorSize);
            scrollAnchor.setPrefHeight(newAnchorSize);
        }
        System.out.println("Add right button moved down");

        // get new button id
        int newButtonId;
        if (Global.slide.rightButtons.isEmpty()) {
            newButtonId = 0;
        } else {
            newButtonId = (Global.slide.rightButtons.get(Global.slide.rightButtons.size() - 1).buttonId + 1);
        }
        // add button to scene
        Pane newButton = FXMLLoader.load(getClass().getResource("editingButtonPrefab.fxml"));
        // set new Button Pane
        newButton.setLayoutX(244);
        newButton.setLayoutY(buttonPositionY);
        newButton.setId("rightButton" + (newButtonId));
        System.out.println("new right button id is: " + (newButtonId));
        // set new Button button
        Button newButLeftPart = new Button("right" + (newButtonId));
        newButLeftPart.setPrefSize(115, 50);
        newButLeftPart.setFont(new Font(23));
        newButton.getChildren().set(0, newButLeftPart);
        newButton.getChildren().get(0).setId("rightButtonButton" + (newButtonId));
        // set new Button edit
        newButton.getChildren().get(1).setId("rightButtonEdit" + (newButtonId));
        newButton.getChildren().get(1).setOnMouseClicked(event -> editRightButton(newButton));
        // set new Button close
        newButton.getChildren().get(2).setId("rightButtonClose" + (newButtonId));
        newButton.getChildren().get(2).setOnMouseClicked(event -> deleteRightButton(newButton));
        // add button to list of left buttons
        UserButton userButtonObj = new UserButton(newButtonId);
        userButtonObj.buttonPane = newButton;
        userButtonObj.buttonClickable = newButLeftPart;
        Global.slide.rightButtons.add(userButtonObj);
        Global.isOpeningButtonRight = true;
        Global.openingButtonId = Global.slide.rightButtons.get(Global.slide.rightButtons.size() - 1).buttonId;
        System.out.println("Button added to scene and slide object");
        scrollAnchor.getChildren().add(newButton);

        System.out.println("Button added to scene");
        FileManager.saved = false;
        openButtonScene("editingButton.fxml");
        System.out.println("Button added");
    }
    public void deleteRightButton(Pane button) {
        System.out.println("deleting button: " + button.getId());
        // get button id
        String buttonIdString = button.getId();
        int buttonId = Integer.parseInt(buttonIdString.substring(11, buttonIdString.length()));
        // remove button element
        scrollAnchor.getChildren().removeAll(button);
        // move other buttons up
        UserButton curButton;
        int buttonIndex = 0;
        for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
            curButton = Global.slide.rightButtons.get(i);
            if (curButton.buttonId == buttonId) {
                buttonIndex = i;
                continue;
            }
            if (curButton.buttonId > buttonId) {
                curButton.buttonPane.setLayoutY(curButton.buttonPane.getLayoutY() - 64d);
            }
        }
        // remove button from left buttons list
        Global.slide.rightButtons.remove(buttonIndex);
        // set new anchor size
        double newAnchorSize = 64d + 0d + Global.slide.rightButtons.size() * 64d;
        System.out.println("New anchor size = " +newAnchorSize);
        scrollAnchor.setPrefHeight(newAnchorSize);
        // set new add button position
        double buttonPositionY = rightPlus.getLayoutY();
        rightPlus.setLayoutY(buttonPositionY - 64);
        FileManager.saved = false;
        System.out.println("Left button deleted");
    }
    public void editRightButton(Pane button) {
        System.out.println("Opening edit button");
        String buttonIdString = button.getId();
        int buttonId = Integer.parseInt(buttonIdString.substring(11, buttonIdString.length()));
        Global.isOpeningButtonRight = true;
        Global.openingButtonId = buttonId;
        openButtonScene("editingButton.fxml");
        System.out.println("Editing button");
    }

    public void closeButtonsPane() {
        buttonRoot.setVisible(false);
        editingButton = false;
    }
    private void openButtonScene(String path) {
        System.out.println("Opening scene in button pane: " + path);
        editingButton = true;
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(path));
            buttonPane.getChildren().removeAll(buttonPane.getChildren());
            buttonPane.getChildren().addAll(pane);
            buttonRoot.setVisible(true);
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
        openEditUpSlideManu();

        System.out.println("Edit up slide menu opened");
    }
    private void openEditUpSlideManu() {
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


    private void addLeftButtonToScene(int newButtonId) {
        try {
            System.out.println("Adding left button to scene");
            double buttonPositionY = leftPlus.getLayoutY();
            leftPlus.setLayoutY(buttonPositionY + 64);
            if (Global.slide.leftButtons.size() >= Global.slide.rightButtons.size()) {
                // resize scroll anchor
                double newAnchorSize = 64d + 0d + Global.slide.leftButtons.size() * 64d;
                System.out.println("New anchor size = " + newAnchorSize);
                scrollAnchor.setPrefHeight(newAnchorSize);
            }

            // add button to scene
            Pane newButton = FXMLLoader.load(getClass().getResource("editingButtonPrefab.fxml"));
            // set new Button Pane
            newButton.setLayoutX(16);
            newButton.setLayoutY(buttonPositionY);
            newButton.setId("leftButton" + (newButtonId));
            System.out.println("new left button id is: " + (newButtonId));
            // set new Button button
            Button newButLeftPart = new Button(Global.slide.leftButtons.get(newButtonId).title);
            newButLeftPart.setPrefSize(115, 50);
            newButLeftPart.setFont(new Font(23));
            newButton.getChildren().set(0, newButLeftPart);
            newButton.getChildren().get(0).setId("leftButtonButton" + (newButtonId));
            // set new Button edit
            newButton.getChildren().get(1).setId("leftButtonEdit" + (newButtonId));
            newButton.getChildren().get(1).setOnMouseClicked(event -> editLeftButton(newButton));
            // set new Button close
            newButton.getChildren().get(2).setId("leftButtonClose" + (newButtonId));
            newButton.getChildren().get(2).setOnMouseClicked(event -> deleteLeftButton(newButton));
            // add to scene
            scrollAnchor.getChildren().add(newButton);
            // add button objects to variable
            Global.slide.leftButtons.get(newButtonId).buttonPane = newButton;
            Global.slide.leftButtons.get(newButtonId).buttonClickable = newButLeftPart;
        } catch (Exception ex) {
            System.out.println("Can't add left button in initialization, error: " + ex.toString());
        }
    }
    private void addRightButtonToScene(int newButtonId) {
        try {
            System.out.println("Adding right button to scene");
            double buttonPositionY = rightPlus.getLayoutY();
            rightPlus.setLayoutY(buttonPositionY + 64);
            if (Global.slide.rightButtons.size() >= Global.slide.leftButtons.size()) {
                // resize scroll anchor
                double newAnchorSize = 64d + 0d + Global.slide.rightButtons.size() * 64d;
                System.out.println("New anchor size = " + newAnchorSize);
                scrollAnchor.setPrefHeight(newAnchorSize);
            }

            // add button to scene
            Pane newButton = FXMLLoader.load(getClass().getResource("editingButtonPrefab.fxml"));
            // set new Button Pane
            newButton.setLayoutX(250);
            newButton.setLayoutY(buttonPositionY);
            newButton.setId("rightButton" + (newButtonId));
            System.out.println("new right button id is: " + (newButtonId));
            // set new Button button
            Button newButRightPart = new Button(Global.slide.rightButtons.get(newButtonId).title);
            newButRightPart.setPrefSize(115, 50);
            newButRightPart.setFont(new Font(23));
            newButton.getChildren().set(0, newButRightPart);
            newButton.getChildren().get(0).setId("leftButtonButton" + (newButtonId));
            // set new Button edit
            newButton.getChildren().get(1).setId("leftButtonEdit" + (newButtonId));
            newButton.getChildren().get(1).setOnMouseClicked(event -> editRightButton(newButton));
            // set new Button close
            newButton.getChildren().get(2).setId("leftButtonClose" + (newButtonId));
            newButton.getChildren().get(2).setOnMouseClicked(event -> deleteRightButton(newButton));
            // add new button to scene
            scrollAnchor.getChildren().add(newButton);
            // add button objects to variable
            Global.slide.rightButtons.get(newButtonId).buttonPane = newButton;
            Global.slide.rightButtons.get(newButtonId).buttonClickable = newButRightPart;
        } catch (Exception ex) {
            System.out.println("Can't add left button in initialization, error: " + ex.toString());
        }
    }

}
