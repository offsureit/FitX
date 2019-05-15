package src.kioskfitnessapplication;

import src.kioskfitnessapplication.model.VideoItems;

import java.util.Comparator;

@SuppressWarnings("ALL")
public class VideoItemsComparator implements Comparator<VideoItems> {

    @Override
    public int compare(VideoItems o1, VideoItems o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
