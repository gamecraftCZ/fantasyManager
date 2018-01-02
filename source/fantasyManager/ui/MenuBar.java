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

    private boolean selectedEditMode = true;
    private boolean selectFileWindowOpened = false;
    @FXML private Pane root;
    @FXML private MenuItem addCharacter;
    @FXML private MenuItem addPlace;
    @FXML private MenuItem addSubPlace;
    @FXML private MenuItem addOrganisation;
    @FXML private MenuItem addSubOrganisation;
    @FXML private MenuItem goToButton;
    @FXML private Pane add;
//    @FXML public void initialize() {
//        System.out.println("Menu bar initialization");
//    }

    public void newProject() {
        System.out.println("Creating new project...");
        if (!FileManager.doYouWantToSave()) {
            System.out.println("Saving project canceled or not successful");
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
                projectLoadedAllowMenuButtons(true);
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
        if (!FileManager.doYouWantToSave()) {
            System.out.println("Saving project canceled or not successful");
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
            projectLoadedAllowMenuButtons(false);
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
            projectLoadedAllowMenuButtons(false);
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
    }
    public void addPlace() {
        System.out.println("Adding place");
    }
    public void addSubPlace() {
        System.out.println("Adding sub place");
    }
    public void addOrganisation() {
        System.out.println("Adding organisation");
    }
    public void addSubOrganisation() {
        System.out.println("Adding sub organisation");
    }

    public void openGoTo() {
        System.out.println("Opening go to menu");
    }




    private void projectLoadedAllowMenuButtons(boolean allow) {
        System.out.println("Allowing menu buttons on open project: " + allow);
        addCharacter.setDisable(allow);
        addPlace.setDisable(allow);
        addSubPlace.setDisable(allow);
        addOrganisation.setDisable(allow);
        addSubOrganisation.setDisable(allow);
        goToButton.setDisable(allow);
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

}



