package fantasyManager;

import java.io.File;
import java.util.zip.ZipFile;

public class FileManager {

    private static File fileObject;
    private static ZipFile zipFileObject;
    public static boolean saved = true;

    public static boolean openProjectFile(File file) {
        //open project file into zipFile(handler)
        System.out.println("Opening project file: " + file);
        return true;
    }

    public static boolean newProjectFile(File file) {
        // create new Project file
        System.out.println("Creating project file: " + file);
        saved = true;
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

}
