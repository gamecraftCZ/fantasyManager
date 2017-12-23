package fantasyManager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import jdk.internal.util.xml.impl.Input;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileManager {

    private static File fileObject;
    public static boolean saved = true;

    public static boolean newProjectFile(File file) {
        // create new Project file
        System.out.println("Creating project file: " + file);
        InputStream inStream = null;
        OutputStream outStream = null;
        boolean Return = false;
        try {
            System.out.println("Creating project file");
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
            System.out.println("Project created");
            fileObject = file;
            saved = true;
            Return = true;
        } catch (IOException ex) {
            System.out.println("Cant create project, IOException: " + ex.toString());
        }

        try {
            inStream.close();
            outStream.close();
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
        try {
            InputStream stream = getFile("images/" + imageId + ".png");
            BufferedImage bufferedImage = ImageIO.read(stream);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return image;
        } catch (IOException ex) {
            System.out.println("Cant open file to read from: " + fileObject);
            Global.showError("Chyba přístupu k souboru", "Nelze otevřít soubor projektu! chyba: \n"
                    + ex.toString());
            Image image = null;
            return image;
        }
    }

















    public static InputStream getFile(String path) throws IOException {
        ZipFile zipFile = new ZipFile(fileObject);
        InputStream stream = zipFile.getInputStream(new ZipEntry(path));
        return stream;
    }

}
