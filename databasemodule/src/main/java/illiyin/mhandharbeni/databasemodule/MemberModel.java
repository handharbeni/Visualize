package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 10/4/17.
 */

public class MemberModel extends RealmObject {
    @PrimaryKey
    int id;

    private int id_grup;
    private int id_user;
    private String nama, alamat, no_telp, email, image, sha;
    private String type_member;



    public MemberModel() {
    }

    public MemberModel(int id, int id_grup, int id_user, String nama, String alamat, String no_telp, String email, String image, String sha, String type_member) {
        this.id = id;
        this.id_grup = id_grup;
        this.id_user = id_user;
        this.nama = nama;
        this.alamat = alamat;
        this.no_telp = no_telp;
        this.email = email;
        this.image = image;
        this.sha = sha;
        this.type_member = type_member;
    }

    public String getType_member() {
        return type_member;
    }

    public void setType_member(String type_member) {
        this.type_member = type_member;
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

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
