package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 11/14/17.
 */

public class GrupLocationModel extends RealmObject {
    @PrimaryKey
    int id;

    int id_grup;
    String nama_lokasi, latitude, longitude;
    int prioritas;
    String type, date_add, date_modified, sha;

    public GrupLocationModel() {
    }

    public GrupLocationModel(int id, int id_grup, String nama_lokasi, String latitude, String longitude, int prioritas, String type, String date_add, String date_modified, String sha) {
        this.id = id;
        this.id_grup = id_grup;
        this.nama_lokasi = nama_lokasi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prioritas = prioritas;
        this.type = type;
        this.date_add = date_add;
        this.date_modified = date_modified;
        this.sha = sha;
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
