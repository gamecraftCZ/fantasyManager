/*
 * 2018 Patrik Vácal.
 * This file is under GNU-GPL license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.fileManager;

import fantasyManager.BasicSlideInfo;
import fantasyManager.Global;
import fantasyManager.Main;
import fantasyManager.SlideHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Saves {

    private static final int autosaveInterval = 900; // 15 minutes

    /*
    project.fmp ----┬---index.xml
                    ├---info.xml
                    ├---characters ----┬---0.xml
                    |                  ├---1.xml
                    |                  └---...
                    |
                    ├---images --------┬---0.png
                    |                  ├---1.png
                    |                  └---...
                    |
                    ├---organisations -┬---0.xml
                    |                  ├---1.xml
                    |                  └---...
                    |
                    ├---other ---------┬---0.xml
                    |                  ├---1.xml
                    |                  └---...
                    |
                    └---places --------┬---0.xml
                                       ├---1.xml
                                       └---...
     */

    private static File fileObject;
    public static boolean saved = true;

    public static File getFileObject() {
        return fileObject;
    }

    public static boolean newProjectFile(File file) {
        // create new Project file
        System.out.println("Creating project file: " + file);
        InputStream inStream = null;
        OutputStream outStream = null;
        boolean Return = false;
        try {
            System.out.println("Creating input stream");
            inStream = Saves.class
                    .getResourceAsStream("/resources/projectTemplate.fmp");
            System.out.println("Creating output stream");
            outStream = new FileOutputStream(file);
            System.out.println("Copying project from template to file");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            fileObject = file;
            saved = true;
            Return = true;
            System.out.println("Project created");
        } catch (Exception ex) {
            System.out.println("Cant create project, Exception: " + ex.toString());
            Global.showError("Projekt nelze vytvoořit!", "Chyba při tvorbě projektu, error: \n" + ex.toString());
        }
        try {
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
        } catch(Exception ex) {
            System.out.println("Cant close stream! error: " +ex.toString());
        }

        Global.slidesList.clear();
        return Return;

    }

    public static boolean openProjectFile(File file) {
        //open project file from zipFile(handler)
        System.out.println("Opening project file: " + file);
        Global.slidesList.clear();
        fileObject = file;
        return loadSlidesBasicInfo();
    }
    private static boolean loadSlidesBasicInfo() {
        System.out.println("Loading slides basic info");
        Document doc = null;
        try {
            // get info.xml as doc
            doc = FileLoader.getFileAsDocument("info.xml");
            // create xPath factory
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();
            XPathExpression expr;
            Node sequence;
            // add characters
            expr = xPath.compile("/project/characters[1]");
            sequence = (Node) expr.evaluate(doc, XPathConstants.NODE);
            addBasicSlideInfoListFromListNode(sequence);
            // add organisations
            expr = xPath.compile("/project/organisations[1]");
            sequence = (Node) expr.evaluate(doc, XPathConstants.NODE);
            addBasicSlideInfoListFromListNode(sequence);
            // add places
            expr = xPath.compile("/project/places[1]");
            sequence = (Node) expr.evaluate(doc, XPathConstants.NODE);
            addBasicSlideInfoListFromListNode(sequence);
            // add other
            expr = xPath.compile("/project/other[1]");
            sequence = (Node) expr.evaluate(doc, XPathConstants.NODE);
            addBasicSlideInfoListFromListNode(sequence);
            return true;
        } catch (Exception e) {
            System.out.println("Can't load basic slides info, error: " + e.toString());
            if (doc != null) {
                System.out.println("info.xml contains:");
                printXML(doc, true);
            }
            Global.showError("Nelze načíst projekt!", "Chyba při načítání základních informacích o slidech.");
            return false;
        }
    }
    private static void addBasicSlideInfoListFromListNode(Node node) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
             addBasicSlideInfoFromNode(node.getChildNodes().item(i));
        }
    }
    private static void addBasicSlideInfoFromNode(Node node) {
        BasicSlideInfo info = new BasicSlideInfo();

        // get name
        Node nameNode = node.getChildNodes().item(0);
        if (nameNode != null) {
            info.name = nameNode.getTextContent();
        } else {
            info.name = "Název je pravděpodobně poškozen!";
        }

        String idString = node.getAttributes().getNamedItem("id").getNodeValue();
        info.id = Integer.parseInt(idString);

        info.path = node.getChildNodes().item(1).getTextContent();
        info.type = node.getParentNode().getNodeName();

        // get slidesPointingHere
        Node slidesPointingHereNode = node.getChildNodes().item(2);
        if (slidesPointingHereNode != null) {
            List<String> slidePointingHere = Arrays.asList(slidesPointingHereNode.getTextContent().split(" "));
            info.slidesPointingHere.addAll(slidePointingHere);
        }

        Global.slidesList.add(info);
    }

    public static boolean save() {
        if (fileObject == null) {
            System.out.println("No loaded project -> can't be saved");
            return true;
        } else if (saved) {
            System.out.println("File is already saved");
            return true;
        } else {
            try {
                System.out.println("Saving project...");
                String slidePath = Global.slide.path;
                Map<String, String> env = new HashMap<>();
                env.put("create", "true");
                URI uri = URI.create("jar:" + fileObject.toURI());
                System.out.println("Zip file path: " + uri);
                try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
                    // add slide to file //
                    System.out.println("Saving slide");
                    Path pathInZipfile = fs.getPath(slidePath);
                    Files.delete(pathInZipfile);
                    OutputStream slideOutputStream = Files.newOutputStream(pathInZipfile);
                    Document slideDoc = Global.slide.toDocument();
                    System.out.println("Current slide xml:");
                    printXML(slideDoc);
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(slideDoc);
                    StreamResult result = new StreamResult(slideOutputStream);
                    transformer.transform(source, result);
                    slideOutputStream.close();
                    System.out.println("Slide saved to zip file");

                    // save basic slides info to file // // //
                    // load basic slides info // //
                    System.out.println("Loading info.xml");
                    InputStream infoInput = Files.newInputStream(fs.getPath("info.xml"));
                    // create xml parser //
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document infoDoc = builder.parse(infoInput);
                    // close stream //
                    infoInput.close();
                    // edit document //
                    System.out.println("modifying info.xml doc");
                    XPathFactory xpathFactory = XPathFactory.newInstance();
                    XPath xPath = xpathFactory.newXPath();
                    XPathExpression expr;


                    // edit current BasicSlideInfo \\
                    System.out.println("Modifying current slide BasicSlideInfo");
                    if (!(slidePath.equals("") || slidePath.equals("/")
                            || slidePath.equals("index.xml") || slidePath.equals("/index.xml"))) {
                        // edit current slide BasicSlideInfo
                        int slidePos = Global.getSlidePositionInSlidesListByPath(slidePath);
                        Global.slidesList.get(slidePos).name = Global.slide.name;
                    }

                    // get BasicSlideInfos
                    ArrayList<BasicSlideInfo> basicCharactersInfo = new ArrayList<>();
                    ArrayList<BasicSlideInfo> basicOrganisationsInfo = new ArrayList<>();
                    ArrayList<BasicSlideInfo> basicPlacesInfo = new ArrayList<>();
                    ArrayList<BasicSlideInfo> basicOtherInfo = new ArrayList<>();
                    // for each basicSlideInfo
                    System.out.println("Getting all basic slides info from list");
                    for (BasicSlideInfo slide : Global.slidesList) {
                        switch (slide.type) {
                            case "characters":
                                basicCharactersInfo.add(slide);
                                break;
                            case "places":
                                basicOrganisationsInfo.add(slide);
                                break;
                            case "organisations":
                                basicPlacesInfo.add(slide);
                                break;
                            default:   // other
                                basicOtherInfo.add(slide);
                        }
                    }
                    // get nodes from original doc
                    System.out.println("Getting nodes from info.xml doc");
                    expr = xPath.compile("/project/characters[1]");
                    Node charactersInfoNode = (Node) expr.evaluate(infoDoc, XPathConstants.NODE);
                    expr = xPath.compile("/project/organisations[1]");
                    Node OrganisationsInfoNode = (Node) expr.evaluate(infoDoc, XPathConstants.NODE);
                    expr = xPath.compile("/project/places[1]");
                    Node PlacesInfoNode = (Node) expr.evaluate(infoDoc, XPathConstants.NODE);
                    expr = xPath.compile("/project/other[1]");
                    Node OtherInfoNode = (Node) expr.evaluate(infoDoc, XPathConstants.NODE);
                    // remove old info from nodes
                    System.out.println("Removing old info from nodes");
                    removeAllChildrenOfNode(charactersInfoNode);
                    removeAllChildrenOfNode(OrganisationsInfoNode);
                    removeAllChildrenOfNode(PlacesInfoNode);
                    removeAllChildrenOfNode(OtherInfoNode);
                    // add new info to nodes
                    System.out.println("Adding new info to nodes");
                    addBasicSlideInfoToNode(charactersInfoNode, basicCharactersInfo, infoDoc);
                    addBasicSlideInfoToNode(OrganisationsInfoNode, basicOrganisationsInfo, infoDoc);
                    addBasicSlideInfoToNode(PlacesInfoNode, basicPlacesInfo, infoDoc);
                    addBasicSlideInfoToNode(OtherInfoNode, basicOtherInfo, infoDoc);

                    System.out.println("new info.xml fil content: ");
                    printXML(infoDoc);

                    // save modified slides info // //
                    System.out.println("Saving info.xml");
                    Files.delete(fs.getPath("info.xml"));
                    OutputStream infoOutput = Files.newOutputStream(fs.getPath("info.xml"));
                    DOMSource infoSource = new DOMSource(infoDoc);
                    StreamResult infoResult = new StreamResult(infoOutput);
                    transformer.transform(infoSource, infoResult);
                    infoOutput.close();

                    System.out.println("info saved to file");

                    saved = true;
                    return true;
                } catch (Exception ex) {
                    System.out.println("Saving error: " + ex.toString());
                    Global.showError("Chyba ukládání", "Nelze uložit projekt, chyba: \n" + ex.toString());
                    return false;
                }
            } catch (Exception ex) {
                System.out.println("Error when saving: " + ex.toString());
                return false;
            }
        }
    }
    private static void removeAllChildrenOfNode(Node node) {
        int charactersOldLength = node.getChildNodes().getLength();
        for (int i = 0; i < charactersOldLength; i++) {
            node.removeChild(node.getFirstChild());
        }
    }
    private static void addBasicSlideInfoToNode(Node node, ArrayList<BasicSlideInfo> basicSlideInfo, Document doc){
        for (BasicSlideInfo info : basicSlideInfo) {
            node.appendChild(info.getAsElement(doc));
        }
    }

    public static boolean saveAs(File file) {
        System.out.println("Saving project as: " + file.toString());
        // copy current project file to new location
        try {
            copyFile(fileObject, file);
        } catch (Exception e) {
            System.out.println("Cant save as, error: " + e.toString());
            Global.showError("Nelze uložit soubor jako...", "Soubor nelze uložit na nové umístění," +
                    " budu stále pracovat v momentálním souboru");
        }
        fileObject = file;
        // save project
        return save();
    }
    private static void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public static String addSlide(String slideType, String name, String upSlide) {
        try {
            // get newSlideId //
            // get input stream
            String path = "info.xml";
            ZipFile zipFile = getZipFile();
            ZipEntry entry = new ZipEntry(path);
            InputStream xmlStream = zipFile.getInputStream(entry);

            // create xml parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            System.out.println("XML doc created");
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            // get slide sequence
            System.out.println("Getting slide sequence");
            XPathExpression expr = xPath.compile("/project/" + slideType + "[1]");
            Node slideSequence = (Node) expr.evaluate(doc, XPathConstants.NODE);
            XPathExpression sequenceExpr = xPath.compile("/project/" + slideType + "[1]/@sequence");
            int newSlideId = Integer.parseInt((String) sequenceExpr.evaluate(doc, XPathConstants.STRING));
            System.out.println("New slide id: " + newSlideId);
            try {
                xmlStream.close();
                zipFile.close();
            } catch (Exception ex) {
                System.out.println("Cant close stream or zipFile! error: " + ex.toString());
            }


            // add slide to file //
            System.out.println("Adding slide to project file");
            String slidePath = slideType + "/" + newSlideId + ".xml";
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            URI uri = URI.create("jar:" + fileObject.toURI());
            System.out.println("Zip file path: " + uri);
            try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
                // add slide to file
                Path pathInZipFile = fs.getPath(slidePath);
                OutputStream slideOutputStream = Files.newOutputStream(pathInZipFile);
                SlideHandler slide = new SlideHandler(name, upSlide);
                Document slideDoc = slide.toDocument();
                printXML(slideDoc);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(slideDoc);
                StreamResult result = new StreamResult(slideOutputStream);
                transformer.transform(source, result);
                slideOutputStream.close();
                System.out.println("Slide saved to zip file");

                // add slide to info.xml
                slideSequence.appendChild(getElementForNewItemXml(newSlideId, name, slidePath, doc));
                // add 1 to slide sequence
                slideSequence.getAttributes().getNamedItem("sequence").setNodeValue("" + (newSlideId + 1));
                // write the content into xml file

                Files.delete(fs.getPath("info.xml"));
                OutputStream infoFileStream = Files.newOutputStream(fs.getPath("info.xml"));
                transformer = TransformerFactory.newInstance().newTransformer();
                source = new DOMSource(doc);
                result = new StreamResult(infoFileStream);
                transformer.transform(source, result);
                infoFileStream.close();
                System.out.println("Image sequence in info.xml was updated");
                Global.slidesList.add(new BasicSlideInfo(name, slidePath, slideType, newSlideId));
            }
            return slidePath;
        } catch (Exception ex) {
            System.out.println("Cant add new slide file, error: " + ex.toString());
            Global.showError("Can't add slide!", "Slice cant be added to project file, \nerror: "
                    + ex.toString());
            return "";
        }
    }
    private static Node getElementForNewItemXml(int newSlideId, String name, String path, Document doc) {
        Element item = doc.createElement("item");
        item.setAttribute("id", Integer.toString(newSlideId));

        Element nameElement = doc.createElement("jmeno");
        nameElement.insertBefore(doc.createTextNode(name), nameElement.getLastChild());
        item.appendChild(nameElement);

        Element pathElement = doc.createElement("cesta");
        pathElement.insertBefore(doc.createTextNode(path), pathElement.getLastChild());
        item.appendChild(pathElement);

        return item;
    }

    public static void deleteFile(String path) {
        System.out.println("Deleting file from project: " + path);

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create("jar:" + fileObject.toURI());
        System.out.println("Zip file path: " + uri);
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path filePath = fs.getPath(path);
            if (Files.exists(filePath)) Files.delete(filePath);
            else System.out.println("File " + path + " doesnt exists in project file");
        } catch (Exception e) {
            System.out.println("Cant delete file from project, error: " + e.toString());
        }
    }

    public static int addImage(File imageFile) {
        int imageId;
        try {
            BufferedImage image = ImageIO.read(imageFile);
            imageId = addImageToZip(image);
            return imageId;
        } catch (Exception ex) {
            System.out.println("Cant load image, error: " +ex.getMessage());
            Global.showError("Can't load image!","Image cant be loaded, \nerror: " +ex.toString());
            return -1;
        }
    }
    private static int addImageToZip(BufferedImage image) {
        int newImageId;
        try {
            // get new image id //
            // get input stream
            String path = "info.xml";
            ZipFile zipFile = getZipFile();
            ZipEntry entry = new ZipEntry(path);
            InputStream xmlStream = zipFile.getInputStream(entry);

            // create xml parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            System.out.println("XML doc created");
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            // get image sequence
            System.out.println("Getting name");
            XPathExpression expr = xPath.compile("/project/images[1]");
            Node imageSequence = ((Node) expr.evaluate(doc, XPathConstants.NODE)).getAttributes()
                    .getNamedItem("sequence");
            expr = xPath.compile("/project/images[1]/@sequence");
            newImageId = Integer.parseInt((String) expr.evaluate(doc, XPathConstants.STRING));
            System.out.println("New image id: " +newImageId);
            try {
                xmlStream.close();
                zipFile.close();
            } catch(Exception ex) {
                System.out.println("Cant close stream or zipFile! error: " +ex.toString());
            }

            // add image to file //
            System.out.println("Adding image to project file");
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            URI uri = URI.create("jar:" + fileObject.toURI());
            System.out.println("Zip file path: " +uri);
            try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
                // add image to file
                Path pathInZipfile = fs.getPath("images/" +newImageId+ ".png");
                OutputStream imageOutputStream = Files.newOutputStream(pathInZipfile);
                ImageIO.write(image, "png", imageOutputStream);
                imageOutputStream.close();
                System.out.println("Image saved to zip file");
                // add 1 to images sequence
                imageSequence.setNodeValue(""+(newImageId+1));
                // write the content into xml file
                Files.delete(fs.getPath("info.xml"));
                OutputStream infoFileStream = Files.newOutputStream(fs.getPath("info.xml"));
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(infoFileStream);
                transformer.transform(source, result);
                infoFileStream.close();
                System.out.println("Image sequence in info.xml was updated");
            }

            return newImageId;
        } catch (Exception ex) {
            System.out.println("Cant add image to zip, error: " +ex.toString());
            Global.showError("Can't add image!","Image cant be saved to project file, \nerror: "
                    +ex.toString());
            return -1;
        }

    }

    public static Image getImage(int imageId) {
        System.out.println("Getting image file: " +imageId);
        InputStream stream = null;
        Image returnImage;
        ZipFile zipFile = null;
        try {
            String path = "images/" + imageId + ".png";
            zipFile = Saves.getZipFile();
            ZipEntry entry = new ZipEntry(path);
            stream = zipFile.getInputStream(entry);
            System.out.println("Got image file");
            BufferedImage bufferedImage = ImageIO.read(stream);
            System.out.println("Image loaded as buffered image");
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            System.out.println("Image loaded from buffered image");
            returnImage = image;
        } catch (Exception ex) {
            System.out.println("Cant open file to read from: " + fileObject);
            Global.showError("Chyba přístupu k souboru", "Nelze otevřít soubor projektu! chyba: \n"
                    + ex.toString());
            returnImage = null;
        }
        try {
            if (zipFile != null) zipFile.close();
            if (stream != null) stream.close();
        } catch(Exception ex) {
            System.out.println("Cant close stream! error: " +ex.toString());
        }
        return returnImage;
    }
    public static boolean deleteImage(int imageId) {
        try {
            // delete image file //
            System.out.println("Deleting image from project file");
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            URI uri = URI.create("jar:" + fileObject.toURI());
            System.out.println("Zip file path: " + uri);
            try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
                // delete image file
                Path pathInZipfile = fs.getPath("images/" + imageId + ".png");
                if (Files.exists(pathInZipfile)) {
                    Files.delete(pathInZipfile);
                }
                System.out.println("Image deleted form zip file");
            }
            saved = false;
            return true;
        } catch(Exception ex) {
            System.out.println("Cant delete image form zip, error: " + ex.toString());
            Global.showError("Chyba odebírání obrázku", "Nelze odstravnit obrázek ze souboru projektu, " +
                    "chyba: " + ex.toString());
            return false;
        }
    }

    private static ZipFile getZipFile() throws IOException {
        System.out.println("Loading zip file");
        return new ZipFile(fileObject);
    }






    // DEBUG - prints document as XML \\
    private static void printXML(Document xml) {
        printXML(xml, Main.DEBUGGING);
    }
    private static void printXML(Document xml, boolean print) {
        if (print) {
            try {
                Transformer tf = TransformerFactory.newInstance().newTransformer();
                tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                Writer out = new StringWriter();
                tf.transform(new DOMSource(xml), new StreamResult(out));
                System.out.println(out.toString());
            } catch (Exception e) {
                System.out.println("info.xml cant be printed! Error: " + e.toString());
            }
        }
    }

}
