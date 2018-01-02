package fantasyManager;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class UserButton {

    public String title;
    public String subTitle;
    public int buttonId;
    public int typeOfButton; // 0=link to another slide, 1=text, 2=list of another buttons
    // if type is 0 -> link to another slide
    public String linkTarget;
    // if type is 1 -> text field
    public String text;
    // if type is 2 -> list of another buttons
    public ArrayList<UserButton> leftButtons;
    public ArrayList<UserButton> rightButtons;
    // button object in scene
    public Pane buttonPane;
    public Button buttonClickable;


    public UserButton(int id) {
        System.out.println("Creating blank user button with id: " +id);
        buttonId = id;
    }
    public UserButton(String title, String subTitle, int buttonId, int typeOfButton, String linkTarget, String text,
                      ArrayList<UserButton> leftButtons, ArrayList<UserButton> rightButtons) {
        System.out.println("Creating button class with: title = " +title+ ", subtitile = " +subTitle+
                ", buttonId = " +buttonId+ ", typeOfButton = " +typeOfButton+ ", linkTarget = " +linkTarget+
                ", text = " +text+ ", leftButtons, rightButtons");
        this.title = title;
        this.subTitle = subTitle;
        this.buttonId = buttonId;
        this.typeOfButton = typeOfButton;
        this.linkTarget = linkTarget;
        this.text = text;
        this.leftButtons = leftButtons;
        this.rightButtons = rightButtons;
    }

}
