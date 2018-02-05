package illiyin.mhandharbeni.visualize.utils.model;

/**
 * Created by root on 1/22/18.
 */

public class InfoWindowData {
    private String title;
    private String address;
    private String last_fetch;

    public InfoWindowData(String title, String address, String last_fetch) {
        this.title = title;
        this.address = address;
        this.last_fetch = last_fetch;
    }

    public InfoWindowData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_fetch() {
        return last_fetch;
    }

    public void setLast_fetch(String last_fetch) {
        this.last_fetch = last_fetch;
    }
}
