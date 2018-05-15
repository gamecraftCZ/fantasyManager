package fantasyManager.fileManager;

import fantasyManager.BasicSlideInfo;
import fantasyManager.Global;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RestoreInfoXmlTestingClassDoNotUse {

    public static void main(String[] args) throws Exception {
        generateBasicSlideInfo();
        saveBasicSlideInfo();
    }
    private static void generateBasicSlideInfo() throws Exception {
        String slideTypes[] = { "characters", "organisations", "places", "other" };
        for (String type : slideTypes) {
            File folder = new File(type);
            for (final File fileEntry : folder.listFiles()) {
                Global.slidesList.add(fileToBasicSlideInfo(fileEntry, type));
            }
        }
    }
    private static BasicSlideInfo fileToBasicSlideInfo(File file, String slideType) throws Exception {
        String fileName = file.getName();
        Document doc = getFileAsDocument(slideType + File.separator + fileName);
        fileName = fileName.substring(0, fileName.length() - 4);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();

        // get name //
        System.out.println("Getting name");
        XPathExpression expr = xPath.compile("/slide/name[1]/text()");
        String name = (String) expr.evaluate(doc, XPathConstants.STRING);
        System.out.println("Name: " + name);

        // extract path //
        String path = file.getParent() + File.separator + file.getName();

        // get slide id
//        int idSubstringStartPosition = slideType.length() + 1;
//        String idAsString = fileName.substring(idSubstringStartPosition);
        int id = Integer.parseInt(fileName);

        return new BasicSlideInfo(name, path, slideType, id);
    }
    private static void saveBasicSlideInfo() throws Exception {
        // save basic slides info to file // // //
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        // load basic slides info // //
        System.out.println("Loading info.xml");
        Document infoDoc = getFileAsDocument("info.xml");
        // edit document //
        System.out.println("modifying info.xml doc");
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();
        XPathExpression expr;

        // get BasicSlideInfo
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
        new File(("info.xml")).delete();
        OutputStream infoOutput = new FileOutputStream("info.xml");
        DOMSource infoSource = new DOMSource(infoDoc);
        StreamResult infoResult = new StreamResult(infoOutput);
        transformer.transform(infoSource, infoResult);
        infoOutput.close();

        System.out.println("info saved to file");
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

    public static Document getFileAsDocument(String path) throws Exception {
        System.out.println("Getting xml file as doc: " + path);

        InputStream inputStream = new FileInputStream(path);

        Document doc = inputStreamToDocument(inputStream);
        inputStream.close();
        return doc;
    }
    private static Document inputStreamToDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            System.out.println("Cant convert input stream to xml, error: " + e);
            return null;
        }
    }


    private static void printXML(Document xml) {
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
