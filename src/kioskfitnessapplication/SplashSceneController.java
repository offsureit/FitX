package src.kioskfitnessapplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import src.kioskfitnessapplication.util.AppUtil;
import src.kioskfitnessapplication.util.PreferenceBean;
import src.kioskfitnessapplication.util.UserCredentials;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SplashSceneController implements Initializable {

    @FXML
    private AnchorPane splashAnchorPane;

    private Preferences preferences;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences = Preferences.userRoot().node(PreferenceBean.USER_ROOT);
//        if (!isUserLoggedIn()) {
//            AppUtil.generateAppDefaultsFolders();
//        }

        if (UserCredentials.CRED_APP_WITH_LOGIN) {
            new SplashScreen().start();
        } else {
            new SplashScreenWithoutLogin().start();
        }
//        new NeedData().start();
    }

//    class NeedData extends Thread {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(1000);
//                Platform.runLater(() -> {
//
//
////                    if (UserCredentials.CRED_APP_WITH_LOGIN) {
////                        new SplashScreen().start();
////                    } else {
////                        new SplashScreenWithoutLogin().start();
////                    }
//                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /*
     *  Call this method when you need login process
     * */
    class SplashScreen extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    System.out.println("Is Login : " + isUserLoggedIn());
                    if (isUserLoggedIn()) {
                        AppUtil.generateThumnails();
                        AppUtil.storeUsernamePassword();
                        FXMLDocumentController controller = (FXMLDocumentController) AppUtil.replaceSceneContent("FXMLDocument.fxml", splashAnchorPane);
                        controller.setParent(splashAnchorPane);
                    } else {
                        AppUtil.generateThumnails();
                        AppUtil.storeUsernamePassword();
                        AppUtil.replaceSceneContent("SplashLogin.fxml", splashAnchorPane);
                    }

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *  Call this method when you do not need login process
     * */
    class SplashScreenWithoutLogin extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    AppUtil.generateThumnails();
                    AppUtil.storeUsernamePassword();
                    FXMLDocumentController controller = (FXMLDocumentController) AppUtil.replaceSceneContent("FXMLDocument.fxml", splashAnchorPane);
                    controller.setParent(splashAnchorPane);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean isUserLoggedIn() {
        return preferences.getBoolean(PreferenceBean.IS_LOGIN, false);
    }


}
