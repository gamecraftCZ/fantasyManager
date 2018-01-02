package fantasyManager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileManager {

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
//            try {
//                file.delete();
//            } catch (Exception ex) {
//
//            }



//            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(file);
//            ZipParameters parameters = new ZipParameters();
//            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
//            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
//            File folder = new File(FileManager.class.getResource("/projectTemplate").toString().substring(6));
//            System.out.println("Template folder is: " +folder);
//            File[] items = folder.listFiles();
//            for (File f : items) {
//                System.out.println("Adding file: " +f.getName());
//                if (f.isFile()) {
//                    zipFile.addFile(f, parameters);
//                } else {
//                    zipFile.addFolder(f, parameters);
//                }
//            }
//
//
            file.createNewFile();
            System.out.println("Creating input stream");
            inStream = FileManager.class
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
            inStream.close();
            outStream.close();
        } catch(Exception ex) {
            System.out.println("Cant close stream! error: " +ex.toString());
        }

        return Return;

    }

    public static boolean openProjectFile(File file) {
        //open project file into zipFile(handler)
        System.out.println("Opening project file: " + file);
        return true;
    }

    public static boolean save() {
        System.out.println("Saving project...");
        return true;
    }
    public static boolean saveAs(File file) {
        System.out.println("Saving project...");
        return true;
    }

    public static boolean doYouWantToSave() {
        System.out.println("Do you want to save this project?");
        return true;
    }

    public static Image getImage(int imageId) {
        System.out.println("Getting image file " +imageId);
        InputStream stream = null;
        Image returnImage;
        ZipFile zipFile = null;
        try {
            String path = "images/" + imageId + ".png";
            zipFile = FileManager.getZipFile();
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
            stream.close();
            zipFile.close();
        } catch(Exception ex) {
            System.out.println("Cant close stream! error: " +ex.toString());
        }
        return returnImage;
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



    public static int addImageToZip(BufferedImage image) {
        // get image id
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
            Node imageSequence = ((Node) expr.evaluate(doc, XPathConstants.NODE)).getAttributes().getNamedItem("sequence");
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
//            // export image to temp file
//            File tempImageFile = new File(Global.tempFolder.toString()+"/"+newImageId+".png");
//            System.out.println("Temp file: " +tempImageFile);
//            ImageIO.write(image, "png", tempImageFile);
//            System.out.println("Image saved to temp file.");

            // save image
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

    public static ZipFile getZipFile() throws IOException {
        System.out.println("Loading zip file");
        ZipFile zipFile = new ZipFile(fileObject);
        return zipFile;
    }

}
