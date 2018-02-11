package fantasyManager.ui;

import fantasyManager.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class EditingSlideInfo {

    @FXML TextArea textArea;

    @FXML void initialize() {
        textArea.setText(Global.slide.info);
        // text changed -> change it in Global.slide.info
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
//                System.out.println("New info text: " + newValue);
                Global.slide.info = newValue;
            }
        });
    }

}
