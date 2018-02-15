/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class ViewManager {

    private static final double buttonHeight = 50;
    private static final double buttonOffsets = 14;
    private static final double buttonOffsetFromTop = 74;

    private static void addSubButtonsToButtonPane(Pane buttonPane, Button clickable) {
        double width = buttonPane.getPrefWidth();
        double height = buttonPane.getPrefHeight();

        // set up clickable button
        clickable.setPrefHeight(Math.round(height));
        clickable.setPrefWidth(Math.round(width));

        // add buttons to button pane
        buttonPane.getChildren().add(clickable);
    }

    public static void addButtonToScene(UserButton buttonToAdd, double buttonWidth, AnchorPane scrollAnchor,
                                        double buttonOffsetFromLeft, Pane buttonViewPane) {
        System.out.println(".Creating new button");

        // create button pane
        Pane newButtonPane = new Pane();
        newButtonPane.setPrefSize(buttonWidth, buttonHeight);

        // set new Button Pane
        newButtonPane.setLayoutX(buttonOffsetFromLeft);
        double buttonPositionY = buttonOffsetFromTop + ((buttonToAdd.buttonId - 1)* (buttonHeight + buttonOffsets));
        newButtonPane.setLayoutY(buttonPositionY);

        // set new button click
        Button buttonClick = new Button();
        buttonClick.setFont(new Font(23));
        buttonClick.setText(buttonToAdd.title);
        buttonClick.setOnMouseClicked(e -> clickedButton(buttonToAdd, buttonViewPane));

        // add sub buttons
        addSubButtonsToButtonPane(newButtonPane, buttonClick);

        // add button to scene
        scrollAnchor.getChildren().add(newButtonPane);

        // add javaFX objects to new button
        buttonToAdd.buttonPane = newButtonPane;
        buttonToAdd.buttonClickable = buttonClick;

        System.out.println("Button added to scene.");
    }
    private static void clickedButton(UserButton button, Pane buttonViewPane) {
        switch (button.typeOfButton) {
            case 0:
                // if type is 0 -> link to another slide
                Global.openNewSlide(button.linkTarget);
                break;
            default:
                // if type is 1 -> text field
                // if type is 2 -> list of another buttons
                openButtonView(button, buttonViewPane);
                break;
        }
    }

    public static void openButtonView(UserButton button, Pane editorPane) {
        Global.buttonEditor_button = button;
        String path = "viewButton.fxml";

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
