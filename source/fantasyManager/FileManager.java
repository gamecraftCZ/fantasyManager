package fantasyManager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.tools.FileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
                    ├---organisations -┬---0 ----------┬---index.xml
                    |                  ├---1 ---...    ├---0 ---...
                    |                  └---...         └---...
                    |
                    ├---other ---------┬---0.xml
                    |                  ├---1.xml
                    |                  └---...
                    |
                    └---places --------┬---0 ----------┬---index.xml
                                       ├---1 ---...    ├---0 ---...
                                       └---...         └---...

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
            outStream.close();
            inStream.close();
        } catch (IOException ex) {
            System.out.println("Cant close stream for new project file, IOException: " + ex.toString());
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
        try {
            InputStream stream = getFileFromZip("images/" + imageId + ".png");
            System.out.println("Got image file");
            BufferedImage bufferedImage = ImageIO.read(stream);
            System.out.println("Image loaded as buffered image");
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            System.out.println("Image loaded from buffered image");
            return image;
        } catch (Exception ex) {
            System.out.println("Cant open file to read from: " + fileObject);
            Global.showError("Chyba přístupu k souboru", "Nelze otevřít soubor projektu! chyba: \n"
                    + ex.toString());
            return null;
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



    public static int addImageToZip(BufferedImage image) {
        // get image id
        int newImageId;
        try {
            // get new image id //
            // create xml parser
            InputStream xmlStream = getFileFromZip("info.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            System.out.println("XML doc created");
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            // get image sequence
            System.out.println("Getting name");
            XPathExpression expr = xPath.compile("/project/images[1]/@sequence");
            newImageId = Integer.parseInt((String) expr.evaluate(doc, XPathConstants.STRING));
            System.out.println("New image id: " +newImageId);

            // add image to file //
            System.out.println("Adding image to project file");
            // export image to temp file
            File tempImageFile = new File(Global.tempFolder.toString()+"/"+newImageId+".png");
            System.out.println("Temp file: " +tempImageFile);
            ImageIO.write(image, "png", tempImageFile);
            System.out.println("Image saved to temp file.");
            // save image
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(fileObject);
            ZipParameters parameters = new ZipParameters();
            parameters.setRootFolderInZip("images/");
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipFile.addFile(tempImageFile, parameters);
            // delete temp file //and close zipFile
            tempImageFile.delete();


            return newImageId;
        } catch (Exception ex) {
            System.out.println("Cant add image to zip, error: " +ex.getMessage());
            Global.showError("Can't add image!","Image cant be saved to project file, \nerror: "
                    +ex.toString());
            return -1;
        }

    }

    public static InputStream getFileFromZip(String path) throws IOException {
        System.out.println("Loading zip entry: " +path + " , from zip file: " +fileObject);
        ZipFile zipFile = new ZipFile(fileObject);
        ZipEntry entry = new ZipEntry(path);
        InputStream stream = zipFile.getInputStream(entry);
        if (zipFile == null) {
            System.out.println("File not loaded correctly!");
            throw new IOException("File does not exists in this zip file");
        }
        return stream;
    }

}
