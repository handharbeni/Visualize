package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/21/17.
 */

public class GrupModel extends RealmObject {
    @PrimaryKey
    int id;

    private String nama_grup;
    private int id_user;
    private int masa_aktif;
    private String date_add;
    private String date_end;
    private int paid;
    private String deleted;
    private int kuota_free;
    private String sha;
    private String sha_grup;
    private String nama_user;
    private String confirmation;

    public GrupModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_grup() {
        return nama_grup;
    }

    public void setNama_grup(String nama_grup) {
        this.nama_grup = nama_grup;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getMasa_aktif() {
        return masa_aktif;
    }

    public void setMasa_aktif(int masa_aktif) {
        this.masa_aktif = masa_aktif;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public int getKuota_free() {
        return kuota_free;
    }

    public void setKuota_free(int kuota_free) {
        this.kuota_free = kuota_free;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getSha_grup() {
        return sha_grup;
    }

    public void setSha_grup(String sha_grup) {
        this.sha_grup = sha_grup;
    }
}
