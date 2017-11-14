package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 10/26/17.
 */

public class MemberLocationModel extends RealmObject {
    @PrimaryKey
    int id;

    int id_grup;
    int id_user;
    String nama;
    String image;
    String latitude;
    String longitude;
    String sha;

    public MemberLocationModel() {
    }

    public MemberLocationModel(int id, int id_grup, int id_user, String nama, String image, String latitude, String longitude, String sha) {
        this.id = id;
        this.id_grup = id_grup;
        this.id_user = id_user;
        this.nama = nama;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sha = sha;
    }

    public int getId_grup() {
        return id_grup;
    }

    public void setId_grup(int id_grup) {
        this.id_grup = id_grup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
