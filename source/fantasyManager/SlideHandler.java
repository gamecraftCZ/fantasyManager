package fantasyManager;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SlideHandler {

    public String name; // name of character/place/organisation
    public String path; // path to location of this character/place/organisation
    public ArrayList<Info> infos = new ArrayList<>();
    public ArrayList<String> linksPointingHere = new ArrayList<>();
    public ArrayList<Integer> images = new ArrayList<>(); // list of images id's in this slide
    public ArrayList<UserButton> leftButtons = new ArrayList<>(); // list of buttons on left side
    public ArrayList<UserButton> rightButtons = new ArrayList<>(); // list of buttons on right side

    public boolean loadedCorrectly;

    public SlideHandler(String slidePath) {
        // load slide from file
        System.out.println("Creating slide handler, path: " + slidePath);
        path = slidePath;
        InputStream stream;
        ZipFile zipFile;
        try {
            String path = slidePath + "index.xml";
            zipFile = FileManager.getZipFile();
            ZipEntry entry = new ZipEntry(path);
            stream = zipFile.getInputStream(entry);
        } catch (IOException ex) {
            System.out.println("Cant open file " +slidePath+ " from project file " + FileManager.getFileObject());
            Global.showError("Project file error", "Cant open slide "
                    + slidePath + " from project "
                    + FileManager.getFileObject()
                    + "\n File cant be loaded");
            loadedCorrectly = false;
            return;
        }
        try {
            loadData(stream);
        } catch (Exception ex) {
            System.out.println("Cant load slide correctly " +slidePath
                    + " from project file "
                    +FileManager.getFileObject() +
                    ", error: " + ex.toString());
            Global.showError("Project file error", "Cant load slide "
                    + slidePath + " from project "
                    + FileManager.getFileObject()
                    + "\n Slide file is broken, error: \n"
                    + ex.toString());
            loadedCorrectly = false;
            return;
        }
        loadedCorrectly = true;
        try {
            stream.close();
            zipFile.close();
        } catch(Exception ex) {
            System.out.println("Cant close stream! error: " +ex.toString());
        }
    }
    private void loadData(InputStream inputStream) throws Exception {
        System.out.println("Reading slide file");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        System.out.println("XML doc created");
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();

        // get name //
        System.out.println("Getting name");
        XPathExpression expr = xPath.compile("/slide/name[1]/text()");
        this.name = (String) expr.evaluate(doc, XPathConstants.STRING);
        System.out.println("Name: " +name);

        // get info //
        System.out.println("Getting info");
        expr = xPath.compile("/slide/info/item");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        // for each info
        for (int i = 1; i < nodeList.getLength()+1; i++) {
            expr = xPath.compile("/slide/info/item[" +i+ "]/name/text()");
            String infoName = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xPath.compile("/slide/info/item[" +i+ "]/value/text()");
            String infoValue = (String) expr.evaluate(doc, XPathConstants.STRING);
            Info infoObj = new Info(infoName, infoValue);
            this.infos.add(infoObj);
            System.out.println("Info name: " +infoName+ " , value: " +infoValue);
        }

        // get images //
        System.out.println("Getting images");
        expr = xPath.compile("/slide/images[1]/text()");
        String[] images = ((String) expr.evaluate(doc, XPathConstants.STRING)).split(" ");
        for (int i = 0; i < images.length; i++) {
            this.images.add(Integer.parseInt(images[i]));
            System.out.println("Image " +i+ " = " +images[i]);
        }

        // get links pointing here //
        System.out.println("Getting links pointing here");
        expr = xPath.compile("/slide/linksPointingHere[1]/text()");
        String[] linksPointingHere = ((String) expr.evaluate(doc, XPathConstants.STRING)).split(" ");
        for (int i = 0; i < linksPointingHere.length; i++) {
            this.linksPointingHere.add(linksPointingHere[i]);
            System.out.println("Link " +i+ " = " +linksPointingHere[i]);
        }

        // get left buttons //
        System.out.println("Getting left buttons");
        try {
            expr = xPath.compile("/slide/leftButtons/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 1; i < nodeList.getLength() + 1; i++) {
                System.out.println("Getting info for left button " + i);
                leftButtons.add(getButtonInfo(xPath, doc, "/slide/leftButtons/button[" + i + "]", i));
            }
        } catch (Exception ex) {
            System.out.println("Probably no left buttons, error: " +ex.toString());
        }

        // get right buttons //
        System.out.println("Getting right buttons");
        try {
            expr = xPath.compile("/slide/rightButtons/button");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // for each button
            for (int i = 1; i < nodeList.getLength()+1; i++) {
                System.out.println("Getting info for right button " +i);
                rightButtons.add(getButtonInfo(xPath, doc, "/slide/rightButtons/button[" +i+ "]", i));
            }
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
                leftButtonsList.add(getButtonInfo(xPath, doc, pathToButton + "/leftButtons/button[" +i+ "]", i));
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
                rightButtonsList.add(getButtonInfo(xPath, doc, pathToButton + "/rightButtons/button[" + i + "]", i));
            }
        } catch (Exception ex) {
            System.out.println("Probably no right sub buttons, error: " +ex.toString());
        }


        UserButton userButtonObj = new UserButton(title, subTitle, buttonId, type, link, text, leftButtonsList, rightButtonsList);
        System.out.println("Sub button info loaded");
        return userButtonObj;
    }

}
