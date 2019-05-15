/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.kioskfitnessapplication;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import src.kioskfitnessapplication.model.MenuItems;
import src.kioskfitnessapplication.model.VideoItems;
import src.kioskfitnessapplication.util.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.prefs.Preferences;

/**
 * @author Jammwal
 */
@SuppressWarnings("ALL")
public class FXMLDocumentController implements Initializable {

    private List<MenuItems> list;
    private ObservableList<MenuItems> observableList;
    private List<String> titleNames;

    private float column = 4;
    private float row = 0;

    @FXML
    private AnchorPane homePane;

    @FXML
    private ListView<MenuItems> listView;

    @FXML
    private ScrollPane videoData;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private GridPane videosGridView;

    @FXML
    private JFXDrawer drawerMenu;

    @FXML
    private ImageView iv_menu;

    @FXML
    private ImageView app_logo;

    @FXML
    private VBox nodeLeft;

    @FXML
    private VBox nodeRight;

    @FXML
    private Label title;

    @FXML
    private JFXButton addFolder;

    @FXML
    final ProgressBar progressIndicator = new ProgressBar();

    private Pane parent;

    private Preferences preferences;

    private WorkIndicatorDialog wd = null;

    @FXML
    public void OnMenuClicked() {
        drawerMenu.setVisible(true);
        drawerMenu.open();
    }

    @FXML
    public void AddFolder() {
        noVideos();
    }

    @FXML
    public void OnDrawerMenuClicked() {
        drawerMenu.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        progressIndicator.setProgress(50);
        progressIndicator.setVisible(true);

        nodeLeft.setPrefWidth(d.getWidth() / 100 * 15.72);
        listView.setMinWidth(d.getWidth() / 100 * 17);
        listView.setMaxWidth(d.getWidth() / 100 * 17);

        nodeLeft.setPrefHeight(d.getHeight() - 30);

        app_logo.setFitWidth(d.getWidth() / 100 * 10);

        //prefWidth="1618.0" layoutX="303.0"
        //Set the node right
        nodeRight.setPrefWidth(d.getWidth() / 100 * 84.27);
        nodeRight.setLayoutX(d.getWidth() / 100 * 15.72 + 1);
        nodeRight.setPrefHeight(d.getHeight());

        System.out.println("The node left value " + d.getWidth() / 100 * 15.72);
        System.out.println("The node Right value " + d.getWidth() / 100 * 84.27);

        /////////////
        videosGridView.setPrefWidth(d.getWidth() / 100 * 81.77);
        //layoutX="1200.0"
        iv_menu.setLayoutX(videosGridView.getPrefWidth() - 7);
        System.out.println("Grid view size  " + d.getWidth() / 100 * 81.77);
        preferences = Preferences.userRoot().node(PreferenceBean.USER_ROOT);
        if (AppUtil.isDirectoryExist()) {
            setTitleNames();
            observableList = FXCollections.observableList(list);
            listView.setItems(observableList);
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            listView.setPrefSize(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
            listView.setCellFactory((ListView<MenuItems> param) -> new ListViewCell());
            listView.getSelectionModel().select(0);
            listView.getSelectionModel().selectedItemProperty().addListener((ov, oldvalue, newvalue) -> {
                progressIndicator.setVisible(true);
                removeView();
                updateVideoData();
            });
            listView.setOrientation(Orientation.VERTICAL);
            removeView();
            updateVideoData();
        }

        createColumns();
        videoData.setContent(videosGridView);
        if (AppUtil.isDirectoryExist())
            designGridPane(AppUtil.getCategoryItemsByName(listView.getSelectionModel().getSelectedItem().getName()));
        drawerMenu.setVisible(false);
        setDrawerMenuItems();
        if (addData()) {
            noVideos();
            addFolder.setVisible(true);
        } else {
            addFolder.setVisible(false);
        }
    }


    private void removeView() {
        if (videosGridView.getChildren().size() > 0) {
            videosGridView.getChildren().retainAll(videosGridView.getChildren().get(0));
        }
    }

    private void updateVideoData() {

        title.setText(listView.getSelectionModel().getSelectedItem().getName());
        ArrayList<VideoItems> videos = new ArrayList<>();
        if (listView.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase(AppUtil.VIDEO_LIBRARY)) {
            for (MenuItems items : list) {
                if (!items.getName().equalsIgnoreCase(AppUtil.VIDEO_LIBRARY)) {
                    System.out.println("Videos Size is :" + AppUtil.VIDEO_LIBRARY);
                    videos.addAll(AppUtil.getCategoryItemsByName(items.getName()));
                    Collections.sort(videos, Comparator.comparing(VideoItems::getName));
//                    videos.sort(new VideoItemsComparator());
                }
            }
        } else {
            videos.addAll(AppUtil.getCategoryItemsByName(listView.getSelectionModel().getSelectedItem().getName()));
        }

        System.out.println("Videos Size is :" + videos.size());
        if ((videos.size() > 0)) {
            videosGridView.setVisible(true);
            designGridPane(videos);
            videoData.layout();
            videoData.setVvalue(0.0f);
        } else {
            System.out.println("Videos Size is :" + videos.size());
            videosGridView.setVisible(false);
            videosGridView.getRowConstraints().removeAll();
        }
        progressIndicator.setVisible(false);
    }

    private void createColumns() {
        for (int j = 0; j < column; j++) {
            ColumnConstraints columns = new ColumnConstraints();
            columns.setFillWidth(true);
            columns.setHgrow(Priority.ALWAYS);
            videosGridView.getColumnConstraints().add(columns);
        }
    }

    private void setTitleNames() {
        titleNames = AppUtil.getCategoryFolderList();
        list = new ArrayList<>();
        if (titleNames.size() > 0) {
            for (String name : titleNames) {
                int id = titleNames.indexOf(name);
                list.add(new MenuItems(name, id + 1, false));
            }
        }
    }


    private void designGridPane(List<VideoItems> videos) {

        row = videos.size() / column;

        if (row < 3) {
            row = 3;
        }

        videosGridView.getRowConstraints().clear();

        for (int i = 0; i < row; i++) {
            RowConstraints rows = new RowConstraints();
            rows.setVgrow(Priority.ALWAYS);
            rows.setFillHeight(true);
            videosGridView.getRowConstraints().add(rows);
        }

        addGridPane(videos);
    }

    private void addGridPane(List<VideoItems> videos) {
        int k = 0;
        videosGridView.setHgap(10);
        videosGridView.setVgap(10);
        videosGridView.setPadding(new Insets(10, 10, 10, 10));
        for (int i = 0; i < row; i++) {

            for (int j = 0; j < column; j++) {
                if (k < videos.size()) {
                    ///need to set the dynamic data from folder
                    videosGridView.add(getGridPaneChildNode(videos.get(k)), j, i);
                    k++;
                } else {
                    return;
                }
            }
        }

        videosGridView.autosize();
    }

    private StackPane getGridPaneChildNode(VideoItems video) {

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        double rightContainer = videosGridView.getPrefWidth() / 100 * 15.5;
        StackPane stackPane = new StackPane();
        stackPane.setMaxHeight(rightContainer);
        stackPane.setMinHeight(rightContainer);
        VBox bottom = getGridPaneChildBottom(video);
        bottom.setMaxHeight(rightContainer);
        bottom.setMinHeight(rightContainer);

        VBox top = getGridPaneChildTop(video);
        top.setMaxHeight(rightContainer);
        top.setMinHeight(rightContainer);
        ///////////////
        System.out.println("The width value is 1 " + (rightContainer));
        System.out.println("The width value is 2 " + (d.getWidth() - 25));
        StackPane.setMargin(bottom, new Insets(20, 15, 50, 15));
        StackPane.setMargin(top, new Insets(20, 15, 50, 15));
        stackPane.getChildren().addAll(bottom, top);
        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMouseClicked(event -> {

            VideoPlayerController controller = (VideoPlayerController) AppUtil.replaceSceneContent("VideoPlayer.fxml", homePane);
            controller.setCategory(video.getCategory());
            controller.setName(video.getName());
            controller.setParent(homePane);
            controller.playVideo();
        });

        return stackPane;
    }

    private VBox getGridPaneChildBottom(VideoItems video) {
        VBox vBox = new VBox();
        Color col = Color.valueOf("#322F31");
        vBox.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));
        StackPane stackPaneImage = new StackPane();
        /// This is for the the Grid View Thumbnail
        ImageView imageHouse = new ImageView();
        if (video.getImage() != null) {
            imageHouse.setImage(new Image(video.getImage()));
        }
        double value = (videosGridView.getPrefWidth() * (videosGridView.getColumnConstraints().get(0).getPercentWidth() / 100)) - 20.0;
        double rightContainer = videosGridView.getPrefWidth() / 100 * 15.5;
        imageHouse.setFitWidth(value);
        imageHouse.setFitHeight(rightContainer - 50);
        System.out.println("The Image Height is 1 " + (rightContainer - 50));
        StackPane.setAlignment(imageHouse, Pos.CENTER);
        stackPaneImage.getChildren().add(imageHouse);
        stackPaneImage.setMaxWidth(value);
        stackPaneImage.setMinWidth(value);
        stackPaneImage.setMaxHeight(value - 50);
        stackPaneImage.setMinHeight(value - 50);

        StackPane stackPaneImagePlay = new StackPane();
        ImageView imagePlay = new ImageView(
                new Image(FXMLDocumentController.class.getResourceAsStream("images/play_icon.png")));
        imagePlay.setFitWidth(25);
        imagePlay.setFitHeight(25);
        StackPane.setAlignment(imagePlay, Pos.CENTER);
        stackPaneImagePlay.getChildren().add(imagePlay);
        stackPaneImagePlay.setMaxWidth(50);
        stackPaneImagePlay.setMinWidth(50);
        stackPaneImagePlay.setMaxHeight(50);
        stackPaneImagePlay.setMinHeight(50);

        HBox hbox = new HBox();
        Label label = new Label();
        label.setText(video.getName());
        URL url = this.getClass().getResource("styles/label.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        String css = url.toExternalForm();
        label.getStylesheets().add(css);
        label.setMaxHeight(50);
        label.setMinHeight(50);
        label.setMinWidth(value - 50);
        label.setMaxWidth(value - 50);
        label.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, stackPaneImagePlay);
        if (video.getImage() != null) {
            vBox.getChildren().addAll(imageHouse, hbox);
        } else {
            vBox.getChildren().add(label);
        }

        return vBox;
    }

    private VBox getGridPaneChildTop(VideoItems video) {
        VBox vBox = new VBox();

        BorderPane borderPane = new BorderPane();

        BorderPane pane = new BorderPane();
//        Label time = new Label();
//        time.setText(video.getTime());
        URL url = this.getClass().getResource("styles/label2.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        double rightContainer = videosGridView.getPrefWidth() / 100 * 15.5;
        String css = url.toExternalForm();
        Color col = Color.rgb(0, 0, 0, 0.5);
        CornerRadii corn = new CornerRadii(5);
        Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
//        time.setBackground(background);
//        time.getStylesheets().add(css);
//        pane.setPadding(new Insets(12, 15, 12, 15));
//        pane.setRight(time);
//        BorderPane.setAlignment(pane, Pos.BOTTOM_RIGHT);
//        borderPane.setBottom(pane);
        borderPane.setMaxHeight(rightContainer - 50);
        borderPane.setMinHeight(rightContainer - 50);

        Label label = new Label();
        label.setPadding(new Insets(12, 15, 12, 15));
        label.setMaxHeight(50);
        label.setMinHeight(50);
        vBox.getChildren().addAll(borderPane, label);

        return vBox;
    }

    private void setDrawerMenuItems() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(FXMLDocumentController.class.getResource("DrawerMenuItems.fxml"));
            InputStream isin = FXMLDocumentController.class.getResourceAsStream("DrawerMenuItems.fxml");
            AnchorPane pane = loader.load(isin);
            DrawerMenuController controller = loader.getController();
            controller.setDrawerMenu(drawerMenu);
            controller.setLogoutClicked(this::showPopup);
            drawerMenu.setDirection(JFXDrawer.DrawerDirection.RIGHT);
            drawerMenu.setDefaultDrawerSize(250);
            drawerMenu.setSidePane(pane);
            drawerMenu.setOverLayVisible(false);
            drawerMenu.setResizeContent(true);
            drawerMenu.setResizableOnDrag(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawerMenu.setVisible(false);
        drawerMenu.setOnDrawerClosed(event -> {
            iv_menu.setVisible(true);
            drawerMenu.setVisible(false);
        });
        drawerMenu.setOnDrawerClosing(event -> iv_menu.setVisible(true));
        drawerMenu.setOnDrawerOpened(event -> iv_menu.setVisible(false));
    }

    private void needToLoadLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(FXMLDocumentController.class.getResource("SplashLogin.fxml"));
            InputStream isin = FXMLDocumentController.class.getResourceAsStream("SplashLogin.fxml");
            AnchorPane pane = loader.load(isin);

            if (getParent().getChildren().contains(pane)) {
                getParent().getChildren().remove(homePane);
            } else {
                getParent().getChildren().addAll(pane);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(FXMLDocumentController.class.getResource("SplashScene.fxml"));
            InputStream isin = FXMLDocumentController.class.getResourceAsStream("SplashScene.fxml");
            AnchorPane pane = loader.load(isin);

            if (getParent().getChildren().contains(pane)) {
                getParent().getChildren().remove(homePane);
            } else {
                getParent().getChildren().addAll(pane);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane getParent() {
        return parent;
    }

    public void setParent(Pane parent) {
        this.parent = parent;
    }

    private void showPopup(ClickRequest requests) {
        LogoutAlertController logout = (LogoutAlertController) AppUtil.replaceSceneContent("LogoutAlert.fxml", homePane);
        logout.setParent(homePane);
        logout.setButtonText(getButtonText(requests));
        logout.setLogoutClicked(request -> {
            switch (request) {
                case CLOSE:
                    break;

                case LOGOUT:
                    checkRequest(requests);
                    break;
            }
        });
    }

    private void checkRequest(ClickRequest request) {
        switch (request) {
            case CLOSE:
                performClose();
                break;

            case LOGOUT:
                if (UserCredentials.CRED_APP_WITH_LOGIN) {
                    performLogout();
                } else {
                    performClose();
                }
                break;
        }
    }

    private void performClose() {
        parent.getScene().getWindow().hide();
    }

    private void performLogout() {
        needToLoadLogin();
        preferences.putBoolean(PreferenceBean.IS_LOGIN, false);
    }

    private String getButtonText(ClickRequest request) {
        switch (request) {
            case CLOSE:
                return "Close";

            default:
                return "Logout";
        }
    }

    private Boolean addData() {
        return AppUtil.needDataOrNot();
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    updateMessage("2000 milliseconds");
                    updateProgress(i + 1, 10);
                }
                return true;
            }
        };
    }

    private void copyFilesComplete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText("Adding videos to FitX completed. Restart to sync videos in FitX.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reset();
        }
    }

    private void noVideos() {
        Platform.runLater(() -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File selectedDirectory = chooser.showDialog(null);
            if (null == selectedDirectory) {
                System.out.println("selectedDirectory.listFiles().length null");
            } else {

                wd = new WorkIndicatorDialog(addFolder.getScene().getWindow(), "Loading Project Files...");

                wd.addTaskEndNotification(result -> {
                    System.out.println(result);
                    wd = null; // don't keep the object, cleanup
                    copyFilesComplete();
                });

                wd.exec("123", inputParam -> {
                    try {
                        FileCopy.copy(selectedDirectory.getAbsolutePath());
//                        AppUtil.createFolders(selectedDirectory.listFiles());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new Integer(1);
                });
            }
        });
    }
}