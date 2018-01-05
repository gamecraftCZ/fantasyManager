package fantasyManager;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        this.typeOfButton = 0;
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

    public Element toElement(Document doc) {
        // prepare elements //
        if (title == null) { title = ""; }
        if (subTitle == null) { subTitle = ""; }
        if (linkTarget == null) { linkTarget = ""; }
        if (text == null) { text = ""; }

        // create document //
        Element element = doc.createElement("button");
        // title
        Element titleElement = doc.createElement("title");  // create tag
        titleElement.insertBefore(doc.createTextNode(title), titleElement.getLastChild());  // add text
        element.appendChild(titleElement);  // add tag to slide
        // sub title
        Element subTitleElement = doc.createElement("subtitle");  // create tag
        subTitleElement.insertBefore(doc.createTextNode(subTitle), subTitleElement.getLastChild());  // add text
        element.appendChild(subTitleElement);  // add tag to slide
        // type
        Element typeElement = doc.createElement("type");  // create tag
        typeElement.insertBefore(doc.createTextNode(Integer.toString(typeOfButton)), typeElement.getLastChild());  // add text
        element.appendChild(typeElement);  // add tag to slide
        // text
        Element textElement = doc.createElement("text");  // create tag
        textElement.insertBefore(doc.createTextNode(text), textElement.getLastChild());  // add text
        element.appendChild(textElement);  // add tag to slide
        // link
        Element linkElement = doc.createElement("link");  // create tag
        linkElement.insertBefore(doc.createTextNode(linkTarget), linkElement.getLastChild());  // add text
        element.appendChild(linkElement);  // add tag to slide
        // leftButtons
        if (leftButtons != null && !leftButtons.isEmpty()) {
            Element leftButtonsElement = doc.createElement("leftButtons");  // create tag
            for (UserButton button : leftButtons) {
                leftButtonsElement.appendChild(button.toElement(doc));
            }
            element.appendChild(leftButtonsElement);  // add tag to slide
        }
        // rightButtons
        if (rightButtons != null && !rightButtons.isEmpty()) {
            Element rightButtonsElement = doc.createElement("rightButtons");  // create tag
            for (UserButton button : rightButtons) {
                rightButtonsElement.appendChild(button.toElement(doc));
            }
            element.appendChild(rightButtonsElement);  // add tag to slide
        }

        return element;
    }

}
