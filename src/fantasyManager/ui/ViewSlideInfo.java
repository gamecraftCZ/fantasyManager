/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.Global;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewSlideInfo {

    @FXML private Label textArea;

    @FXML void initialize() {
        textArea.setText(Global.slide.info);
        // text changed -> change it in Global.slide.info
    }

    public void close() {
        textArea.getParent().getParent().getParent().setVisible(false);
    }

}
