package src.kioskfitnessapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import src.kioskfitnessapplication.util.AppUtil;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class VideoPlayerController implements Initializable {

    @FXML
    private MediaView mediaView;

    @FXML
    private StackPane video;

    @FXML
    private ImageView play_pause;

    private String category = "";
    private String name = "";
    private Pane parent;

    private MediaPlayer mp;

    private Boolean play = false;

    private String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Pane getParent() {
        return parent;
    }

    public void setParent(AnchorPane parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void playVideo() {

        if (!getCategory().isEmpty() && !getName().isEmpty()) {
            File f = new File(AppUtil.folder_path + "/" + getCategory() + "/" + getName() + ".mp4");
            final Media m = new Media(f.toURI().toString());
            mp = new MediaPlayer(m);

            mediaView.setMediaPlayer(mp);
            mp.play();
            play = true;
            play_pause.setImage(new Image(FXMLDocumentController.class.getResourceAsStream("images/pause_icon_black.png")));
            mp.setOnEndOfMedia(() -> {
                getParent().getChildren().remove(video);
            });
        }
    }

    @FXML
    private void onCloseVideoViewClicked() {
        mp.stop();
        getParent().getChildren().remove(video);
    }

    @FXML
    private void onPlayPauseVideoViewClicked() {

        if (play) {
            play_pause.setImage(new Image(FXMLDocumentController.class.getResourceAsStream("images/play_icon_black.png")));
            mp.pause();
            play = false;
        } else {
            play_pause.setImage(new Image(FXMLDocumentController.class.getResourceAsStream("images/pause_icon_black.png")));
            mp.play();
            play = true;
        }
    }
}
