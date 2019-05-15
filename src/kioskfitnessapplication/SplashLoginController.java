package src.kioskfitnessapplication;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import src.kioskfitnessapplication.model.User;
import src.kioskfitnessapplication.util.AppUtil;
import src.kioskfitnessapplication.util.PreferenceBean;
import src.kioskfitnessapplication.util.RestApiUtil;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SplashLoginController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label invalid;

    @FXML
    private AnchorPane splashPane;

    private Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preferences = Preferences.userRoot().node(PreferenceBean.USER_ROOT);
        CornerRadii corn = new CornerRadii(20);
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, corn, Insets.EMPTY)));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> invalid.setVisible(false));
        username.textProperty().addListener((observable, oldValue, newValue) -> invalid.setVisible(false));
    }

    @FXML
    private void OnExitClicked() {
        splashPane.getScene().getWindow().hide();
    }

    @FXML
    private void OnLoginClicked() {
        if (checkLoginDetails()) {

            if (!username.getText().isEmpty())
                username.clear();

            if (!passwordField.getText().isEmpty())
                passwordField.clear();

            preferences.putBoolean(PreferenceBean.IS_LOGIN, true);

            FXMLDocumentController controller = (FXMLDocumentController) AppUtil.replaceSceneContent("FXMLDocument.fxml", splashPane);
            controller.setParent(splashPane);
        }
    }

    private Boolean checkLoginDetails() {

        User user = RestApiUtil.login(username.getText().trim(), passwordField.getText().trim());
        /// Here needs to check the data from database
        if (user != null) {
            if (AppUtil.isActiveSystem(user.getMacAddress())) {
                return true;
            } else if (user.getLicense_uses_count() == user.getLicenseCount()) {
                invalid.setText("Max License exceed, Please contact to administrator ");
                invalid.setVisible(true);
                return false;
            } else
                RestApiUtil.createUserLicense(user.getUser_id(), AppUtil.getMacAddress());
            return true;
        } else {
            invalid.setText("Invalid username or password");
            invalid.setVisible(true);
            return false;
        }

    }

}