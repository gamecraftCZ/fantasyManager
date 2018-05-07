/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.Global;
import fantasyManager.UserButton;
import fantasyManager.fileManager.Saves;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static fantasyManager.ui.ViewManager.centerImage;

public class View {

    private static final double buttonWidth = 370;
    private static final double leftButtonsOffsetFromLeft = 16;
    private static final double rightButtonsOffsetFromLeft = 400;

    @FXML private Label name;
    @FXML private ImageView image;
    @FXML private Group imageLeft;
    @FXML private Group imageRight;
    @FXML private Label pictureNotAvailableText;
    @FXML private Label upButtonTarget;
    @FXML private Button slideUp;
    @FXML private Pane buttonViewPane;
    @FXML private AnchorPane buttonsScrollAnchor;

    private int currentImage;

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
            Image imageVar = Saves.getImage(Global.slide.images.get(0));
            image.setImage(imageVar);
            centerImage(image);
            if (imageVar == null) {
                pictureNotAvailableText.setVisible(true);
            } else {
                pictureNotAvailableText.setVisible(false);
            }
            if (Global.slide.images.size() > 1) {
                imageRight.setVisible(true);
            } else {
                imageRight.setVisible(false);
            }
        } else {
            currentImage = -1;
            imageRight.setVisible(false);
        }
    }
    private void loadButtons() {
        // left buttons
        for (UserButton button : Global.slide.leftButtons) {
            ViewManager.addButtonToScene(button, buttonWidth, buttonsScrollAnchor,
                    leftButtonsOffsetFromLeft, buttonViewPane);
        }
        // right buttons
        for (UserButton button : Global.slide.rightButtons) {
            ViewManager.addButtonToScene(button, buttonWidth, buttonsScrollAnchor,
                    rightButtonsOffsetFromLeft, buttonViewPane);
        }
        setScrollAnchorSize();
    }
    private void setUpNavigationButtons() {
        // is root -> no up slide
        if (Global.slide.path.length() < 10) {
            slideUp.setVisible(false);
            upButtonTarget.setVisible(false);
        } else {
            slideUp.setVisible(true);
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
        }
    }

    public void infoButton() {
        System.out.println("Showing info");
        openButtonScene("viewSlideInfo.fxml");
    }

    public void imageLeftButton() {
        System.out.println("Going one image left");
        currentImage -= 1;
        System.out.println("Now image position is " +currentImage);
        Image imageVar = Saves.getImage(Global.slide.images.get(currentImage));
        image.setImage(imageVar);
        centerImage(image);
        if (imageVar == null) {
            pictureNotAvailableText.setVisible(true);
        } else {
            pictureNotAvailableText.setVisible(false);
        }
        imageRight.setVisible(true);
        if (currentImage == 0) {
            System.out.println("This image is 0");
            imageLeft.setVisible(false);
        }
    }
    public void imageRightButton() {
        System.out.println("Going one image right");
        currentImage += 1;
        System.out.println("Now image position is " +currentImage);
        Image imageVar = Saves.getImage(Global.slide.images.get(currentImage));
        image.setImage(imageVar);
        centerImage(image);
        if (imageVar == null) {
            pictureNotAvailableText.setVisible(true);
        } else {
            pictureNotAvailableText.setVisible(false);
        }
        imageLeft.setVisible(true);
        if ((currentImage + 1) == Global.slide.images.size()) {
            System.out.println("Image " +currentImage+ " is last image, image on position " +Global.slide.images.size());
            imageRight.setVisible(false);
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

    private void openButtonScene(String path) {
        System.out.println("Opening scene in button pane: " + path);
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(path));
            buttonViewPane.getChildren().removeAll(buttonViewPane.getChildren());
            buttonViewPane.getChildren().addAll(pane);
            buttonViewPane.getParent().setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are in available
        }
    }

    private void setScrollAnchorSize() {
        double size = EditorManager.getNewAnchorSize(Global.slide.leftButtons, Global.slide.rightButtons);
        buttonsScrollAnchor.setPrefHeight(size);
    }

}
