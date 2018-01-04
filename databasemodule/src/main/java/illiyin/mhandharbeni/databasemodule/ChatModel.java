package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/21/17.
 */

public class ChatModel extends RealmObject {
    @PrimaryKey
    int id;

    private int id_grup;
    private int id_user;

    private String nama_user;
    private String image_user;
    private String nama_grup;

    private String type;
    private String text;
    private String deleted;
    private String date_add;
    private String sha;

    public ChatModel() {
    }

    public ChatModel(int id, int id_grup, int id_user, String nama_user, String image_user, String nama_grup, String type, String text, String deleted, String date_add, String sha) {
        this.id = id;
        this.id_grup = id_grup;
        this.id_user = id_user;
        this.nama_user = nama_user;
        this.image_user = image_user;
        this.nama_grup = nama_grup;
        this.type = type;
        this.text = text;
        this.deleted = deleted;
        this.date_add = date_add;
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

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
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

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public String getNama_grup() {
        return nama_grup;
    }

    public void setNama_grup(String nama_grup) {
        this.nama_grup = nama_grup;
    }
}
