package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.GlobalVariables;
import fantasyManager.editorHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MenuBar {

    private boolean selectedEditMode = true;
    private boolean selectFileWindowOpened = false;
    @FXML
    private Pane root;



    public void newProject() {
        System.out.println("Creating new project...");
    }
    public void saveroject() {
        System.out.println("Saving project....");
    }
    public void saverojectAs() {
        System.out.println("Saving project as....");
    }
    public void openProject() throws IOException {
        if (selectFileWindowOpened) {
            // System dialog for file open already opened
            System.out.println("System dialog for file open already opened");
            return;
        }
        selectFileWindowOpened =  true;
        Stage stage = new Stage();
        System.out.println("Select file in file choose dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open existing project");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.fmp"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("Opening file: " + file);
            if (FileManager.openProjectFile(file)) {
                System.out.println("File opened");
                GlobalVariables.editor = new editorHandler();
                System.out.println("New editor handler created");
                openNewScene("editing.fxml");
                System.out.println("Scene loaded");
            } else {
                System.out.println("File not opened: " + file);
            }
        } else {
            System.out.println("File was not selected");
        }
        selectFileWindowOpened = false;
    }
    public void switchToView() {
        if (selectedEditMode) {
            // switch to view mode
            System.out.println("Switching to view mode...");
        } else {
            // view mode already selected
            System.out.println("View mode already selected");
        }
    }
    public void switchToEdit() {
        if (!selectedEditMode) {
            // switch to edit mode
            System.out.println("Switching to edit mode...");
        } else {
            // edit mode already selected
            System.out.println("Edit mode already selected");
        }
    }






    private void openNewScene(String scenePath) throws IOException {
        System.out.println("Opening scene: " + scenePath);
        Pane pane = FXMLLoader.load(getClass().getResource(scenePath));
        root.getChildren().addAll(pane);
    }


}



