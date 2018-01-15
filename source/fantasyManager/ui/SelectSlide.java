package fantasyManager.ui;

import fantasyManager.BasicSlideInfo;
import fantasyManager.BasicSlideInfoWithButton;
import fantasyManager.Global;
import fantasyManager.JavafxExtendedFunctions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.util.ArrayList;

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

        // Listen for Search field changes
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                newSearch(newValue);
            }
        });

        // show all
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
        Button buttonPrefab = new Button("+");
        try {
            buttonPrefab = FXMLLoader.load(getClass().getResource("addButton.fxml"));
        } catch (Exception ex) {
            System.out.println("No way to get there, error: " + ex.toString());
        }
        buttonPrefab.setOnMouseClicked(e -> selectButton(e));
        ObservableList<BasicSlideInfoWithButton> searchMatches = FXCollections.observableArrayList();
        try {
            for (BasicSlideInfo slide : Global.slidesList) {
                if (slide.name.contains(searchText)) {
                    System.out.println("Slide match search criteria: \"" + slide.name + "\"");
                    Button fxButton = FXMLLoader.load(getClass().getResource("addButton.fxml"));
                        //JavafxExtendedFunctions.duplicateButton(buttonPrefab);
                    fxButton.setId("but-" + slide.path);
                    BasicSlideInfoWithButton searchedSlide =
                            new BasicSlideInfoWithButton(slide.name, slide.path, getNameOfType(slide.name), fxButton);
                    searchMatches.add(searchedSlide);
                }
            }
        } catch (Exception ex) {
            System.out.println("Slide search error: " + ex.toString());
        }

        // add search matches to scene //
        // set up table columns
        searchResultsColumnName.setCellValueFactory(
                new PropertyValueFactory<BasicSlideInfoWithButton, String>("name") );
        searchResultsColumnValue.setCellValueFactory(
                new PropertyValueFactory<BasicSlideInfoWithButton, String>("type") );
//        searchResultsColumnSelect.setCellValueFactory(
//                new PropertyValueFactory<BasicSlideInfoWithButton, Button>("fxButton") );




        ObservableList<BasicSlideInfoWithButton> data
                = FXCollections.observableArrayList(
                new BasicSlideInfoWithButton("Jacob", "Smith", "typ", new Button("+")),
                new BasicSlideInfoWithButton("Isabella", "Johnson", "typ", new Button("+")),
                new BasicSlideInfoWithButton("Ethan", "Williams", "typ", new Button("+")),
                new BasicSlideInfoWithButton("Emma", "Jones", "typ", new Button("+")),
                new BasicSlideInfoWithButton("Michael", "Brown", "typ", new Button("+"))
        );

//        TableColumn firstNameCol = new TableColumn("First Name");
//        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//
//        TableColumn lastNameCol = new TableColumn("Last Name");
//        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//
//        TableColumn actionCol = new TableColumn("Action");
        searchResultsColumnSelect.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<BasicSlideInfoWithButton, String>, TableCell<BasicSlideInfoWithButton, String>> cellFactory
                = //
                new Callback<TableColumn<BasicSlideInfoWithButton, String>, TableCell<BasicSlideInfoWithButton, String>>() {
                    @Override
                    public TableCell call(final TableColumn<BasicSlideInfoWithButton, String> param) {
                        final TableCell<BasicSlideInfoWithButton, String> cell = new TableCell<BasicSlideInfoWithButton, String>() {

                            final Button btn = new Button("+");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        BasicSlideInfoWithButton info = getTableView().getItems().get(getIndex());
                                        System.out.println(info.getName()
                                                + "   " + info.getPath()
                                                + "   " + info.getType());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        searchResultsColumnSelect.setCellFactory(cellFactory);


        searchResultsTableView.setItems(data);





//        searchResultsColumnSelect.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
//
//        Callback<TableColumn<BasicSlideInfoWithButton, String>, TableCell<BasicSlideInfoWithButton, String>> cellFactory
//                = //
//                new Callback<TableColumn<BasicSlideInfoWithButton, String>, TableCell<BasicSlideInfoWithButton, String>>() {
//                    @Override
//                    public TableCell call(final TableColumn<BasicSlideInfoWithButton, String> param) {
//                        final TableCell<BasicSlideInfoWithButton, String> cell = new TableCell<BasicSlideInfoWithButton, String>() {
//
//                            final Button btn = new Button("+");
//
//                            @Override
//                            public void updateItem(String item, boolean empty) {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                } else {
//                                    btn.setOnAction(event -> {
//                                        BasicSlideInfoWithButton info = getTableView().getItems().get(getIndex());
//                                        System.out.println(info.getName()
//                                                + "   " + info.getType()
//                                                + "   " + info.getPath());
//                                    });
//                                    setGraphic(btn);
//                                    setText(null);
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                };
//
//        searchResultsColumnSelect.setCellFactory(cellFactory);



        // add to table
//        searchResultsTableView.setItems(searchMatches);

        System.out.println("Search completed, results count: " + searchMatches.size());
    }
    private String getNameOfType(String type) {
        System.out.println("Getting name for type: \"" + type + "\"");
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

    private void selectButton(MouseEvent e) {
        String path = e.getButton().name().substring(4);
        select(path);
    }

}
