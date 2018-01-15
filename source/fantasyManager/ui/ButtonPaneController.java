package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import fantasyManager.UserButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class ButtonPaneController {

    @FXML private TextField buttonTitle;
    @FXML private TextField buttonSubTitle;
    @FXML private ChoiceBox typeOfButton;
    @FXML private Pane text;
    @FXML private Pane buttons;
    @FXML private Pane link;
    @FXML private TextArea buttonTextArea;

    private boolean isButtonRight;
    private int buttonId;

    @FXML public void initialize() {
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
        System.out.println("Opening editor for button id: " + buttonId + " on position: " +buttonPosition);
        typeOfButton.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number n1, Number n2) {
                typeChanged(typeOfButton.getItems().get((Integer) n2).toString());
            }
        });
        if (isButtonRight) {
            // button is right
        } else {
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
                if (Global.slide.leftButtons.get(i).buttonId == buttonId) {
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


}
