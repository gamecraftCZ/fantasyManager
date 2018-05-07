/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager;

import fantasyManager.ui.MenuBar;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Global {

    public static SlideHandler slide;
    public static UserButton getButtonById(ArrayList<UserButton> buttons, int buttonId) {
        for (UserButton button : buttons) {
            if (button.buttonId == buttonId) {
                return button;
            }
        }
        return new UserButton(buttons.get(buttons.size()).buttonId + 1);
    }
    public static int getButtonPositionInListById(ArrayList<UserButton> buttons, int buttonId) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).buttonId == buttonId) {
                return i;
            }
        }
        return -1;
    }

    public static boolean editMode = true;
    public static ArrayList<String> lastVisitedSlides = new ArrayList<>();
    public static void addToLastVisitedSlides(String path) {
        lastVisitedSlides.add(path);
        if (lastVisitedSlides.size() > 255) {
            lastVisitedSlides.remove(0);
        }
    }
    public static void openNewSlide(String slidePath) {
        // if slide with this path does not exists -> open main slide
        if (!slideWithPathExists(slidePath)) slidePath = "";
        // save current slide path
        String currentSlidePath = "";
        if (slide != null) currentSlidePath = Global.slide.path;
        // open new scene
        if (editMode) {
            if (slide != null) {
                if (FileManager.save()) {
                    slide = new fantasyManager.SlideHandler(slidePath);
                    openNewScene("editor.fxml");
                } else {
                    Global.yesNoDialogForNotSaved("Nelze uložit!",
                            "Chyba při ukládání, chcete přesto přejít na další slide?");
                    if (FileManager.saved) {
                        slide = new fantasyManager.SlideHandler(slidePath);
                        openNewScene("editor.fxml");
                    } else {
                        return;
                    }
                }
            } else {
                slide = new fantasyManager.SlideHandler(slidePath);
                openNewScene("editor.fxml");
            }
        } else {
            slide = new fantasyManager.SlideHandler(slidePath);
            openNewScene("view.fxml");
        }
        // add to last visited slides
        if (!currentSlidePath.isEmpty()) addToLastVisitedSlides(currentSlidePath);
    }
    public static void openNewScene(String scenePath) {
        System.out.println("Opening scene: " + scenePath);
        try {
            Pane pane = FXMLLoader.load(MenuBar.class.getResource(scenePath));
            MenuBar.windowRoot.getChildren().removeAll(MenuBar.windowRoot.getChildren());
            MenuBar.windowRoot.getChildren().addAll(pane);
        } catch (IOException ex) {
            System.out.println("No chance to get there, error: " +ex.toString());
            // No chance to get there until all scenes that are opening are available
        }
    }
    public static boolean slideWithPathExists(String path) {
        for (BasicSlideInfo slide : slidesList) {
            if (slide.path.equals(path))
                return true;
        }
        return false;
    }

    public static ArrayList<BasicSlideInfo> slidesList = new ArrayList<>();
    public static int getSlidePositionInSlidesListByPath(String path) {
        int pos = -1;
        for (int i = 0; i < slidesList.size(); i++) {
            if (slidesList.get(i).path.equals(path)) {
                pos = i;
            }
        }
        return pos;
    }
    public static BasicSlideInfo getSlideInSlidesListByPath(String path) {
        int slidePos = getSlidePositionInSlidesListByPath(path);
        if (slidePos == -1) {
            return null;
        } else {
            return slidesList.get(slidePos);
        }
    }

    public static UserButton buttonEditor_button;

    public static String whatToAdd;

    public static String selectSlide_Prompt;
    public static Pane selectSlide_Pane;
    public static String selectSlide_Selected;
    public static boolean selectSlide_SearchAlsoForCurrentSlide;

    public static void showError(String title, String text) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());
        closeButton.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
            {
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }
    public static void yesNoDialogForNotSaved(String title, String text) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            window.close();
            FileManager.saved = false;
        });
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            window.close();
            FileManager.saved = true;
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, noButton, yesButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }
    private static boolean dialogOutput;
    public static boolean areYouSureDialog(String title, String text) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            window.close();
            dialogOutput = false;
        });
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            window.close();
            dialogOutput = true;
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, noButton, yesButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        dialogOutput = false;
        window.showAndWait();
        return dialogOutput;
    }

    public static File lastFolderImageGotFrom;

}
