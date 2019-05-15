package src.kioskfitnessapplication.util;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Pane;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import src.kioskfitnessapplication.FXMLDocumentController;
import src.kioskfitnessapplication.model.VideoItems;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.prefs.Preferences;

@SuppressWarnings("ALL")
public class AppUtil {

    public static String folder_path = new StringBuilder(System.getProperty("user.home")).append("/Desktop").append("/fitx").toString();

/*
    static String newFolderPath = "C:/Program Files (x86)";
    static String newFolderName = "/fitx/Videos";
    public static String folder_path = newFolderPath + newFolderName;
*/

    public static String VIDEO_LIBRARY = "Video Library";

    enum CATEGORY {
        Express_Workout("Express Workout"),
        Kickboxing("Kickboxing"),
        Yoga("Yoga"),
        Step360("Step360"),
        Interval_Training("Interval Training"),
        Prism_Rack_Tutorial("Prism Rack Tutorial"),
        Stretch("Stretch"),
        Total_Body_Workout("Total Body Workout"),
        CYCLE("Cycle");
        private String name;

        CATEGORY(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    public static void generateAppDefaultsFolders() {
        File root = new File(folder_path);
        if (!root.exists())
            root.mkdir();

        ///generate category folder
//        for (CATEGORY c : CATEGORY.values()) {
//            System.out.println(c.getName());
//            File categoryFolder = new File(folder_path + "/" + c.getName());
//            if (!categoryFolder.exists()) {
//                categoryFolder.mkdir();
//            }
//        }
    }

    public static Boolean isDirectoryExist() {
        File root = new File(folder_path);
        return root.exists();
    }

    public static void createFolders(File[] selectedDirectory) {
        for (File file : selectedDirectory) {
            if (file.isDirectory()) {
                File categoryFolder = new File(folder_path + "/" + file.getName());
                if (!categoryFolder.exists()) {
                    categoryFolder.mkdir();
                }
            }
        }

        for (File file : selectedDirectory) {
            if (file.listFiles().length > 0)
                for (File videos : file.listFiles()) {
                    if (videos.isFile() && (videos.getName().lastIndexOf(".mp4") > 0 || videos.getName().lastIndexOf(".MP4") > 0)) {

//                        final int BUFFERSIZE = 4 * 1024;
//                        File categoryFolder = new File(folder_path + "/" + file.getName());
//                        File destination = new File(categoryFolder.getAbsolutePath() + "/" + videos.getName());
//                        String sourceFilePath = videos.getAbsolutePath();
//                        String outputFilePath = destination.getAbsolutePath();
//                        System.out.println("Source Path is ::: " + sourceFilePath);
//                        System.out.println("Destination Path is ::: " + outputFilePath);
//                        try (
//                                FileInputStream fin = new FileInputStream(videos);
//                                FileOutputStream fout = new FileOutputStream(destination);
//                        ) {
//
//                            byte[] buffer = new byte[BUFFERSIZE];
//                            int bytesRead;
//
//                            while (fin.available() != 0) {
//                                bytesRead = fin.read(buffer);
//                                fout.write(buffer, 0, bytesRead);
//                            }
//
//                        } catch (Exception e) {
//                            System.out.println("Something went wrong! Reason: " + e.getMessage());
//                        }
                    }
                }
        }
    }

    public static Boolean needDataOrNot() {
        File root = new File(folder_path);
        if (root.exists()) {
            if (root.listFiles().length > 0) {
                return false;
            }
        }

        return true;
    }

    public static List<String> getCategoryFolderList() {
        File directory = new File(folder_path);
        //get all the files from a directory
        if (!directory.exists()) {
            return null;
        }
        File[] fList = directory.listFiles();
        List<String> listCategory = new ArrayList<>();
        listCategory.add(VIDEO_LIBRARY);
        for (File file : fList) {
            if (file.isDirectory()) {
                listCategory.add(file.getName());
            }
        }
        return listCategory;
    }

    public static List<VideoItems> getCategoryItemsByName(String itemName) {
        if (itemName.equalsIgnoreCase(VIDEO_LIBRARY)) {
            return new ArrayList<>();
        }

        File directory = new File(new StringBuilder(folder_path).append("/").append(itemName).toString());
        System.out.println("the category name folder is " + directory.getName());
        //get all the files from a directory
        File[] fList = directory.listFiles();

        List<VideoItems> videoItemsList = new ArrayList<>();

        for (File file : fList) {
            if (file.isFile() && file.getName().lastIndexOf(".png") > 0) {
                VideoItems videoItem = new VideoItems();
                videoItem.setName(file.getName().replaceAll(".png", ""));
                try {
                    // Set the image input io stream
                    FrameGrab grab = null;
                    InputStream imageInputStream = new FileInputStream(file);

                    // Get the Time duration of video file

                    File fileString = new File(folder_path + "/" + itemName + "/" + file.getName().replaceAll(".png", ".mp4"));
                    grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(fileString));
                    DemuxerTrack video_track = grab.getVideoTrack();

                    videoItem.setImage(imageInputStream);
                    videoItem.setCategory(itemName);

                    videoItem.setTime(getTime(Double.valueOf(video_track.getMeta().getTotalDuration()).longValue()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JCodecException e) {
                    e.printStackTrace();
                }
                videoItemsList.add(videoItem);
            }
        }
        return videoItemsList;

    }

    public static void generateThumnails() {
        File directory = new File(new StringBuilder(folder_path).toString());
        if (!directory.exists())
            return;
        //get all the files from a directory
        File[] categories = directory.listFiles();
        System.out.println("folder::: " + directory.listFiles());

        List<VideoItems> videoItemsList = new ArrayList<>();
        DemuxerTrack video_track = null;
        for (File folder : categories) {
            System.out.println("folder:::" + folder.getName());
            File videos = new File(new StringBuilder(folder_path).append("/").append(folder.getName()).toString());
            //get all the files from a directory
            File[] fList = videos.listFiles();
            for (File file : fList) {
                try {
                    // Get the Time duration of video file
                    FrameGrab grab = null;
                    File thumbnail = new File(folder_path + "/" + folder.getName() + "/" + file.getName().toLowerCase().replaceAll(".mp4", ".png"));
                    if (!thumbnail.exists()) {
                        File videoFile = new File(folder_path + "/" + folder.getName() + "/" + file.getName().replaceAll(".png", ".mp4"));
                        System.out.println("video filename " + videoFile.getName());
                        grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(videoFile));
                        video_track = grab.getVideoTrack();
                        System.out.println("Inside true  method " + videoFile.getName());
                        ///create Thumbnail Image
                        int frameNumber = video_track.getMeta().getTotalFrames() / 2;
                        Picture picture = FrameGrab.getFrameFromFile(videoFile, frameNumber);
                        BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

                        ImageIO.write(bufferedImage, "png", thumbnail);
                        System.out.println("Generated file " + videoFile.getName());
                    }
                } catch (JCodecException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Initializable replaceSceneContent(String fxml, Pane parent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(FXMLDocumentController.class.getResource(fxml));
        Pane page = new Pane();
        try (InputStream isin = FXMLDocumentController.class.getResourceAsStream(fxml)) {
            page = loader.load(isin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parent.getChildren().addAll(page);
        return loader.getController();
    }

    public static void storeUsernamePassword() {
        Preferences preferences = Preferences.userRoot().node(PreferenceBean.USER_ROOT);
        if (UserCredentials.CRED_APP_WITH_LOGIN) {
            if (preferences.get(PreferenceBean.USERNAME, "").isEmpty() && preferences.get(PreferenceBean.PASSWORD, "").isEmpty()) {
                preferences.put(PreferenceBean.USERNAME, UserCredentials.CRED_USERNAME);
                preferences.put(PreferenceBean.PASSWORD, UserCredentials.CRED_PASSWORD);
            }
        } else {
            if (preferences.get(PreferenceBean.WITHOUT_LOGIN_PASSWORD, "").isEmpty()) {
                preferences.put(PreferenceBean.WITHOUT_LOGIN_PASSWORD, UserCredentials.CRED_PASSWORD_WITHOUT);
            }
        }

    }

    public static boolean isActiveSystem(List<String> macAddressList) {
        String currentSystemAddress = AppUtil.getMacAddress();
        for (String macAddress : macAddressList) {
            if (macAddress.compareTo(currentSystemAddress) == 0)
                return true;
        }
        return false;
    }


    public static String getMacAddress() {
        InetAddress ip;
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                if (network.getHardwareAddress() != null) {
                    byte[] mac = network.getHardwareAddress();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Current MAC address : ");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String getVideoTime(Long timeDuration) {
        String time = LocalTime.MIN.plus(
                Duration.ofMinutes(timeDuration)
        ).toString();
        return time;
    }

    private static String getTime(Long timeDuration) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        log.info("Duration: " + sdf.format(new Date(duration - TimeZone.getDefault().getRawOffset())));

//        int seconds = Integer.parseInt("" + timeDuration);
//        System.out.println("Video Duration is : " + seconds);
//        int p1 = seconds % 60;
//        int p2 = seconds / 60;
//        int p3 = p2 % 60;
//        p2 = p2 / 60;

        return sdf.format(new Date((timeDuration * 1000) - TimeZone.getDefault().getRawOffset()));
//        return String.format("%02d:%02d:%02d", p2, p3, p1);
//        return p2+":"+p3+":"+p1;
    }


}