package illiyin.mhandharbeni.databasemodule;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/21/17.
 */

public class ChatModel extends RealmObject {
    @PrimaryKey
    int id;

    int id_grup;
    int id_user;
    String type;
    String text;
    String deleted;
    String date_add;
    String sha;

    public ChatModel() {
    }

    public ChatModel(int id, int id_grup, int id_user, String type, String text, String deleted, String date_add, String sha) {
        this.id = id;
        this.id_grup = id_grup;
        this.id_user = id_user;
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
}
