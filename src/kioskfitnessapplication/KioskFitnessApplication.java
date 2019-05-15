/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.kioskfitnessapplication.util.AppUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jammwal
 */
public class KioskFitnessApplication extends Application {

    private DoubleProperty fontSize = new SimpleDoubleProperty(10);

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image("/src/kioskfitnessapplication/images/fitx_logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("SplashScene.fxml"));
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        System.out.println("Mac Address= " + AppUtil.getMacAddress());
        System.out.println("Screen width = " + d.width);
        System.out.println("Screen height = " + d.height);
        Scene scene = new Scene(root, d.width, d.height);
        fontSize.bind(scene.widthProperty().add(scene.heightProperty()).divide(140));
        root.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
        stage.setScene(scene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(
                    null, t.getClass().getSimpleName() + ": " + t.getMessage());
            throw t; // don't suppress Throwable
        }

    }

}
