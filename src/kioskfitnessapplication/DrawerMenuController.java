package src.kioskfitnessapplication;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import src.kioskfitnessapplication.interfaces.OnLogoutClicked;
import src.kioskfitnessapplication.util.ClickRequest;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class DrawerMenuController extends Stage implements Initializable {

    private JFXDrawer drawerMenu;

    private OnLogoutClicked logoutClicked;

    @FXML
    private void OnLogoutClicked() {
        if (getDrawerMenu() != null) {
            getDrawerMenu().close();
        }

        if (getLogoutClicked() != null) {
            getLogoutClicked().logoutClicked(ClickRequest.LOGOUT);
        }
    }

    @FXML
    private void OnCloseClicked() {
        if (getDrawerMenu() != null) {
            getDrawerMenu().close();
        }

        if (getLogoutClicked() != null) {
            getLogoutClicked().logoutClicked(ClickRequest.CLOSE);
        }
    }

    @FXML
    private void OnMenuClose() {
        if (getDrawerMenu() != null) {
            getDrawerMenu().close();
        }
    }

    @FXML
    private void OnMenuClicked() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private JFXDrawer getDrawerMenu() {
        return drawerMenu;
    }

    public void setDrawerMenu(JFXDrawer drawerMenu) {
        this.drawerMenu = drawerMenu;
    }

    private OnLogoutClicked getLogoutClicked() {
        return logoutClicked;
    }

    public void setLogoutClicked(OnLogoutClicked logoutClicked) {
        this.logoutClicked = logoutClicked;
    }
}
