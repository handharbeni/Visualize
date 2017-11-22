package illiyin.mhandharbeni.databasemodule;

import android.content.Context;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

import illiyin.mhandharbeni.networklibrary.CallHttp;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import io.realm.RealmResults;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by root on 17/07/17.
 */

public class AdapterModel implements SessionListener{
    String server;
    private String endpoint_listgrup;
    private String endpoint_listchat;
    private String endpoint_creategrup;
    private String endpoint_updategrup;
    private String endpoint_deletegrup;
    private String endpoint_sendmessagegrup;
    private String endpoint_senglocationgrup;
    private String endpoint_invitemembergrup;
    private String endpoint_konfimasimasukgrup;
    private String endpoint_listcontact;
    private String endpoint_listmember;
    private String endpoint_listmemberlocation;
    private String endpoint_getchatgrup;
    private String endpoint_addcontact;
    private String endpoint_sentchat;
    private String endpoint_sentlocationgroup;
    private String endpoint_getlocationgroup;

    private String endpoint_login;
    private String endpoint_register;

    private DatabaseListener databaseListener;


    Context context;
    Crud crud;
    CallHttp callHttp;
    Session session;

    public AdapterModel(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.context = context;
        callHttp = new CallHttp(context);
        session = new Session(context, this);
        initEndPoint();
    }
    private void initEndPoint(){
        server = context.getString(R.string.module_server);
        endpoint_listgrup = server+""+context.getString(R.string.endpoint_listgrup);
        endpoint_creategrup = server+""+context.getString(R.string.endpoint_creategrup);
        endpoint_updategrup = server+""+context.getString(R.string.endpoint_updategrup);
        endpoint_deletegrup = server+""+context.getString(R.string.endpoint_deletegrup);
        endpoint_sendmessagegrup = server+""+context.getString(R.string.endpoint_sendmessagegrup);
        endpoint_senglocationgrup = server+""+context.getString(R.string.endpoint_senglocationgrup);
        endpoint_invitemembergrup = server+""+context.getString(R.string.endpoint_invitemembergrup);
        endpoint_konfimasimasukgrup = server+""+context.getString(R.string.endpoint_konfimasimasukgrup);
        endpoint_listcontact = server+""+context.getString(R.string.endpoint_listcontact);
        endpoint_listmember = server+""+context.getString(R.string.endpoint_listmember);
        endpoint_listmemberlocation = server+""+context.getString(R.string.endpoint_locationmember);
        endpoint_getchatgrup = server+""+context.getString(R.string.endpoint_getchatgrup);
        endpoint_login = server+""+context.getString(R.string.endpoint_login);
        endpoint_register = server+""+context.getString(R.string.endpoint_register);
        endpoint_addcontact= server+""+context.getString(R.string.endpoint_addcontact);
        endpoint_sentlocationgroup = server+context.getString(R.string.endpoint_setlocation);
        endpoint_getlocationgroup = server+context.getString(R.string.endpoint_getlocation);
    }
    @Override
    public void sessionChange() {

    }
    @Nullable
    public void syncDestinationGroup() throws JSONException, IllegalArgumentException {
        GrupModel gm= new GrupModel();
        Crud crudGrup = new Crud(context, gm);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            RealmResults readAllGrup = crudGrup.read();
            if (readAllGrup.size() > 0){
                for (int i=0;i<readAllGrup.size();i++){
                    GrupModel newGM = (GrupModel) readAllGrup.get(i);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("key", session.getToken())
                            .addFormDataPart("id_grup", String.valueOf(newGM.getId()))
                            .build();
                    String response = null;
                    try{
                        response = callHttp.post(endpoint_getlocationgroup, requestBody);
                    }catch(IllegalArgumentException e){

                    }
//                    String response = callHttp.post(endpoint_getlocationgroup, requestBody);
                    if (!response.isEmpty() || response != null){
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getInt("code")==300){
                            JSONArray arrayData = objectResponse.getJSONArray("data");
                            if (arrayData.length() > 0){
                                for (int k=0;k<arrayData.length();k++){
                                    JSONObject objectData = arrayData.getJSONObject(k);

                                    String id = objectData.getString("id");
                                    String id_grup = objectData.getString("id_grup");
                                    String nama_lokasi = objectData.getString("nama_lokasi");
                                    String latitude = objectData.getString("latitude");
                                    String longitude = objectData.getString("longitude");
                                    String prioritas = objectData.getString("prioritas");
                                    String type = objectData.getString("type");
                                    String date_add = objectData.getString("date_add");
                                    String date_modified = objectData.getString("date_modified");
                                    String sha = objectData.getString("sha");

                                    GrupLocationModel glm = new GrupLocationModel();
                                    Crud crudGlm = new Crud(context, glm);
                                    RealmResults resultGLM = crudGlm.read("id", Integer.valueOf(id));
                                    if (resultGLM.size() > 0){
                                        /*update*/
                                        GrupLocationModel objGLM = (GrupLocationModel) resultGLM.get(0);
                                        if (!objGLM.getSha().equalsIgnoreCase(sha)){
                                            crudGlm.openObject();
                                            objGLM.setLatitude(latitude);
                                            objGLM.setLongitude(longitude);
                                            objGLM.setNama_lokasi(nama_lokasi);
                                            objGLM.setPrioritas(Integer.valueOf(prioritas));
                                            objGLM.setType(type);
                                            objGLM.setDate_add(date_add);
                                            objGLM.setDate_modified(date_modified);
                                            objGLM.setSha(sha);
                                            crudGlm.update(objGLM);
                                            crudGlm.commitObject();
                                            crudGlm.closeRealm();
                                        }
                                    }else{
                                        /*insert*/
                                        glm.setId(Integer.valueOf(id));
                                        glm.setId_grup(Integer.valueOf(id_grup));
                                        glm.setLatitude(latitude);
                                        glm.setLongitude(longitude);
                                        glm.setNama_lokasi(nama_lokasi);
                                        glm.setPrioritas(Integer.valueOf(prioritas));
                                        glm.setType(type);
                                        glm.setDate_add(date_add);
                                        glm.setDate_modified(date_modified);
                                        glm.setSha(sha);
                                        crudGlm.create(glm);
                                        crudGlm.closeRealm();
                                    }
                                    crudGlm.closeRealm();
                                }
                            }
                        }
                    }
                }
            }
            crudGrup.closeRealm();
        }
        crudGrup.closeRealm();
    }
    @Nullable
    public void syncGrup() throws JSONException, IllegalArgumentException {
        GrupModel gm = new GrupModel();
        crud = new Crud(context, gm);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", session.getToken())
                    .build();
            String response = null;
            try{
                response = callHttp.post(endpoint_listgrup, requestBody);
            }catch(IllegalArgumentException e){

            }
//            String response = callHttp.post(endpoint_listgrup, requestBody);
            if (!response.isEmpty() || response != null){
                JSONObject objectResponse = new JSONObject(response);
                if (objectResponse.getInt("code")==300){
                    /*check data ada atau tidak*/
                    JSONArray arrayData = objectResponse.getJSONArray("data");
                    if (arrayData.length() > 0){
                        for (int i=0;i<arrayData.length();i++){
                            JSONObject objectData = arrayData.getJSONObject(i);

                            String id = objectData.getString("id");
                            String nama_grup = objectData.getString("nama_grup");
                            String id_user = objectData.getString("id_user");
                            String masa_aktif = objectData.getString("masa_aktif");
                            String date_add = objectData.getString("date_add");
                            String date_end = objectData.getString("date_end");
                            String paid = objectData.getString("paid");
                            String deleted = objectData.getString("deleted");
                            String kuota_free = objectData.getString("kuota_free");
                            String sha = objectData.getString("sha");

                            RealmResults results = crud.read("id", Integer.valueOf(objectData.getString("id")));
                            if (results.size() > 0){
                                /*check sha*/
                                GrupModel oGm = (GrupModel) results.get(0);
                                if (!oGm.getSha().equalsIgnoreCase(sha)){
                                    /*update data*/
                                    crud.openObject();
                                    oGm.setNama_grup(nama_grup);
                                    oGm.setId_user(Integer.valueOf(id_user));
                                    oGm.setMasa_aktif(Integer.valueOf(masa_aktif));
                                    oGm.setDate_add(date_add);
                                    oGm.setDate_end(date_end);
                                    oGm.setPaid(Integer.valueOf(paid));
                                    oGm.setDeleted(deleted);
                                    oGm.setKuota_free(Integer.valueOf(kuota_free.split("#")[1]));
                                    oGm.setSha(sha);
                                    crud.update(oGm);
                                    crud.commitObject();
                                }
                            }else{
                                /*insert baru*/
                                GrupModel nGm = new GrupModel();
                                nGm.setId(Integer.valueOf(id));
                                nGm.setNama_grup(nama_grup);
                                nGm.setId_user(Integer.valueOf(id_user));
                                nGm.setMasa_aktif(Integer.valueOf(masa_aktif));
                                nGm.setDate_add(date_add);
                                nGm.setDate_end(date_end);
                                nGm.setPaid(Integer.valueOf(paid));
                                nGm.setDeleted(deleted);
                                nGm.setKuota_free(Integer.valueOf(kuota_free.split("#")[1]));
                                nGm.setSha(sha);
                                crud.create(nGm);
                            }
                        }
                    }
                }
            }
            crud.closeRealm();
        }
        crud.closeRealm();
    }
    @Nullable
    public void syncListMember() throws JSONException, IllegalArgumentException {
        GrupModel gm = new GrupModel();
        crud = new Crud(context, gm);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            RealmResults allGrup = crud.read();
            if (allGrup.size() > 0){
                for (int i = 0 ; i < allGrup.size() ; i++) {
                    GrupModel resultGM = (GrupModel) allGrup.get(i);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("key", session.getToken())
                            .addFormDataPart("id_grup", String.valueOf(resultGM.getId()))
                            .build();
                    String response = null;
                    try{
                        response = callHttp.post(endpoint_listmember, requestBody);
//                        String response = callHttp.post(endpoint_listmember, requestBody);
                    }catch(IllegalArgumentException e){

                    }
                    if (!response.isEmpty() || response != null) {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getInt("code") == 300) {
                            JSONArray arrayData = objectResponse.getJSONArray("data");
                            if (arrayData.length() > 0){
                                for (int x = 0 ; x < arrayData.length() ; x++){
                                    JSONObject objectData = arrayData.getJSONObject(x);
                                    String id_grup = objectData.getString("id_grup");
                                    String id = objectData.getString("id");
                                    String nama = objectData.getString("nama");
                                    String alamat = objectData.getString("alamat");
                                    String no_telp = objectData.getString("no_telp");
                                    String email = objectData.getString("email");
                                    String image = objectData.getString("image");
                                    String sha = objectData.getString("sha");

                                    MemberModel mm = new MemberModel();
                                    Crud crudMM = new Crud(context, mm);
                                    RealmResults resultMM = crudMM.read("id", Integer.valueOf(id_grup+""+id));
                                    if (resultMM.size() > 0){
                                        /*update*/
                                        MemberModel updateMM = (MemberModel) resultMM.get(0);
                                        if (!updateMM.getSha().equalsIgnoreCase(sha)){
                                            crudMM.openObject();
                                            updateMM.setNama(nama);
                                            updateMM.setAlamat(alamat);
                                            updateMM.setNo_telp(no_telp);
                                            updateMM.setEmail(email);
                                            updateMM.setImage(image);
                                            updateMM.setSha(sha);
                                            crudMM.update(updateMM);
                                            crudMM.commitObject();
                                            crudMM.closeRealm();
                                        }
                                    }else{
                                        MemberModel newMM = new MemberModel();
                                        newMM.setId(Integer.valueOf(id_grup+""+id));
                                        newMM.setId_grup(Integer.valueOf(id_grup));
                                        newMM.setId_user(Integer.valueOf(id));
                                        newMM.setNama(nama);
                                        newMM.setAlamat(alamat);
                                        newMM.setNo_telp(no_telp);
                                        newMM.setEmail(email);
                                        newMM.setImage(image);
                                        newMM.setSha(sha);
                                        crudMM.create(newMM);
                                        crudMM.closeRealm();
                                    }
                                    crudMM.closeRealm();
                                }
                            }
                        }
                    }
                }
            }
            crud.closeRealm();
        }
        crud.closeRealm();
    }
    @Nullable
    public void syncListMemberLocation() throws JSONException, IllegalArgumentException {
        GrupModel gm = new GrupModel();
        crud = new Crud(context, gm);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            RealmResults allGrup = crud.read();
            if (allGrup.size() > 0){
                for (int i = 0 ; i < allGrup.size() ; i++) {
                    GrupModel resultGM = (GrupModel) allGrup.get(i);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("key", session.getToken())
                            .addFormDataPart("id_grup", String.valueOf(resultGM.getId()))
                            .build();
                    String response = null;

                    try{
                        response = callHttp.post(endpoint_listmemberlocation, requestBody);
                    }catch(IllegalArgumentException e){

                    }
                    if (!response.isEmpty() || response != null) {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getInt("code") == 300) {
                            JSONArray arrayData = objectResponse.getJSONArray("data");
                            if (arrayData.length() > 0){
                                for (int x = 0 ; x < arrayData.length() ; x++){
                                    JSONObject objectData = arrayData.getJSONObject(x);
                                    String id = objectData.getString("id");
                                    String id_grup = objectData.getString("id_grup");
                                    String nama = objectData.getString("nama");
                                    String image = objectData.getString("image");
                                    String latitude = objectData.getString("latitude");
                                    String longitude = objectData.getString("longitude");
                                    String sha = objectData.getString("sha");
                                    MemberLocationModel mlm = new MemberLocationModel();
                                    Crud crudMLM = new Crud(context, mlm);
                                    RealmResults resultsMLM = crudMLM.read("id", Integer.valueOf(id_grup+""+id));
                                    if (resultsMLM.size() > 0){
                                        /*update*/
                                        MemberLocationModel updateLocation = (MemberLocationModel) resultsMLM.get(0);
                                        if (!updateLocation.getSha().equalsIgnoreCase(sha)){
                                            crudMLM.openObject();
                                            updateLocation.setNama(nama);
                                            updateLocation.setImage(image);
                                            updateLocation.setLatitude(latitude);
                                            updateLocation.setLongitude(longitude);
                                            updateLocation.setSha(sha);
                                            crudMLM.update(updateLocation);
                                            crudMLM.commitObject();
                                            session.setCustomParams("CHANGELOCATION", sha);
                                            crudMLM.closeRealm();
                                        }
                                    }else{
                                        /*insert*/
                                        MemberLocationModel newMLM = new MemberLocationModel();
                                        newMLM.setId(Integer.valueOf(id_grup+""+id));
                                        newMLM.setId_grup(Integer.valueOf(id_grup));
                                        newMLM.setId_user(Integer.valueOf(id));
                                        newMLM.setNama(nama);
                                        newMLM.setImage(image);
                                        newMLM.setLatitude(latitude);
                                        newMLM.setLongitude(longitude);
                                        newMLM.setSha(sha);
                                        crudMLM.create(newMLM);
                                        crudMLM.closeRealm();
                                    }
                                    crudMLM.closeRealm();
                                }
                            }
                        }
                    }
                }
            }
            crud.closeRealm();
        }
        crud.closeRealm();
    }
    @Nullable
    public void syncChat() throws JSONException, IllegalArgumentException {
        ChatModel cm = new ChatModel();
        crud = new Crud(context, cm);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            GrupModel gm = new GrupModel();
            Crud crudGrup= new Crud(context, gm);
            RealmResults crudResults = crudGrup.read();
            if (crudResults.size()>0){
                for (int z = 0;z<crudResults.size();z++){
                    GrupModel ggm = (GrupModel) crudResults.get(z);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("key", session.getToken())
                            .addFormDataPart("id_grup", String.valueOf(ggm.getId()))
                            .build();
                    String response = null;
                    try{
                        response = callHttp.post(endpoint_getchatgrup, requestBody);
                    }catch(IllegalArgumentException e){

                    }
//                    String response = callHttp.post(endpoint_getchatgrup, requestBody);
                    if (!response.isEmpty() || response != null){
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getInt("code")==300){
                            /*check data ada atau tidak*/
                            JSONArray arrayData = objectResponse.getJSONArray("data");
                            if (arrayData.length() > 0){
                                for(int i=0;i<arrayData.length();i++){
                                    JSONObject objectArray = arrayData.getJSONObject(i);
                                    String id = objectArray.getString("id");
                                    String id_grup = objectArray.getString("id_grup");
                                    String id_user = objectArray.getString("id_user");

                                    String nama_user = objectArray.getString("nama_user");
                                    String image_user = objectArray.getString("image_user");
                                    String nama_grup = objectArray.getString("nama_grup");

                                    String type = objectArray.getString("type");
                                    String text = objectArray.getString("text");
                                    String deleted = objectArray.getString("deleted");
                                    String date_add = objectArray.getString("date_add");
                                    String sha = objectArray.getString("sha");
                                    RealmResults results = crud.read("id", Integer.valueOf(id));
                                    if (results.size() > 0){
                                        /*check update*/
                                        ChatModel oCm = (ChatModel) results.get(0);
                                        if (!oCm.getSha().equalsIgnoreCase(sha)){
                                            /*do update*/
                                            crud.openObject();
                                            oCm.setId_grup(Integer.valueOf(id_grup));
                                            oCm.setId_user(Integer.valueOf(id_user));
                                            oCm.setNama_user(nama_user);
                                            oCm.setImage_user(image_user);
                                            oCm.setNama_grup(nama_grup);
                                            oCm.setType(type);
                                            oCm.setText(text);
                                            oCm.setDeleted(deleted);
                                            oCm.setDate_add(date_add);
                                            oCm.setSha(sha);
                                            crud.update(oCm);
                                            crud.commitObject();
                                        }
                                    }else{
                                        /*insert new*/
                                        ChatModel nCm = new ChatModel();
                                        nCm.setId(Integer.valueOf(id));
                                        nCm.setId_grup(Integer.valueOf(id_grup));
                                        nCm.setId_user(Integer.valueOf(id_user));
                                        nCm.setNama_user(nama_user);
                                        nCm.setImage_user(image_user);
                                        nCm.setNama_grup(nama_grup);
                                        nCm.setType(type);
                                        nCm.setText(text);
                                        nCm.setDeleted(deleted);
                                        nCm.setDate_add(date_add);
                                        nCm.setSha(sha);
                                        crud.create(nCm);
                                    }
                                }
                            }
                        }
                    }
                }
                crudGrup.closeRealm();
            }
            crud.closeRealm();
            crudGrup.closeRealm();
        }
        crud.closeRealm();
    }
    @Nullable
    public void syncContact() throws JSONException, IllegalArgumentException {
        ContactModel cm = new ContactModel();
        crud = new Crud(context, cm);

        if (!session.getToken().equalsIgnoreCase("nothing")){
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", session.getToken())
                    .build();
            String response = null;
            try{
                response = callHttp.post(endpoint_listcontact, requestBody);
            }catch(IllegalArgumentException e) {

            }

//                String response = callHttp.post(endpoint_listcontact, requestBody);
            if (!response.isEmpty() || response != null){
                JSONObject objectResponse = new JSONObject(response);
                if (objectResponse.getInt("code")==300){
                    /*check data ada atau tidak*/
                    JSONArray arrayData = objectResponse.getJSONArray("data");
                    if (arrayData.length() > 0){
                        for (int i=0;i<arrayData.length();i++){
                            JSONObject objectData = arrayData.getJSONObject(i);
                            String id = objectData.getString("id");
                            String nama = objectData.getString("nama");
                            String alamat = objectData.getString("alamat");
                            String no_telp = objectData.getString("no_telp");
                            String email = objectData.getString("email");
                            String image = objectData.getString("image");
                            String sha = objectData.getString("sha");
                            RealmResults results = crud.read("id", Integer.valueOf(id));
                            if (results.size() > 0){
                                ContactModel oCm = (ContactModel) results.get(0);
                                if (!oCm.getSha().equalsIgnoreCase(sha)){
                                    /*do update*/
                                    crud.openObject();
                                    oCm.setNama(nama);
                                    oCm.setAlamat(alamat);
                                    oCm.setNo_telp(no_telp);
                                    oCm.setEmail(email);
                                    oCm.setImage(image);
                                    oCm.setSha(sha);
                                    crud.update(oCm);
                                    crud.commitObject();
                                }
                            }else{
                                ContactModel nCm = new ContactModel();
                                nCm.setId(Integer.valueOf(id));
                                nCm.setNama(nama);
                                nCm.setAlamat(alamat);
                                nCm.setNo_telp(no_telp);
                                nCm.setEmail(email);
                                nCm.setImage(image);
                                nCm.setSha(sha);
                                crud.create(nCm);
                            }
                        }
                    }
                }
            }
        }
        crud.closeRealm();
    }


    public String login(String email, String password) throws JSONException {
        String returns = "Login Gagal, Silakan Coba Cek Kembali";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_login, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_login, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getString("status").equalsIgnoreCase("Ok")){
            JSONArray arrayData = objectResponse.getJSONArray("data");
            if (arrayData.length()>0){
                JSONObject objectData = arrayData.getJSONObject(0);
                String sid = objectData.getString("id");
                String snama = objectData.getString("nama");
                String salamat = objectData.getString("alamat");
                String sno_telp = objectData.getString("no_telp");
                String semail = objectData.getString("email");
                String simage = objectData.getString("image");
                String sdate_add = objectData.getString("date_add");
                String sdate_modified = objectData.getString("date_modified");
                String skey = objectData.getString("key");
                String sreferral = objectData.getString("referral");
                String sconfirm = objectData.getString("confirm");
                String ssha = objectData.getString("sha");
                session.setStateLogin("true");
                session.setSession(snama, salamat, sno_telp, semail, skey, sconfirm, simage);
                returns = "Login Berhasil";
            }
        }
        return returns;
    }
    public String register(String nama, String alamat, String no_telp, String email, String password) throws JSONException {
        String returns = "Pendaftaran Gagal";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nama", nama)
                .addFormDataPart("alamat", alamat)
                .addFormDataPart("no_telp", no_telp)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_register, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_register, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Pendaftaran Berhasil.";
        }
        return returns;
    }
    public String create_grup(String namagrup, String masaaktif) throws JSONException {
        String returns = "Grup Gagal dibuat";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("namagrup", namagrup)
                .addFormDataPart("masaaktif", masaaktif)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_creategrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_creategrup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Grup Berhasil Di Buat.";
        }
        return returns;
    }
    public String update_grup(String id_grup, String namagrup, String masaaktif) throws JSONException {
        String returns = "Grup Gagal dirubah";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .addFormDataPart("namagrup", namagrup)
                .addFormDataPart("masaaktif", masaaktif)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_updategrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_updategrup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Grup Berhasil Di Buat.";
        }
        return returns;
    }
    public String delete_grup(String id_grup) throws JSONException {
        String returns = "Grup Gagal dihapus";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_login, requestBody);
        }catch(IllegalArgumentException e) {

        }//        String response = callHttp.post(endpoint_login, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Grup Berhasil Di Hapus.";
        }
        return returns;
    }
    public String send_message(String id_grup, String message) throws JSONException {
        String returns = "Grup Gagal dihapus";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .addFormDataPart("pesan", message)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_sendmessagegrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_sendmessagegrup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Pesan anda terkirim.";
        }
        return returns;
    }
    public String send_location(String latitude, String longitude) throws JSONException {
        String returns = "Grup Gagal dihapus";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("longitude", longitude)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_senglocationgrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_senglocationgrup, requestBody);
        returns = response;
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Pesan anda terkirim.";
        }
        return returns;
    }
    public String invite_member(String id_grup, String no_telp) throws JSONException {
        String returns = "Invite ";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .addFormDataPart("no_telp", no_telp)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_invitemembergrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_invitemembergrup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Pesan anda terkirim.";
        }
        return returns;
    }
    public String konfirmasi_masuk_grup(String id_grup) throws JSONException {
        String returns = "Gagal Konfirmasi";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_konfimasimasukgrup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_konfimasimasukgrup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Konfirmasi sukses.";
        }
        return returns;
    }
    public String add_contact(String notelp) throws JSONException {
        String returns = "Gagal menambahkan teman";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("no_telp", notelp)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_addcontact, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_addcontact, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Pesan add.";
        }
        return returns;
    }
    public String add_location_grup(String id_grup, String nama_lokasi, String latitude, String longitude, String prioritas, String type) throws JSONException {
        String returns = "Gagal Menambah Lokasi";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", session.getToken())
                .addFormDataPart("id_grup", id_grup)
                .addFormDataPart("nama_lokasi", nama_lokasi)
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("longitude", longitude)
                .addFormDataPart("prioritas", prioritas)
                .addFormDataPart("type", type)
                .build();
        String response = null;
        try{
            response = callHttp.post(endpoint_sentlocationgroup, requestBody);
        }catch(IllegalArgumentException e) {

        }
//        String response = callHttp.post(endpoint_sentlocationgroup, requestBody);
        JSONObject objectResponse = new JSONObject(response);
        if (objectResponse.getInt("code")==300){
            returns = "Lokasi Berhasil Ditambahkan";
        }
        return returns;
    }
}
