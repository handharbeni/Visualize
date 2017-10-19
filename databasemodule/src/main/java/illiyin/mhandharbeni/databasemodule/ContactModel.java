package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/21/17.
 */

public class ContactModel extends RealmObject {
    @PrimaryKey
    int id;

    String nama, alamat, no_telp, email, image, sha;

    public ContactModel() {
    }

    public ContactModel(int id, String nama, String alamat, String no_telp, String email, String image, String sha) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.no_telp = no_telp;
        this.email = email;
        this.image = image;
        this.sha = sha;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
