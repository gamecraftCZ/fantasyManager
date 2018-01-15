package fantasyManager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class JavafxExtendedFunctions {

    // duplicates only part of button!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! not final version !!!!!!!!!!!!!!!!!
    public static Button duplicateButton(Button button) {
        Button newButton = new Button();

        newButton.setOnMouseClicked(button.getOnMouseClicked());
        newButton.setId(button.getId());
        newButton.setText(button.getText());
        newButton.setFont(button.getFont());
        newButton.setPrefSize(button.getPrefWidth(), button.getPrefHeight());
        newButton.setLayoutY(button.getLayoutY());
        newButton.setLayoutX(button.getLayoutX());
        newButton.setVisible(button.isVisible());
        newButton.setCancelButton(button.isCancelButton());
        newButton.setDefaultButton(button.isDefaultButton());
        newButton.setAccessibleText(button.getAccessibleText());
        newButton.setDisable(button.isDisable());
        newButton.setOnKeyPressed(button.getOnKeyPressed());
        newButton.setAccessibleHelp(button.getAccessibleHelp());
        newButton.setAccessibleRole(button.getAccessibleRole());
        newButton.setAccessibleRoleDescription(button.getAccessibleRoleDescription());
        newButton.setAlignment(button.getAlignment());
        newButton.setBackground(button.getBackground());
        newButton.setBlendMode(button.getBlendMode());
        newButton.setBorder(button.getBorder());
        newButton.setCache(button.isCache());
        newButton.setCacheHint(button.getCacheHint());

        newButton.setGraphic(button.getGraphic());
        newButton.setGraphicTextGap(button.getGraphicTextGap());
        newButton.setMinSize(button.getMinWidth(), button.getMinHeight());
        newButton.setMaxSize(button.getMaxWidth(), button.getMaxHeight());
        newButton.setOnMouseClicked(button.getOnMouseClicked());
        newButton.setPrefSize(button.getPrefWidth(), button.getPrefHeight());
        newButton.setScaleX(button.getScaleX());
        newButton.setScaleY(button.getScaleY());
        newButton.setScaleZ(button.getScaleZ());
        newButton.setTranslateX(button.getTranslateX());
        newButton.setTranslateY(button.getTranslateY());
        newButton.setTranslateZ(button.getTranslateZ());
        newButton.setMouseTransparent(button.isMouseTransparent());
        newButton.setRotate(button.getRotate());
        newButton.getChildrenUnmodifiable().addAll(button.getChildrenUnmodifiable());

        return newButton;
    }

    // duplicates only part of label!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! not final version !!!!!!!!!!!!!!!!!
    public static Label duplicateLabel(Label label) {
        Label newLabel = new Label();

        newLabel.setOnMouseClicked(label.getOnMouseClicked());
        newLabel.setId(label.getId());
        newLabel.setText(label.getText());
        newLabel.setFont(label.getFont());
        newLabel.setPrefSize(label.getPrefWidth(), label.getPrefHeight());
        newLabel.setLayoutY(label.getLayoutY());
        newLabel.setLayoutX(label.getLayoutX());
        newLabel.setVisible(label.isVisible());
        newLabel.setAccessibleText(label.getAccessibleText());
        newLabel.setDisable(label.isDisable());
        newLabel.setOnKeyPressed(label.getOnKeyPressed());
        newLabel.setAccessibleHelp(label.getAccessibleHelp());
        newLabel.setAccessibleRole(label.getAccessibleRole());
        newLabel.setAccessibleRoleDescription(label.getAccessibleRoleDescription());
        newLabel.setAlignment(label.getAlignment());
        newLabel.setBackground(label.getBackground());
        newLabel.setBlendMode(label.getBlendMode());
        newLabel.setBorder(label.getBorder());
        newLabel.setCache(label.isCache());
        newLabel.setCacheHint(label.getCacheHint());

        newLabel.setGraphic(label.getGraphic());
        newLabel.setGraphicTextGap(label.getGraphicTextGap());
        newLabel.setMinSize(label.getMinWidth(), label.getMinHeight());
        newLabel.setMaxSize(label.getMaxWidth(), label.getMaxHeight());
        newLabel.setOnMouseClicked(label.getOnMouseClicked());
        newLabel.setPrefSize(label.getPrefWidth(), label.getPrefHeight());
        newLabel.setScaleX(label.getScaleX());
        newLabel.setScaleY(label.getScaleY());
        newLabel.setScaleZ(label.getScaleZ());
        newLabel.setTranslateX(label.getTranslateX());
        newLabel.setTranslateY(label.getTranslateY());
        newLabel.setTranslateZ(label.getTranslateZ());
        newLabel.setMouseTransparent(label.isMouseTransparent());
        newLabel.setRotate(label.getRotate());
        newLabel.getChildrenUnmodifiable().addAll(label.getChildrenUnmodifiable());

        return newLabel;
    }

}
