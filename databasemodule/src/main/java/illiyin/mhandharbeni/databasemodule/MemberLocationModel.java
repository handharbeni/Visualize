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
    String nama;
    String latitude;
    String longitude;
    String sha;

    public MemberLocationModel() {
    }

    public MemberLocationModel(int id, int id_grup, String nama, String latitude, String longitude, String sha) {
        this.id = id;
        this.id_grup = id_grup;
        this.nama = nama;
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
}
