package fantasyManager;

import java.util.ArrayList;

public class SlideHandler {

    public String name; // name of character/place/organisation
    public String path; // path to location of this character/place/organisation
    public ArrayList<Integer> images; // list of images id's in this slide
    public ArrayList<UserButton> leftButtons; // list of buttons on left side
    public ArrayList<UserButton> rightButtons; // list of buttons on right side

    public SlideHandler(String path) {
        System.out.println("Creating editor handler, path: " + path);

    }

}
