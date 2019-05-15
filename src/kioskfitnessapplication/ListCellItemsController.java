/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jammwal
 */
@SuppressWarnings("ALL")
public class ListCellItemsController implements Initializable {
    @FXML
    private HBox hBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();


        hBox.setPrefWidth(d.getWidth() / 100 * 11.72);
    }

}
