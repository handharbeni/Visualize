package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 10/26/17.
 */

public class MemberLocationModel extends RealmObject {
    @PrimaryKey
    int id;

    private int id_grup;
    private int id_user;
    private String nama;
    private String image;
    private String latitude;
    private String longitude;
    private String sha;
    private String type_member;
    private String date_modified;
    private String no_telp;

    public MemberLocationModel() {
    }

    public String getType_member() {
        return type_member;
    }

    public void setType_member(String type_member) {
        this.type_member = type_member;
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

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }
}