/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;

/**
 * @author Jammwal
 */
@SuppressWarnings("ALL")
public class Data {
    @FXML
    public VBox hBox;

    @FXML
    public Label label1;

    @FXML
    public Line line;

    public Data() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("listCellItems.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(String string) {
        label1.setText(string);

    }

    public VBox getBox() {
        return hBox;
    }
}
