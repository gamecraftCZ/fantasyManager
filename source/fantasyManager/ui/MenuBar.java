package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MenuBar {

    public static Pane windowRoot;
    public static Pane popOutMenuRoot;

    private boolean selectedEditMode = true;
    private boolean selectFileWindowOpened = false;
    @FXML private Pane root;
    @FXML private MenuItem addCharacter;
    @FXML private MenuItem addPlace;
    @FXML private MenuItem addOrganisation;
    @FXML private MenuItem addOther;
    @FXML private MenuItem goToButton;
    @FXML private Pane popOutMenu;

    @FXML public void initialize() {
        // testing - delete \/
        FileManager.newProjectFile(new File("c:/_temp/test.fmp"));
        System.out.println("File created");
        Global.slide = new fantasyManager.SlideHandler("");
        System.out.println("New slide handler created");
        openNewScene("editing.fxml");
        System.out.println("Scene loaded");
        projectLoadedDisableMenuButtons(false);
        // testing - delete /\
        System.out.println("Menu bar initialization");
        windowRoot = root;
        popOutMenuRoot = popOutMenu;
    }

    public void newProject() {
        System.out.println("Creating new project...");
        if (!FileManager.saved) {
            FileManager.save();
        }
        if (selectFileWindowOpened) {
            // System dialog for file open already opened
            System.out.println("System dialog for file save already opened");
            return;
        }
        selectFileWindowOpened =  true;
        Stage stage = new Stage();
        System.out.println("New file in file choose dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vytvořit nový projekt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.fmp"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            System.out.println("creating file: " + file);
            if (FileManager.newProjectFile(file)) {
                // create project
                System.out.println("File created");
                Global.slide = new fantasyManager.SlideHandler("");
                System.out.println("New slide handler created");
                openNewScene("editing.fxml");
                System.out.println("Scene loaded");
                projectLoadedDisableMenuButtons(false);
                // project created
            } else {
                System.out.println("File not created: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        selectFileWindowOpened = false;
    }
    public void saveProject() {
        System.out.println("Saving project....");
        if (FileManager.saved) {
            System.out.println("Project already saved");
            return;
        }
        FileManager.save();
    }
    public void saveProjectAs() {
        System.out.println("Saving project as....");
        if (selectFileWindowOpened) {
            // System dialog for file open already opened
            System.out.println("System dialog for file save already opened");
            return;
        }
        selectFileWindowOpened =  true;
        Stage stage = new Stage();
        System.out.println("save file as in file choose dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Uložit projekt jako");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.fmp"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            System.out.println("creating file: " + file);
            if (FileManager.saveAs(file)) {
                // save as
                System.out.println("Project saved as");
            } else {
                System.out.println("Cant save as: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        selectFileWindowOpened = false;
    }
    public void openProject() {
        if (!FileManager.saved) {
            FileManager.save();
        }
        if (selectFileWindowOpened) {
            // System dialog for file open already opened
            System.out.println("System dialog for file open already opened");
            return;
        }
        selectFileWindowOpened =  true;
        Stage stage = new Stage();
        System.out.println("Select file in file choose dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otevření existujícího projektu");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.fmp"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("Opening file: " + file);
            if (FileManager.openProjectFile(file)) {
                // load project
                System.out.println("File opened");
                Global.slide = new fantasyManager.SlideHandler("");
                System.out.println("New slide handler created");
                openNewScene("editing.fxml");
                System.out.println("Scene loaded");
                // project loaded
            } else {
                System.out.println("File not opened: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        FileManager.saved = true;
        selectFileWindowOpened = false;
    }

    public void switchToView() {
        if (selectedEditMode) {
            // switch to view mode
            System.out.println("Switching to view mode...");
            FileManager.save();
            openNewScene("view.fxml");
            projectLoadedDisableMenuButtons(true);
        } else {
            // view mode already selected
            System.out.println("View mode already selected");
        }
    }
    public void switchToEdit() {
        if (!selectedEditMode) {
            // switch to edit mode
            System.out.println("Switching to edit mode...");
            openNewScene("editing.fxml");
            projectLoadedDisableMenuButtons(false);
        } else {
            // edit mode already selected
            System.out.println("Edit mode already selected");
        }
    }

    public void undo() {
        System.out.println("Undo pressed");
    } // not done
    public void redo() {
        System.out.println("Redo pressed");
    } // not done

    public void addCharacter() {
        System.out.println("Adding character");
        Global.whatToAdd = "Postava";
        openPopOutMenu("add.fxml");
    }
    public void addPlace() {
        System.out.println("Adding place");
        Global.whatToAdd = "Místo";
        openPopOutMenu("add.fxml");

    }
    public void addOrganisation() {
        System.out.println("Adding organisation");
        Global.whatToAdd = "Organizace";
        openPopOutMenu("add.fxml");
    }
    public void addOther() {
        System.out.println("Adding other");
        Global.whatToAdd = "Ostatní";
        openPopOutMenu("add.fxml");
    }

    public void openGoTo() {
        System.out.println("Opening go to menu");
    }




    private void projectLoadedDisableMenuButtons(boolean disable) {
        System.out.println("Allowing menu buttons on open project: " + disable);
        addCharacter.setDisable(disable);
        addPlace.setDisable(disable);
        addOrganisation.setDisable(disable);
        addOther.setDisable(disable);
        goToButton.setDisable(disable);
    }

    private void openNewScene(String scenePath) {
        System.out.println("Opening scene: " + scenePath);
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
            root.getChildren().removeAll(root.getChildren());
            root.getChildren().addAll(pane);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }
    private void openPopOutMenu(String menuPath) {
        System.out.println("Opening pop out menu: " + menuPath);
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(menuPath));
            popOutMenu.getChildren().removeAll(popOutMenu.getChildren());
            popOutMenu.getChildren().addAll(pane);
            popOutMenu.setVisible(true);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all opened scenes are available
        }
    }

}



