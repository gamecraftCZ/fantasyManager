package fantasyManager;

import javafx.scene.control.Button;

public class BasicSlideInfoWithButton {

    public String name;
    public String path;
    public String type;
    public Button fxButton;

    public BasicSlideInfoWithButton() {}
    public BasicSlideInfoWithButton(String name, String path, String type, Button fxButton) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.fxButton = fxButton;
    }



    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public String getType() {
        return type;
    }
    public Button getFxButton() {
        System.out.println("ge buttttt: " + fxButton.getId());
        return fxButton;
    }

}
