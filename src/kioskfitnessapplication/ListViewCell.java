/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication;

import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;
import src.kioskfitnessapplication.model.MenuItems;

import java.awt.*;

/**
 * @author Jammwal
 */
public class ListViewCell extends ListCell<MenuItems> {

    private final Data data;
    private final StackPane pane;

    ListViewCell() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        data = new Data();
        pane = new StackPane();
        data.line.setStartX(0);
        data.line.setEndX(d.getWidth() / 100 * 14.72);
        pane.setMinWidth(d.getWidth() / 100 * 14.72);
        pane.setPrefWidth(d.getWidth() / 100 * 14.72);
        pane.setMaxHeight(d.getHeight() / 100 * 7);
        pane.setMinHeight(d.getHeight() / 100 * 7);
        pane.getChildren().add(data.label1);
    }

    @Override
    protected void updateItem(MenuItems item, boolean empty) {
        final String ACTIVE_CLASS = "active";
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (item != null) {
            data.setInfo(item.getName());
            data.getBox().getChildren().addAll(pane);
            setGraphic(data.getBox());
            if (item.isActive() && !getStyleClass().contains(ACTIVE_CLASS)) {
                getStyleClass().add(ACTIVE_CLASS);
            } else {
                getStyleClass().remove(ACTIVE_CLASS);
            }
        } else {
            getStyleClass().remove(ACTIVE_CLASS);
        }
    }
}
