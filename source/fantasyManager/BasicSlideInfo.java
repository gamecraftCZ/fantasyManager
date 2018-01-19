package fantasyManager;

public class BasicSlideInfo {

    public String name;
    public String path;
    public String type;

    public BasicSlideInfo() {
        this.name = "";
        this.path ="";
        this.type = "";
    }
    public BasicSlideInfo(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
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


}