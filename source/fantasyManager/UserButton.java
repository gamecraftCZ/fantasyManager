package fantasyManager;

import java.util.ArrayList;

public class UserButton {

    public int buttonId;
    public int typeOfButton; // 0=link to another slide, 1=text, 2=list of another buttons
    // if type is 0 -> link to another slide
    public String linkTarget;
    // if type is 1 -> text field
    public String fieldText;
    // if type is 2 -> list of another buttons
    public ArrayList<UserButton> leftButtons;
    public ArrayList<UserButton> rightButtons;

}