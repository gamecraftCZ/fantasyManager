/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class ViewButton {

    private static final double buttonWidth = 365;
    private static final double leftButtonsOffsetFromLeft = 16;
    private static final double rightButtonsOffsetFromLeft = 385;

    @FXML private Label buttonTitle;
    @FXML private Label buttonSubTitle;
    @FXML private Label buttonTextLabel;
    @FXML private Pane text;
    @FXML private Pane buttons;
    @FXML private Pane popOutPane;

    @FXML private AnchorPane buttonViewScrollPane;

    private UserButton viewingButton;

    @FXML public void initialize() {
        try {
            System.out.println("Initializing button editor");

            viewingButton = Global.buttonEditor_button;

            buttonTitle.setText(viewingButton.title);
            buttonSubTitle.setText(viewingButton.subTitle);
            buttonTextLabel.setText(viewingButton.text);

            switch (viewingButton.typeOfButton) {
                // if type is 1 -> text field
                case 1:
                    text.setVisible(true);
                    buttons.setVisible(false);
                    break;
                // if type is 2 -> list of another buttons
                case 2:
                    text.setVisible(false);
                    buttons.setVisible(true);
                    loadSubButtons();
                    break;
            }

            System.out.println("Button editor initialized");
        } catch (Exception ex) {
            System.out.println("Button editor initialization error: " + ex.toString());
        }
    }
    private void loadSubButtons() {
        // left buttons
        for (UserButton button : viewingButton.leftButtons) {
            ViewManager.addButtonToScene(button, buttonWidth, buttonViewScrollPane,
                    leftButtonsOffsetFromLeft, popOutPane);
        }
        // right buttons
        for (UserButton button : viewingButton.rightButtons) {
            ViewManager.addButtonToScene(button, buttonWidth, buttonViewScrollPane,
                    rightButtonsOffsetFromLeft, popOutPane);
        }
        setScrollAnchorSize();
    }
    private void setScrollAnchorSize() {
        double size = EditorManager.getNewAnchorSize(viewingButton.leftButtons, viewingButton.rightButtons);
        buttonViewScrollPane.setPrefHeight(size);
    }

    public void close() {
        text.getParent().getParent().getParent().setVisible(false);
    }

}
