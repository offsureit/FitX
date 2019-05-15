package src.kioskfitnessapplication;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import src.kioskfitnessapplication.interfaces.OnLogoutClicked;
import src.kioskfitnessapplication.util.ClickRequest;
import src.kioskfitnessapplication.util.PreferenceBean;
import src.kioskfitnessapplication.util.UserCredentials;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@SuppressWarnings("ALL")
public class LogoutAlertController implements Initializable {

    @FXML
    private StackPane logoutPane;

    @FXML
    private JFXPasswordField password;

    @FXML
    private BorderPane alert;

    @FXML
    private Label invalid;

    @FXML
    private JFXButton btnText;

    private Pane parent;

    private String buttonText;

    private OnLogoutClicked logoutClicked;

    private Preferences preferences;

    private Pane getParent() {
        return parent;
    }

    public void setParent(AnchorPane parent) {
        this.parent = parent;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
        btnText.setText(getButtonText());
    }

    public OnLogoutClicked getLogoutClicked() {
        return logoutClicked;
    }

    public void setLogoutClicked(OnLogoutClicked logoutClicked) {
        this.logoutClicked = logoutClicked;
    }

    @FXML
    public void OnExitClicked() {
        getParent().getChildren().remove(logoutPane);
        if (getLogoutClicked() != null) {
            getLogoutClicked().logoutClicked(ClickRequest.CLOSE);
        }
    }

    @FXML
    public void OnLogoutClicked() {
        String key = "";
        if (UserCredentials.CRED_APP_WITH_LOGIN) {
            key = PreferenceBean.PASSWORD;
        } else {
            key = PreferenceBean.WITHOUT_LOGIN_PASSWORD;
        }

        if (checkDetails(key)) {
            getParent().getChildren().remove(logoutPane);
            if (getLogoutClicked() != null) {
                getLogoutClicked().logoutClicked(ClickRequest.LOGOUT);
            }
        } else {
            invalid.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences = Preferences.userRoot().node(PreferenceBean.USER_ROOT);
        password.textProperty().addListener((observable, oldValue, newValue) -> invalid.setVisible(false));

    }

    private Boolean checkDetails(String key) {
        return password.getText().trim().equalsIgnoreCase(preferences.get(key, ""));
    }
}