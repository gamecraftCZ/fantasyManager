package fantasyManager.ui;

import fantasyManager.*;
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
                for (BasicSlideInfo slide : Global.slidesList) {
                    if (slide.path.equals(Global.slide.rightButtons.get(buttonPosition).linkTarget)) {
                        linkName.setText(slide.name);
                        break;
                    }
                }
                System.out.println("Link target name: " + linkName.getText());

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
                for (BasicSlideInfo slide : Global.slidesList) {
                    if (slide.path.equals(Global.slide.leftButtons.get(buttonPosition).linkTarget)) {
                        linkName.setText(slide.name);
                        break;
                    }
                }
                System.out.println("Link target name: " + linkName.getText());

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
            for (int i = 0; i < Global.slide.rightButtons.size(); i++)
                if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                    buttonPosition = i;
                    break;
                }
            // if link was selected -> remove current link from slide it was pointing
            String linkTarget = Global.getButtonById(Global.slide.rightButtons, buttonId).linkTarget;
            for (int i = 0; i < Global.slidesList.size(); i++) {
                for (int ii = 0; ii < Global.slidesList.get(i).slidesPointingHere.size(); ii++) {
                    if (Global.slidesList.get(i).slidesPointingHere.get(ii).equals(linkTarget)) {
                        System.out.println("Removing link pointing here");
                        Global.slidesList.get(i).slidesPointingHere.remove(ii);
                        break;
                    }
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
                addLinkPointingHereToBasicSlideInfo(linkTarget);
            }
        } else {
            for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                    buttonPosition = i;
                    break;
                }
            }
            // if link was selected -> remove current link from slide it was pointing
            String linkTarget = Global.getButtonById(Global.slide.leftButtons, buttonId).linkTarget;
            for (int i = 0; i < Global.slidesList.size(); i++) {
                for (int ii = 0; ii < Global.slidesList.get(i).slidesPointingHere.size(); ii++) {
                    if (Global.slidesList.get(i).slidesPointingHere.get(ii).equals(linkTarget)) {
                        System.out.println("Removing link pointing here");
                        Global.slidesList.get(i).slidesPointingHere.remove(ii);
                        break;
                    }
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
                addLinkPointingHereToBasicSlideInfo(linkTarget);
            }
        }
        FileManager.saved = false;
    }
    private void addLinkPointingHereToBasicSlideInfo(String linkPath) {
        if (!linkPath.isEmpty()) {
            int linkPosition = Global.getSlidePositionInSlidesListByPath(linkPath);
            BasicSlideInfo slide = Global.slidesList.get(linkPosition);
            System.out.println("Adding link pointing here: " + linkPath);
            slide.slidesPointingHere.add(linkPath);
        }
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

            // Set path to button object
            if (isButtonRight) {
                for (int i = 0; i < Global.slide.rightButtons.size(); i++) {
                    if (Global.slide.rightButtons.get(i).buttonId == buttonId) {
                        Global.slide.rightButtons.get(i).linkTarget = linkPath;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < Global.slide.leftButtons.size(); i++) {
                    if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
                        Global.slide.leftButtons.get(i).linkTarget = linkPath;
                        break;
                    }
                }
            }

            // Set slidePointingHere to new slide //
            // Remove old
            for (int i = 0; i < Global.slidesList.size(); i++) {
                for (int ii = 0; ii < Global.slidesList.get(i).slidesPointingHere.size(); ii++) {
                    if (Global.slidesList.get(i).slidesPointingHere.get(ii).equals(linkPath)) {
                        System.out.println("Removing link pointing here");
                        Global.slidesList.get(i).slidesPointingHere.remove(ii);
                        break;
                    }
                }
            }
            // Add new
            if (!linkPath.isEmpty()) {
                int linkPosition = Global.getSlidePositionInSlidesListByPath(linkPath);
                BasicSlideInfo slide = Global.slidesList.get(linkPosition);
                System.out.println("Adding link pointing here: " + linkPath);
                slide.slidesPointingHere.add(linkPath);
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
