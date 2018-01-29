package fantasyManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

public class BasicSlideInfo {

    public String name;
    public int id;
    public String path;
    public String type;
    public ArrayList<String> slidesPointingHere = new ArrayList<>();

    public BasicSlideInfo() {
        this.name = "";
        this.path ="";
        this.type = "";
    }
    public BasicSlideInfo(String name, String path, String type, int id) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public String getType() {
        return type;
    }

    public Element getAsElement(Document doc) {
        Element slideInfoElement = doc.createElement("item");
        // set id
        slideInfoElement.setAttribute("id", ""+id);
        // set name
        Element nameElement = doc.createElement("jmeno");
        nameElement.insertBefore(doc.createTextNode(name), nameElement.getLastChild());
        slideInfoElement.appendChild(nameElement);
        // set path
        Element pathElement = doc.createElement("jmeno");
        pathElement.insertBefore(doc.createTextNode(path), pathElement.getLastChild());
        slideInfoElement.appendChild(pathElement);
        // set type
        Element typeElement = doc.createElement("jmeno");
        typeElement.insertBefore(doc.createTextNode(type), typeElement.getLastChild());
        slideInfoElement.appendChild(typeElement);
        // set slidesPointingHere
        Element slidesPointingHereElement = doc.createElement("jmeno");

        String slidesPointingHereAsText = "";
        for (String slidePathPointingHere : slidesPointingHere) {
            slidesPointingHereAsText = slidesPointingHereAsText + slidePathPointingHere + " ";
        }
        slidesPointingHereAsText = slidesPointingHereAsText.substring(slidesPointingHereAsText.length() - 2);

        slidesPointingHereElement.insertBefore(doc.createTextNode(slidesPointingHereAsText), slidesPointingHereElement.getLastChild());
        slideInfoElement.appendChild(slidesPointingHereElement);

        return slideInfoElement;
    }

}
