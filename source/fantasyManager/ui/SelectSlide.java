package fantasyManager.ui;

import fantasyManager.BasicSlideInfo;
import fantasyManager.Global;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;

public class SelectSlide {

    private Pane selectPane;

    @FXML private Label promptText;
    @FXML private TextField searchField;
    @FXML private TableView searchResultsTableView;
    @FXML private TableColumn searchResultsColumnName;
    @FXML private TableColumn searchResultsColumnValue;
    @FXML private TableColumn searchResultsColumnSelect;

    @FXML public void initialize() {
        System.out.println("Initializing slide select");
        selectPane = Global.selectSlidePane;
        promptText.setText(Global.selectSlidePrompt);

        // Listen for Search field changes \\
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                newSearch(newValue);
            }
        });

        // initialize table view \\
        searchResultsColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchResultsColumnValue.setCellValueFactory(new PropertyValueFactory<>("type"));

        Callback<TableColumn<BasicSlideInfo, String>, TableCell<BasicSlideInfo, String>> buttonCellFactory
                = new Callback<TableColumn<BasicSlideInfo, String>, TableCell<BasicSlideInfo, String>>() {
                    @Override
                    public TableCell call(final TableColumn<BasicSlideInfo, String> param) {
                        final TableCell<BasicSlideInfo, String> cell = new TableCell<BasicSlideInfo, String>() {

                            final Button btn = new Button("->");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        BasicSlideInfo slideInfo = getTableView().getItems().get(getIndex());
                                        System.out.println("Info name of slide to added: " + slideInfo.name);
                                        select(slideInfo.path);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        searchResultsColumnSelect.setCellFactory(buttonCellFactory);



        // show all slides
        newSearch("");
    }


    public void selectMainSlide() {
        System.out.println("Selecting main page");
        select("");
    }
    public void cancel() {
        System.out.println("Selection canceled");
        select(null);
    }
    private void select(String slidePath) {
        System.out.println("Selecting slide: " + slidePath);
        Global.selectSlideSelected = slidePath;
        selectPane.setVisible(false);
    }

    private void newSearch(String searchText) {
        System.out.println("Searching for: \"" + searchText + "\"");

        // search for slides
        ObservableList<BasicSlideInfo> searchMatches = FXCollections.observableArrayList();
        try {
            for (BasicSlideInfo slide : Global.slidesList) {
                if (slide.name.contains(searchText)) {
                    System.out.println("Slide match search criteria: \"" + slide.name + "\"");
                    BasicSlideInfo searchedSlide =
                            new BasicSlideInfo(slide.name, slide.path, getNameOfType(slide.type));
                    searchMatches.add(searchedSlide);
                }
            }
        } catch (Exception ex) {
            System.out.println("Slide search error: " + ex.toString());
        }

        // add search matches to scene //
        searchResultsTableView.setItems(searchMatches);


        System.out.println("Search completed, results count: " + searchMatches.size());
    }

    private String getNameOfType(String type) {
        switch (type) {
            case "characters":
                return "Postava";
            case "places":
                return "Místo";
            case "organisations":
                return "Organizace";
            default:
                return "Ostatní";
        }

    }

}