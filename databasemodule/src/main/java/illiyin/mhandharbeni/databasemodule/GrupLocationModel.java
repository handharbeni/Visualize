package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 11/14/17.
 */

public class GrupLocationModel extends RealmObject {
    @PrimaryKey
    int id;

    private int id_grup;
    private String nama_lokasi, latitude, longitude;
    private int prioritas;
    private String type, date_add, date_modified, sha, deleted;

    public GrupLocationModel() {
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_grup() {
        return id_grup;
    }

    public void setId_grup(int id_grup) {
        this.id_grup = id_grup;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public void setNama_lokasi(String nama_lokasi) {
        this.nama_lokasi = nama_lokasi;
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

    public int getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(int prioritas) {
        this.prioritas = prioritas;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
