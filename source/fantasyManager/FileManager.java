package fantasyManager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

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
