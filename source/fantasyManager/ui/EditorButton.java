/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class EditorButton {

    private static final double buttonWidth = 365;
    private static final double leftButtonsOffsetFromLeft = 16;
    private static final double rightButtonsOffsetFromLeft = 385;

    private static final short openedInPopOut_selectUpSlide = 1;

    @FXML private TextField buttonTitle;
    @FXML private TextField buttonSubTitle;
    @FXML private ChoiceBox typeOfButton;
    @FXML private Pane text;
    @FXML private Pane buttons;
    @FXML private Pane link;
    @FXML private TextArea buttonTextArea;
    @FXML private Pane popOutRoot;
    @FXML private Pane popOutPane;
    @FXML private Label linkName;

    @FXML private Button leftPlus;
    @FXML private Button rightPlus;
    @FXML private AnchorPane scrollAnchor;

    private UserButton editingButton;

    private short openedInPopOut;

    @FXML public void initialize() {
        try {
            System.out.println("Initializing button editor");

            editingButton = Global.buttonEditor_button;

            loadButtonObjectToScene(editingButton);

            loadSubButtons();
            addTypeChangeListener();
            addPopOutCloseListener();

            System.out.println("Button editor initialized");
        } catch (Exception ex) {
            System.out.println("Button editor initialization error: " + ex.toString());
        }
    }
    private void addPopOutCloseListener() {
        popOutRoot.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observableValue, final Boolean aBoolean,
                                final Boolean aBoolean2) {
                if (aBoolean) popOutClosed(); // set invisible -> path was selected
            }
        });
    }
    private void addTypeChangeListener() {
        typeOfButton.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number n1, Number n2) {
                typeChanged(typeOfButton.getItems().get((Integer) n2).toString());
            }
        });
    }
    private void loadButtonObjectToScene(UserButton button) {
        buttonTitle.setText(button.title);
        buttonSubTitle.setText(button.subTitle);
        buttonTextArea.setText(button.text);

        int linkTarget = Global.getSlidePositionInSlidesListByPath(button.linkTarget);
        if (linkTarget == -1) {
            System.out.println("Link target not found -> should be main slide");
        } else {
            linkName.setText(Global.slidesList.get(linkTarget).name);
            System.out.println("Link target name: " + linkName.getText());
        }

        switch (button.typeOfButton) {
            case 2:
                // if type is 2 -> list of another buttons
                typeOfButton.setValue("rozcestník");
                text.setVisible(false);
                buttons.setVisible(true);
                link.setVisible(false);
                break;
            case 1:
                // if type is 1 -> text field
                typeOfButton.setValue("text");
                text.setVisible(true);
                buttons.setVisible(false);
                link.setVisible(false);
                break;
            default:
                // if type is 0 -> link to another slide
                typeOfButton.setValue("odkaz");
                text.setVisible(false);
                buttons.setVisible(false);
                link.setVisible(true);
                break;
        }
    }
    private void loadSubButtons() {
        // left buttons
        for (UserButton button : editingButton.leftButtons) {
            EditorManager.addButtonToScene(button, editingButton.leftButtons,
                    leftPlus, scrollAnchor, buttonWidth, leftButtonsOffsetFromLeft, popOutPane);
        }
        // right buttons
        for (UserButton button : editingButton.rightButtons) {
            EditorManager.addButtonToScene(button, editingButton.rightButtons,
                    rightPlus, scrollAnchor, buttonWidth, rightButtonsOffsetFromLeft, popOutPane);
        }
    }

    public void titleChanged() {
        System.out.println("New title: " + buttonTitle.getText());
        editingButton.title = buttonTitle.getText();
        editingButton.buttonClickable.setText(buttonTitle.getText());
        FileManager.saved = false;
    }
    public void subTitleChanged() {
        System.out.println("New subtitle: " + buttonSubTitle.getText());
        editingButton.subTitle = buttonSubTitle.getText();
        FileManager.saved = false;
    }
    public void textAreaChanged() {
        System.out.println("New text: " + buttonTextArea.getText());
        editingButton.text = buttonTextArea.getText();
        FileManager.saved = false;
    }

    private void typeChanged(String buttonType) {
        System.out.println("New button type: " + buttonType);

        switch (buttonType) {
            case "rozcestník":
                // if type is 2 -> list of another buttons
                editingButton.typeOfButton = 2;
                text.setVisible(false);
                buttons.setVisible(true);
                link.setVisible(false);
                remove_linkPointingHere_fromBasicSlideInfo(editingButton.linkTarget);
                break;
            case "text":
                // if type is 1 -> text field
                editingButton.typeOfButton = 1;
                text.setVisible(true);
                buttons.setVisible(false);
                link.setVisible(false);
                remove_linkPointingHere_fromBasicSlideInfo(editingButton.linkTarget);
                break;
            default:
                // if type is 0 -> link to another slide
                editingButton.typeOfButton = 0;
                text.setVisible(false);
                buttons.setVisible(false);
                link.setVisible(true);
                add_LinkPointingHere_ToBasicSlideInfo(editingButton.linkTarget);
                break;
        }

        FileManager.saved = false;
    }
    public static void add_LinkPointingHere_ToBasicSlideInfo(String linkPath) {
        if (!linkPath.isEmpty()) {
            BasicSlideInfo slide = Global.getSlideInSlidesListByPath(linkPath);
            if (slide == null) {
                return;
            }
            System.out.println("Adding link pointing here \"" + Global.slide.path + "\" to " + slide.path);
            slide.slidesPointingHere.add(Global.slide.path);
        }
    }
    public static void remove_linkPointingHere_fromBasicSlideInfo(String linkPath) {
        BasicSlideInfo slideInfo = Global.getSlideInSlidesListByPath(linkPath);
        if (slideInfo == null) {
            return;
        }
        for (int i = 0; i < slideInfo.slidesPointingHere.size(); i++) {
            if (slideInfo.slidesPointingHere.get(i).equals(Global.slide.path)) {
                System.out.println("Removing link pointing here");
                slideInfo.slidesPointingHere.remove(i);
                return;
            }
        }
        System.out.println("NO LINK POINTING HERE REMOVED!\\/\n " +
                "current slide path: " + Global.slide.path + " ,slide to remove: " + linkPath);
    }

    public void getLink() {
        System.out.println("Getting up slide for new slide");
        openUpSlideAskWindow();
    }
    private void popOutClosed() {
        System.out.println("Pop out closed");

        switch (openedInPopOut) {
            case openedInPopOut_selectUpSlide:
                System.out.println("Link selected: " + Global.selectSlide_Selected);
                if (Global.selectSlide_Selected != null) {
                    // Path link is set, set its name to linkName object for user
                    String linkPath = Global.selectSlide_Selected;
                    if (linkPath.isEmpty()) {
                        linkName.setText("Hlavní rozcestník");
                    } else {
                        for (BasicSlideInfo slide : Global.slidesList) {
                            if (slide.path.equals(linkPath)) {
                                linkName.setText(slide.name);
                                break;
                            }
                        }
                    }

                    remove_linkPointingHere_fromBasicSlideInfo(editingButton.linkTarget);
                    add_LinkPointingHere_ToBasicSlideInfo(linkPath);

                    editingButton.linkTarget = linkPath;
                }
                break;
        }
        openedInPopOut = 0; // nothing opened
    }

    private void openUpSlideAskWindow() {
        String scenePath = "selectSlide.fxml";
        System.out.println("Opening up slide ask window: " + scenePath);
        try {
            Global.selectSlide_Prompt = "Zvol nadřazené";
            Global.selectSlide_Pane = popOutRoot;
            Global.selectSlide_SearchAlsoForCurrentSlide = false;
            Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
            popOutRoot.getChildren().removeAll(popOutRoot.getChildren());
            popOutRoot.getChildren().addAll(pane);
            popOutRoot.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
        openedInPopOut = openedInPopOut_selectUpSlide;
    }

    public void addLeftButton() {
        System.out.println(".Adding button to left");
        FileManager.saved = false;

        UserButton button = EditorManager.addNewButton(editingButton.leftButtons,
                leftPlus, scrollAnchor, buttonWidth, leftButtonsOffsetFromLeft, popOutPane);
        EditorManager.openButtonEditor(button, popOutPane);

        System.out.println("Button added to left");
    }
    public void addRightButton() {
        System.out.println("Adding button to right");
        FileManager.saved = false;

        UserButton button = EditorManager.addNewButton(editingButton.rightButtons,
                rightPlus, scrollAnchor, buttonWidth, rightButtonsOffsetFromLeft, popOutPane);
        EditorManager.openButtonEditor(button, popOutPane);

        System.out.println("Button added to right");
    }

    public void close() {
        text.getParent().getParent().getParent().setVisible(false);
    }

}
