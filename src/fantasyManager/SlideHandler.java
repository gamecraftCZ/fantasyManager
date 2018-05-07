/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager;

import fantasyManager.fileManager.FileLoader;
import fantasyManager.fileManager.Saves;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

import static fantasyManager.Global.sortButtons;

public class SlideHandler {

    private static final boolean sortLeftButtons = true;
    private static final boolean sortRightButtons = true;

    public String name; // name of character/place/organisation
    public String upSlide; // slide where to go on click on up button
    public String path; // path to location of this character/place/organisation
    public String info;
    public ArrayList<Integer> images = new ArrayList<>(); // list of images id's in this slide
    public ArrayList<UserButton> leftButtons = new ArrayList<>(); // list of buttons on left side
    public ArrayList<UserButton> rightButtons = new ArrayList<>(); // list of buttons on right side

    public boolean loadedCorrectly;

    public SlideHandler(String name, String upSlide) {
        this.name = name;
        if (upSlide.length() > 10) {
            this.upSlide = upSlide;
        } else {
            this.upSlide = "index.xml";
        }
        this.path = "";
    }
    public SlideHandler(String slidePath) {
        // load slide from file
        System.out.println("Creating slide handler, path: " + slidePath);
        path = slidePath;
        Document doc;
        if (slidePath.length() < 10) {
            // edit file path //
            try {
                if (slidePath.isEmpty()) {
                    path = "index.xml";
                } else {
                    boolean last9CharactersAreIndexXml = slidePath.substring(slidePath.length() - 9).equals("index.xml");
                    boolean last1CharacterIsSlash = slidePath.substring(slidePath.length() - 1).equals("/");
                    System.out.println("Last 9 characters of path are \"index.xml\": " + last9CharactersAreIndexXml);
                    System.out.println("Last character of path is \"/\": " + last1CharacterIsSlash);
                    if (last9CharactersAreIndexXml) {
                        path = slidePath;
                    } else if (!last1CharacterIsSlash) {
                        path = slidePath + "/index.xml";
                    } else {
                        path = slidePath + "index.xml";
                    }
                }
            } catch(Exception ex){
                if (!(slidePath.substring(slidePath.length() - 1).equals("/"))) {
                    path = slidePath + "/index.xml";
                } else {
                    path = slidePath + "index.xml";
                }
            }
        } else {
            path = slidePath;
        }
        System.out.println("Edited path: " + path);
        // load data
        try {
            doc = FileLoader.getFileAsDocument(path);
            loadData(doc);
            loadedCorrectly = true;
        } catch (Exception ex) {
            System.out.println("Cant load slide correctly " +slidePath
                    + " from project file "
                    + Saves.getFileObject() +
                    ", error: " + ex.toString());
            Global.showError("Project file error", "Cant load slide "
                    + slidePath + " from project "
                    + Saves.getFileObject()
                    + "\n Slide file is broken, error: \n"
                    + ex.toString());
            loadedCorrectly = false;
        }
    }
    private void loadData(Document doc) throws Exception {

        System.out.println("Reading slide file");

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();

        // get name //
        System.out.println("Getting name");
        XPathExpression expr = xPath.compile("/slide/name[1]/text()");
        this.name = (String) expr.evaluate(doc, XPathConstants.STRING);
        System.out.println("Name: " + name);

        // get up slide //
        System.out.println("Getting up slide");
        expr = xPath.compile("/slide/upSlide[1]/text()");
        this.upSlide = (String) expr.evaluate(doc, XPathConstants.STRING);
        System.out.println("Up slide: " + name);

        // get info //
        System.out.println("Getting info");
        expr = xPath.compile("/slide/info[1]/text()");
        this.info = (String) expr.evaluate(doc, XPathConstants.STRING);
        System.out.println("Info: " + info);

        // get images //
        System.out.println("Getting images");
        expr = xPath.compile("/slide/images[1]/text()");
        String[] images = ((String) expr.evaluate(doc, XPathConstants.STRING)).split(" ");
        if (!images[0].equals("")) {
            for (int i = 0; i < images.length; i++) {
                this.images.add(Integer.parseInt(images[i]));
                System.out.println("Image " + i + " = " + images[i]);
            }
        }

        // get left buttons //
        System.out.println("Getting left buttons");
        NodeList nodeList;
        try {
            expr = xPath.compile("/slide/leftButtons[1]/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println("Getting info for left button " + i);
                leftButtons.add(getButtonInfo(xPath, doc,
                        "/slide/leftButtons[1]/button[" +(i+1)+ "]", i));
            }
            if (sortLeftButtons) sortButtons(leftButtons);

        } catch (Exception ex) {
            System.out.println("Probably no left buttons, error: " +ex.toString());
        }

        // get right buttons //
        System.out.println("Getting right buttons");
        try {
            expr = xPath.compile("/slide/rightButtons[1]/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println("Getting info for right button " +i);
                rightButtons.add(getButtonInfo(xPath, doc,
                        "/slide/rightButtons[1]/button[" +(i+1)+ "]", i));
            }
            if (sortRightButtons) sortButtons(rightButtons);
        } catch (Exception ex) {
            System.out.println("Probably no right buttons, error: " +ex.toString());
        }

        System.out.println("Slide file loaded");
    }
    private UserButton getButtonInfo(XPath xPath, Document doc, String pathToButton, int buttonId) throws Exception {
        System.out.println("Getting button info");
        // get button data //
        XPathExpression expr = xPath.compile(pathToButton + "/title/text()");
        String title = (String) expr.evaluate(doc, XPathConstants.STRING);
        expr = xPath.compile(pathToButton + "/subtitle/text()");
        String subTitle = (String) expr.evaluate(doc, XPathConstants.STRING);
        expr = xPath.compile(pathToButton + "/text/text()");
        String text = (String) expr.evaluate(doc, XPathConstants.STRING);
        expr = xPath.compile(pathToButton + "/type/text()");
        int type = Integer.parseInt((String) expr.evaluate(doc, XPathConstants.STRING));
        expr = xPath.compile(pathToButton + "/link/text()");
        String link = (String) expr.evaluate(doc, XPathConstants.STRING);

        NodeList nodeList;
        ArrayList<UserButton> leftButtonsList = new ArrayList<>();
        ArrayList<UserButton> rightButtonsList = new ArrayList<>();
        // get left sub buttons //
        try {
            expr = xPath.compile(pathToButton + "/leftButtons/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 1; i < nodeList.getLength()+1; i++) {
                System.out.println("Getting info for left sub button " +i);
                leftButtonsList.add(getButtonInfo(xPath, doc,
                        pathToButton + "/leftButtons/button[" +i+ "]", i-1));
            }
        } catch (Exception ex) {
            System.out.println("Probably no left sub buttons, error: " +ex.toString());
        }
        // get right sub buttons //
        try {
            expr = xPath.compile(pathToButton + "/rightButtons/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 1; i < nodeList.getLength() + 1; i++) {
                System.out.println("Getting info for right sub button " + i);
                rightButtonsList.add(getButtonInfo(xPath, doc,
                        pathToButton + "/rightButtons/button[" + i + "]", i-1));
            }
        } catch (Exception ex) {
            System.out.println("Probably no right sub buttons, error: " +ex.toString());
        }


        UserButton userButtonObj = new UserButton(title, subTitle, buttonId, type, link, text,
                leftButtonsList, rightButtonsList);
        System.out.println("Sub button info loaded");
        return userButtonObj;
    }

    public Document toDocument() {
        try {
            // prepare elements //
            if (name == null) { name = ""; }
            if (upSlide == null) { upSlide = ""; }
            if (info == null) { info = ""; }

            // create document //
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();

            // create the root element node
            Element slide = doc.createElement("slide");
            doc.appendChild(slide);
            // name //
            Element nameElement = doc.createElement("name");  // create tag
            nameElement.insertBefore(doc.createTextNode(name), nameElement.getLastChild());  // add text
            slide.appendChild(nameElement);  // add tag to slide
            // up slide //
            Element upSlideElement = doc.createElement("upSlide");  // create tag
            upSlideElement.insertBefore(doc.createTextNode(upSlide), upSlideElement.getLastChild());  // add text
            slide.appendChild(upSlideElement);  // add tag to slide
            // info //
            Element infoElement = doc.createElement("info");  // create tag
            infoElement.insertBefore(doc.createTextNode(info), infoElement.getLastChild());  // add text
            slide.appendChild(infoElement);  // add tag to slide
            // images //
            Element imagesElement = doc.createElement("images");  // create tag
            imagesElement.insertBefore(doc.createTextNode(getImagesAsString()), imagesElement.getLastChild());  // text
            slide.appendChild(imagesElement);  // add tag to slide
            // left buttons //
            if (leftButtons != null && !leftButtons.isEmpty()) {
                slide.appendChild(getButtonsAsElement(leftButtons, "leftButtons", doc));
            }
            // right buttons //
            if (rightButtons != null && !rightButtons.isEmpty()) {
                slide.appendChild(getButtonsAsElement(rightButtons, "rightButtons", doc));
            }

            return doc;
        } catch (Exception ex) {
            System.out.println("Cant convert slide " + name + " to document, error: " +ex.toString());
            // should not get here
            return null;
        }
    }
    private String getImagesAsString() {
        StringBuilder imagesStringBuilder = new StringBuilder();
        for (int s : images) {
            imagesStringBuilder.append(s);
            imagesStringBuilder.append(" ");
        }
        return imagesStringBuilder.toString();
    }

    private Element getButtonsAsElement(ArrayList<UserButton> buttons, String elementName, Document doc) {
        Element buttonElement = doc.createElement(elementName);
        for (UserButton button : buttons) {
            buttonElement.appendChild(button.toElement(doc));
        }

        return buttonElement;
    }

    public int getId() {
        int pos = path.lastIndexOf("/");
        String idString = path.substring(pos + 1);
        return Integer.parseInt(idString);
    }

}
