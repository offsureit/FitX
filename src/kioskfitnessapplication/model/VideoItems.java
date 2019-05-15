package src.kioskfitnessapplication.model;

import java.io.InputStream;

@SuppressWarnings("ALL")
public class VideoItems {

    private int _id;
    private String name;
    private String time;
    private String category;

    private InputStream image;

    public VideoItems() {

    }

    public VideoItems(int _id, String name, String time, InputStream image) {
        this._id = _id;
        this.name = name;
        this.time = time;
        this.image = image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
