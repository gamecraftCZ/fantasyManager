/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

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

    // Used by tableView builder!
    public String getName() {
        return name;
    }
    // Used by tableView builder!
    public String getPath() {
        return path;
    }
    // Used by tableView builder!
    public String getType() {
        return type;
    }

    public Element getAsElement(Document doc) {
        Element slideInfoElement = doc.createElement("item");
        // set id
        slideInfoElement.setAttribute("id", ""+id);
        // set name
        Element nameElement = doc.createElement("name");
        nameElement.insertBefore(doc.createTextNode(name), nameElement.getLastChild());
        slideInfoElement.appendChild(nameElement);
        // set path
        Element pathElement = doc.createElement("path");
        pathElement.insertBefore(doc.createTextNode(path), pathElement.getLastChild());
        slideInfoElement.appendChild(pathElement);
        // set slidesPointingHere
        Element slidesPointingHereElement = doc.createElement("slidesPointingHere");

        String slidesPointingHereAsText = String.join(" ", slidesPointingHere);

        slidesPointingHereElement.insertBefore(doc.createTextNode(slidesPointingHereAsText), slidesPointingHereElement.getLastChild());
        slideInfoElement.appendChild(slidesPointingHereElement);

        return slideInfoElement;
    }

}
