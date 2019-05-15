package src.kioskfitnessapplication.model;

import java.util.List;

/**
 *
 */
@SuppressWarnings("ALL")
public class User {

    private String name;
    private int user_id;
    private int licenseCount;
    private int license_uses_count;
    private String loginStatus;

    private List<String> macAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLicenseCount() {
        return licenseCount;
    }

    public void setLicenseCount(int licenseCount) {
        this.licenseCount = licenseCount;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getLicense_uses_count() {
        return license_uses_count;
    }

    public void setLicense_uses_count(int license_uses_count) {
        this.license_uses_count = license_uses_count;
    }

    public List<String> getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_id=" + user_id +
                ", licenseCount=" + licenseCount +
                ", license_uses_count=" + license_uses_count +
                ", loginStatus='" + loginStatus + '\'' +
                '}';
    }
}
