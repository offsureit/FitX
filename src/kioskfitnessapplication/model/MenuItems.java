/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Jammwal
 */
@SuppressWarnings("ALL")
public class MenuItems {

    private String name;
    private int _id;
    private final BooleanProperty activeProperty;

    public MenuItems(String name, int _id, boolean active) {
        this.name = name;
        this._id = _id;
        this.activeProperty = new SimpleBooleanProperty(active);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public boolean isActive() {
        return activeProperty.getValue() != null
                ? activeProperty.getValue()
                : false;
    }

    public BooleanProperty activePropertyProperty() {
        return activeProperty;
    }

    public void setActiveProperty(boolean activeProperty) {
        this.activeProperty.set(activeProperty);
    }

}