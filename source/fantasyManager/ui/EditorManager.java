/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class EditorManager {

    private static final double buttonHeight = 50d;
    private static final double buttonOffsets = 14d;
    private static final double buttonOffsetFromTop = 74d;

    /**
     *
     * @param buttons1 List of buttons on first row.
     * @param buttons2 List of buttons on second row.
     * @return height of anchor needed to fit these buttons in.
     */
    private static double getNewAnchorSize(int buttons1, int buttons2) {
        if (buttons1 > buttons2) {
            return  ((buttons1 + 1) * (buttonHeight + buttonOffsets)) + buttonOffsetFromTop;
        } else {
            return  ((buttons2 + 1) * (buttonHeight + buttonOffsets)) + buttonOffsetFromTop;
        }
    }
    public static double getNewAnchorSize(ArrayList<UserButton> buttons1, ArrayList<UserButton> buttons2) {
        return getNewAnchorSize(buttons1.size(), buttons2.size());
    }
    private static int getNewButtonId(ArrayList<UserButton> buttons) {
        if (buttons.isEmpty()) {
            return 0;
        } else {
            return buttons.get(buttons.size() - 1).buttonId + 1;
        }
    }
    private static void addSubButtonsToButtonPane(Pane buttonPane, Button clickable, Button edit, Button close) {
        double width = buttonPane.getPrefWidth();
        double height = buttonPane.getPrefHeight();

        // set up clickable button
        clickable.setPrefHeight(Math.round(height));
        clickable.setPrefWidth(Math.round(width - ((2 * buttonHeight) + 5)));

        // set up edit button
        edit.setPrefHeight(Math.round(0.94 * height));
        edit.setPrefWidth(Math.round(0.94 * height));
        edit.setLayoutX(Math.round((clickable.getPrefWidth() + clickable.getLayoutX()) + (0.08 * height)));
        edit.setLayoutY(Math.round(0.04 * height));
        Image editImage = new Image("resources/edit.png");
        ImageView editImageView = new ImageView(editImage);
        editImageView.setFitHeight(25);
        editImageView.setFitWidth(25);
        edit.setGraphic(editImageView);

        // set up delete button
        close.setPrefHeight(Math.round(0.94 * height));
        close.setPrefWidth(Math.round(0.94 * height));
        close.setLayoutX(Math.round((edit.getPrefWidth() + edit.getLayoutX()) + (0.08 * height)));
        close.setLayoutY(Math.round(0.04 * height));
        Image closeImage = new Image("resources/cross.png");
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitHeight(25);
        closeImageView.setFitWidth(25);
        close.setGraphic(closeImageView);

        // add buttons to button pane
        buttonPane.getChildren().add(clickable);
        buttonPane.getChildren().add(edit);
        buttonPane.getChildren().add(close);
    }

    /**
     *
     * @param buttons List of buttons this button will belong.
     * @param plusButton Plus button for adding new buttons.
     * @param scrollAnchor Scroll anchor all these buttons belong to.
     * @return UserButton with components: id, type, buttonPane, buttonClickable
     */
    public static UserButton createButton(ArrayList<UserButton> buttons, Button plusButton, AnchorPane scrollAnchor,
                                          double buttonWidth, double buttonOffsetFromLeft, Pane buttonEditorPane) {
        System.out.println(".Creating new button");

        // get new button position, set new plus button position
        double buttonPositionY = plusButton.getLayoutY();
        plusButton.setLayoutY(buttonPositionY + buttonHeight + buttonOffsets);

        // set new anchor size
        double newAnchorSize = getNewAnchorSize(buttons.size(), 0);
        if (newAnchorSize > scrollAnchor.getPrefHeight()) {
            System.out.println("New anchor size = " + newAnchorSize);
            scrollAnchor.setPrefHeight(newAnchorSize);
        } else {
            System.out.println("Keeping same Anchor size");
        }

        // create new button with id text in list
        int newButtonId = getNewButtonId(buttons);
        UserButton userButton = new UserButton(newButtonId);

        // create button pane
        Pane newButtonPane = new Pane();
        newButtonPane.setPrefSize(buttonWidth, buttonHeight);

        // set new Button Pane
        newButtonPane.setLayoutX(buttonOffsetFromLeft);
        newButtonPane.setLayoutY(buttonPositionY);

        // set new button click
        Button buttonClick = new Button();
        buttonClick.setFont(new Font(23));
        buttonClick.setOnMouseClicked(e -> clickedButton(userButton, buttonEditorPane));

        // set new Button edit
        Button buttonEdit = new Button();
        buttonEdit.setOnMouseClicked(e -> openButtonEditor(userButton, buttonEditorPane));

        // set new Button close
        Button buttonClose = new Button();
        buttonClose.setOnMouseClicked(e -> deleteButton(userButton, buttons, scrollAnchor, plusButton));

        // add sub buttons
        addSubButtonsToButtonPane(newButtonPane, buttonClick, buttonEdit, buttonClose);

        // add button to scene
        scrollAnchor.getChildren().add(newButtonPane);
        System.out.println("Button added to scene");

        // add javaFX objects to new button
        userButton.buttonPane = newButtonPane;
        userButton.buttonClickable = buttonClick;

        System.out.println("New button created.");
        return userButton;
    }
    public static UserButton addNewButton(ArrayList<UserButton> buttons, Button plusButton, AnchorPane scrollAnchor,
                                          double buttonWidth, double buttonOffsetFromLeft, Pane buttonEditorPane) {
        UserButton button = createButton(buttons, plusButton, scrollAnchor,
                buttonWidth, buttonOffsetFromLeft, buttonEditorPane);
        buttons.add(button);
        System.out.println(".and added to buttons list.");
        return button;
    }
    public static void addButtonToScene(UserButton buttonToAdd, ArrayList<UserButton> buttons, Button plusButton,
                                              AnchorPane scrollAnchor, double buttonWidth, double buttonOffsetFromLeft,
                                              Pane buttonEditorPane) {
        System.out.println(".Creating new button");

        // get new button position, set new plus button position
        double buttonPositionY = plusButton.getLayoutY();
        plusButton.setLayoutY(buttonPositionY + buttonHeight + buttonOffsets);

        // set new anchor size
        double newAnchorSize = getNewAnchorSize(buttons.size(), 0);
        if (newAnchorSize > scrollAnchor.getPrefHeight()) {
            System.out.println("New anchor size = " + newAnchorSize);
            scrollAnchor.setPrefHeight(newAnchorSize);
        } else {
            System.out.println("Keeping same Anchor size");
        }

        // create button pane
        Pane newButtonPane = new Pane();
        newButtonPane.setPrefSize(buttonWidth, buttonHeight);

        // set new Button Pane
        newButtonPane.setLayoutX(buttonOffsetFromLeft);
        newButtonPane.setLayoutY(buttonPositionY);

        // set new button click
        Button buttonClick = new Button();
        buttonClick.setFont(new Font(23));
        buttonClick.setText(buttonToAdd.title);
        buttonClick.setOnMouseClicked(e -> clickedButton(buttonToAdd, buttonEditorPane));

        // set new Button edit
        Button buttonEdit = new Button();
        buttonEdit.setOnMouseClicked(e -> openButtonEditor(buttonToAdd, buttonEditorPane));

        // set new Button close
        Button buttonClose = new Button();
        buttonClose.setOnMouseClicked(e -> deleteButton(buttonToAdd, buttons, scrollAnchor, plusButton));

        // add sub buttons
        addSubButtonsToButtonPane(newButtonPane, buttonClick, buttonEdit, buttonClose);

        // add button to scene
        scrollAnchor.getChildren().add(newButtonPane);

        // add javaFX objects to new button
        buttonToAdd.buttonPane = newButtonPane;
        buttonToAdd.buttonClickable = buttonClick;

        System.out.println("Button added to scene.");
    }
    private static void clickedButton(UserButton button, Pane editorPane) {
        switch (button.typeOfButton) {
            case 0:
                // if type is 0 -> link to another slide
                Global.openNewSlide(button.linkTarget);
                break;
            default:
                // if type is 1 -> text field
                // if type is 2 -> list of another buttons
                openButtonEditor(button, editorPane);
                break;
        }
    }
    private static void deleteButton(UserButton button, ArrayList<UserButton> buttons, AnchorPane scrollAnchor,
                                     Button plusButton) {
        System.out.println(".Deleting button: " + button.buttonId);

        // remove button from scene
        scrollAnchor.getChildren().remove(button.buttonPane);

        // move other buttons up
        for (UserButton movingButton : buttons) {
            if (movingButton.buttonId > button.buttonId) {
                movingButton.buttonPane.setLayoutY(movingButton.buttonPane.getLayoutY() - 64d);
            }
        }

        // remove button from left buttons list
        int buttonIndex = Global.getButtonPositionInListById(buttons, button.buttonId);
        buttons.remove(buttonIndex);

        // set new anchor size
        double newAnchorSize = getNewAnchorSize(buttons.size(), 0);
        if (newAnchorSize >= (scrollAnchor.getPrefHeight() - (buttonHeight + buttonOffsets))) {
            System.out.println("New anchor size = " + newAnchorSize);
            scrollAnchor.setPrefHeight(newAnchorSize);
        } else {
            System.out.println("Not resizing anchor");
        }

        // set new plus button position
        double buttonPositionY = plusButton.getLayoutY();
        plusButton.setLayoutY(buttonPositionY - (buttonHeight + buttonOffsets));
        FileManager.saved = false;

        // remove all links from linksPointingHere
        switch (button.typeOfButton) {
            // if type is 0 -> link to another slide
            case 0:
                EditorButton.remove_linkPointingHere_fromBasicSlideInfo(button.linkTarget);
                break;
            // if type is 2 -> list of another buttons
            case 2:
                for (UserButton userButton : button.leftButtons) removeLinksPointingHereFromButton(userButton);
                for (UserButton userButton : button.rightButtons) removeLinksPointingHereFromButton(userButton);
                break;
        }

        System.out.println("Button deleted.");
    }
    private static void removeLinksPointingHereFromButton(UserButton button) {
        switch (button.typeOfButton) {
            // if type is 0 -> link to another slide
            case 0:
                EditorButton.remove_linkPointingHere_fromBasicSlideInfo(button.linkTarget);
                break;
            // if type is 2 -> list of another buttons
            case 2:
                for (UserButton userButton : button.leftButtons) removeLinksPointingHereFromButton(userButton);
                for (UserButton userButton : button.rightButtons) removeLinksPointingHereFromButton(userButton);
                break;
        }
    }

    public static void openButtonEditor(UserButton button, Pane editorPane) {
        Global.buttonEditor_button = button;
        String path = "editorButton.fxml";

        System.out.println("Opening scene in button pane: " + path);
        try {
            Pane pane = FXMLLoader.load(Editor.class.getResource(path));
            editorPane.getChildren().removeAll(editorPane.getChildren());
            editorPane.getChildren().addAll(pane);
            editorPane.setVisible(true);
            editorPane.getParent().setVisible(true);
        } catch (Exception e) {
            System.out.println("No chance to get there, error: " +e.toString());
            // No chance to get there until all opened scenes are in available
        }
    }

}
