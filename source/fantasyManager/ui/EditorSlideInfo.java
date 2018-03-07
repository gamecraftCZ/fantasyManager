/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.FileManager;
import fantasyManager.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class EditorSlideInfo {

    @FXML private TextArea textArea;

    @FXML void initialize() {
        textArea.setText(Global.slide.info);
        // text changed -> change it in Global.slide.info
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
//                System.out.println("New info text: " + newValue);
                Global.slide.info = newValue;
                FileManager.saved = false;
            }
        });
    }

    public void close() {
        textArea.getParent().getParent().getParent().setVisible(false);
    }

}
