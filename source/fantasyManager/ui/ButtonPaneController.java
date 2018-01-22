package fantasyManager.ui;

import com.sun.jna.platform.win32.GL;
import fantasyManager.BasicSlideInfo;
import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class ButtonPaneController {

    @FXML private TextField buttonTitle;
    @FXML private TextField buttonSubTitle;
    @FXML private ChoiceBox typeOfButton;
    @FXML private Pane text;
    @FXML private Pane buttons;
    @FXML private Pane link;
    @FXML private TextArea buttonTextArea;
    @FXML private Pane selectSlidePathRoot;
    @FXML private Label linkName;

    private boolean isButtonRight;
    private int buttonId;

    @FXML public void initialize() {
        System.out.println("Initializing button editor");
        try {
            selectSlidePathRoot.visibleProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(final ObservableValue<? extends Boolean> observableValue, final Boolean aBoolean,
                                    final Boolean aBoolean2) {
                    if (aBoolean) pathSelected(); // set invisible -> path was selected
                }
            });

            isButtonRight = Global.isOpeningButtonRight;
            buttonId = Global.openingButtonId;
            int buttonPosition = 0;

            if (isButtonRight) {
                for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                    if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                        buttonPosition = i;
                    }
                }
            } else {
                for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                    if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                        buttonPosition = i;
                    }
                }
            }

            System.out.println("Opening editor for button id: " + buttonId + " on position: " + buttonPosition);
            typeOfButton.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number n1, Number n2) {
                    typeChanged(typeOfButton.getItems().get((Integer) n2).toString());
                }
            });

            if (isButtonRight) { // button is right
                buttonTitle.setText(Global.slide.rightButtons.get(buttonPosition).title);
                System.out.println("Button title: " + buttonTitle.getText());
                buttonSubTitle.setText(Global.slide.rightButtons.get(buttonPosition).subTitle);
                System.out.println("Button subtitle: " + buttonSubTitle.getText());
                buttonTextArea.setText(Global.slide.rightButtons.get(buttonPosition).text);
                System.out.println("Button text: " + buttonTextArea.getText());

                if (Global.slide.rightButtons.get(buttonPosition).typeOfButton == 2) {
                    // if type is 2 -> list of another buttons
                    typeOfButton.setValue("rozcestník");
                    text.setVisible(false);
                    buttons.setVisible(true);
                    link.setVisible(false);
                } else if (Global.slide.rightButtons.get(buttonPosition).typeOfButton == 1) {
                    // if type is 1 -> text field
                    typeOfButton.setValue("text");
                    text.setVisible(true);
                    buttons.setVisible(false);
                    link.setVisible(false);
                } else {
                    // if type is 0 -> link to another slide
                    typeOfButton.setValue("odkaz");
                    text.setVisible(false);
                    buttons.setVisible(false);
                    link.setVisible(true);
                }
                System.out.println("Button type: " + typeOfButton.getValue());
            } else { // button is left
                buttonTitle.setText(Global.slide.leftButtons.get(buttonPosition).title);
                System.out.println("Button title: " + buttonTitle.getText());
                buttonSubTitle.setText(Global.slide.leftButtons.get(buttonPosition).subTitle);
                System.out.println("Button subtitle: " + buttonSubTitle.getText());
                buttonTextArea.setText(Global.slide.leftButtons.get(buttonPosition).text);
                System.out.println("Button text: " + buttonTextArea.getText());

                if (Global.slide.leftButtons.get(buttonPosition).typeOfButton == 2) {
                    // if type is 2 -> list of another buttons
                    typeOfButton.setValue("rozcestník");
                    text.setVisible(false);
                    buttons.setVisible(true);
                    link.setVisible(false);
                } else if (Global.slide.leftButtons.get(buttonPosition).typeOfButton == 1) {
                    // if type is 1 -> text field
                    typeOfButton.setValue("text");
                    text.setVisible(true);
                    buttons.setVisible(false);
                    link.setVisible(false);
                } else {
                    // if type is 0 -> link to another slide
                    typeOfButton.setValue("odkaz");
                    text.setVisible(false);
                    buttons.setVisible(false);
                    link.setVisible(true);
                }
                System.out.println("Button type: " + typeOfButton.getValue());
            }

        } catch (Exception ex) {
            System.out.println("Button editor initialization error: " + ex.toString());
        }
        System.out.println("Button editor initialized");
    }

    public void titleChanged() {
        System.out.println("New title: " + buttonTitle.getText());
        if (isButtonRight) {
            for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                    Global.slide.rightButtons.get(i).title = buttonTitle.getText();
                    Global.slide.rightButtons.get(i).buttonClickable.setText(buttonTitle.getText());
                    break;
                }
            }
        } else {
            for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                    Global.slide.leftButtons.get(i).title = buttonTitle.getText();
                    Global.slide.leftButtons.get(i).buttonClickable.setText(buttonTitle.getText());
                    break;
                }
            }
        }
        FileManager.saved = false;
    }
    public void subTitleChanged() {
        System.out.println("New subtitle: " + buttonSubTitle.getText());
        if (isButtonRight) {
            for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                    Global.slide.rightButtons.get(i).subTitle = buttonSubTitle.getText();
                    break;
                }
            }
        } else {
            for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                    Global.slide.leftButtons.get(i).subTitle = buttonSubTitle.getText();
                    break;
                }
            }
        }
        FileManager.saved = false;
    }
    public void textAreaChanged() {
        System.out.println("New text: " + buttonTextArea.getText());
        if (isButtonRight) {
            for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                    Global.slide.rightButtons.get(i).text = buttonTextArea.getText();
                    break;
                }
            }
        } else {
            for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                    Global.slide.leftButtons.get(i).text = buttonTextArea.getText();
                    break;
                }
            }
        }
        FileManager.saved = false;
    }

    public void typeChanged(String buttonType) {
        System.out.println("New button type: " + buttonType);
        int buttonPosition = 0;
        if (isButtonRight) {
            for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                    buttonPosition = i;
                    break;
                }
            }
            if (buttonType.equals("rozcestník")) {
                // if type is 2 -> list of another buttons
                Global.slide.rightButtons.get(buttonPosition).typeOfButton = 2;
                text.setVisible(false);
                buttons.setVisible(true);
                link.setVisible(false);
            } else if (buttonType.equals("text")) {
                // if type is 1 -> text field
                Global.slide.rightButtons.get(buttonPosition).typeOfButton = 1;
                text.setVisible(true);
                buttons.setVisible(false);
                link.setVisible(false);
            } else {
                // if type is 0 -> link to another slide
                Global.slide.rightButtons.get(buttonPosition).typeOfButton = 0;
                text.setVisible(false);
                buttons.setVisible(false);
                link.setVisible(true);
            }
        } else {
            for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                    buttonPosition = i;
                    break;
                }
            }
            if (buttonType.equals("rozcestník")) {
                // if type is 2 -> list of another buttons
                Global.slide.leftButtons.get(buttonPosition).typeOfButton = 2;
                text.setVisible(false);
                buttons.setVisible(true);
                link.setVisible(false);
            } else if (buttonType.equals("text")) {
                // if type is 1 -> text field
                Global.slide.leftButtons.get(buttonPosition).typeOfButton = 1;
                text.setVisible(true);
                buttons.setVisible(false);
                link.setVisible(false);
            } else {
                // if type is 0 -> link to another slide
                Global.slide.leftButtons.get(buttonPosition).typeOfButton = 0;
                text.setVisible(false);
                buttons.setVisible(false);
                link.setVisible(true);
            }
        }
        FileManager.saved = false;
    }

    public void getLink() {
        System.out.println("Getting up slide for new slide");
        openUpSlideAskWindow();
    }
    private void pathSelected() {
        System.out.println("Link selected: " + Global.selectSlideSelected);
        if (Global.selectSlideSelected != null) {
            // Path link is set, set its name to linkName object for user
            String linkPath = Global.selectSlideSelected;
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

            // Set slidePointingHere to new slide //
            // Remove old
            for (BasicSlideInfo slide : Global.slidesList) {
                for (int i = 0; i < slide.slidesPointingHere.size(); i++) {
                    if (slide.slidesPointingHere.get(i).equals(linkPath)) {
                        System.out.println("Removing link pointing here");
                        slide.slidesPointingHere.remove(i);
                        break;
                    }
                }
            }
            // Add new
            for (BasicSlideInfo slide : Global.slidesList) {
                if (slide.path.equals(linkPath)) {
                    System.out.println("Adding link pointing here: " + linkPath);
                    slide.slidesPointingHere.add(linkPath);
                    break;
                }
            }


            // Set path to button object
            if (isButtonRight) {
                for (UserButton button : Global.slide.rightButtons) {
                    if (button.buttonId == buttonId) {
                        button.linkTarget = linkPath;
                        break;
                    }
                }
            } else {
                for (UserButton button : Global.slide.leftButtons) {
                    if (button.buttonId == buttonId) {
                        button.linkTarget = linkPath;
                        break;
                    }
                }
            }

        }
    }

    private void openUpSlideAskWindow() {
        String scenePath = "selectSlide.fxml";
        System.out.println("Opening up slide ask window: " + scenePath);
        try {
            Global.selectSlidePrompt = "Zvol nadřazené";
            Global.selectSlidePane = selectSlidePathRoot;
            Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
            selectSlidePathRoot.getChildren().removeAll(selectSlidePathRoot.getChildren());
            selectSlidePathRoot.getChildren().addAll(pane);
            selectSlidePathRoot.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }


}
