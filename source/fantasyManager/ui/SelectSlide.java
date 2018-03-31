/*
 * 2018 Patrik Vácal.
 * This file is under CC BY-SA 4.0 license.
 * This project on github: https://github.com/gamecraftCZ/fantasyManager
 * Please do not remove this comment!
 */

package fantasyManager.ui;

import fantasyManager.BasicSlideInfo;
import fantasyManager.Global;
import fantasyManager.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class SelectSlide {

    private Pane selectPane;
    private boolean searchAlsoForCurrentSlide;

    @FXML private Label promptText;
    @FXML private TextField searchField;
    @FXML private TableView searchResultsTableView;
    @FXML private TableColumn searchResultsColumnName;
    @FXML private TableColumn searchResultsColumnValue;
    @FXML private TableColumn searchResultsColumnSelect;

    @FXML public void initialize() {
        System.out.println("Initializing slide select");

        selectPane = Global.selectSlide_Pane;
        searchAlsoForCurrentSlide = Global.selectSlide_SearchAlsoForCurrentSlide;

        promptText.setText(Global.selectSlide_Prompt);
        addSearchFieldChangeListener();
        initializeTableViewCellFactories();

        // show all slides
        newSearch("");
    }
    private void addSearchFieldChangeListener() {
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                newSearch(newValue);
            }
        });
    }
    private void initializeTableViewCellFactories() {
        searchResultsColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchResultsColumnValue.setCellValueFactory(new PropertyValueFactory<>("type"));
        Callback<TableColumn<BasicSlideInfo, String>, TableCell<BasicSlideInfo, String>> buttonCellFactory
                = new Callback<TableColumn<BasicSlideInfo, String>, TableCell<BasicSlideInfo, String>>() {
            @Override
            public TableCell call(final TableColumn<BasicSlideInfo, String> param) {
                return new TableCell<BasicSlideInfo, String>() {

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
            }
        };
        searchResultsColumnSelect.setCellFactory(buttonCellFactory);
    }


    public void selectMainSlide() {
        System.out.println("Selecting main page");
        select("index.xml");
    }
    public void cancel() {
        System.out.println("Selection canceled");
        select(null);
    }
    private void select(String slidePath) {
        System.out.println("Selecting slide: " + slidePath);
        Global.selectSlide_Selected = slidePath;
        selectPane.setVisible(false);
    }

    private void newSearch(String searchText) {
        System.out.println("Searching for: \"" + searchText + "\"");

        // search for slides
        ObservableList<BasicSlideInfo> searchMatches = FXCollections.observableArrayList();
        try {
            for (BasicSlideInfo slide : Global.slidesList) {
                if (slide.name.contains(searchText)) { // && !slide.path.equals(Global.slide.path)
                    // if we don't want search for currently opened slide and this slide is that slide
                    if (!searchAlsoForCurrentSlide) {
                        if (slide.path.equals(Global.slide.path)) {
                            continue;
                        }
                    }
                    // slide match criteria
                    if (Main.DEBUGGING) System.out.println("Slide match search criteria: \"" + slide.name + "\"");
                    BasicSlideInfo searchedSlide =
                            new BasicSlideInfo(slide.name, slide.path, getNameOfType(slide.type), slide.id);
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
