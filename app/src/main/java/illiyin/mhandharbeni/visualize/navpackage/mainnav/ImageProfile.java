package illiyin.mhandharbeni.visualize.navpackage.mainnav;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;

import de.hdodenhof.circleimageview.CircleImageView;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.databasemodule.ContactModel;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.databasemodule.MemberLocationModel;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.servicemodule.ServiceAdapter;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.MainActivity;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.utils.ProgressRequestBody;
import illiyin.mhandharbeni.visualize.utils.TaskListener;

/**
 * Created by root on 12/27/17.
 */

public class ImageProfile extends AppCompatActivity implements TaskListener, ProgressRequestBody.UploadCallbacks {
    private AdapterModel adapterModel;
    private Session session;
    private String imagePath;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(illiyin.mhandharbeni.visualize.R.layout.__navactivity_mainnav_imageprofile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_module();
        fetch_component();
    }

    private void fetch_component(){
        CircleImageView imageProfile = (CircleImageView) findViewById(illiyin.mhandharbeni.visualize.R.id.imageProfile);
        Glide.with(this)
                .load(
                        session.getCustomParams(
                                Session.IMAGE,
                                "http://enadcity.org/enadcity/wp-content/uploads/2017/02/profile-pictures.png"))
                .into(imageProfile)
                .onLoadFailed(getResources().getDrawable(illiyin.mhandharbeni.visualize.R.drawable.ic_launcher));

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);

                final Intent chooserIntent = Intent.createChooser(galleryIntent, "Pilih Image");
                startActivityForResult(chooserIntent, 1010);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1010) {
            if (data == null) {
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            assert selectedImageUri != null;
            @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
//                new Uploader(this, this).execute();
                try {
                    do_upload();
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }
            }
        }
    }
    private void do_signout(){
        ServiceAdapter serviceAdapter = new ServiceAdapter(this);
        serviceAdapter.stopService();

        GrupModel gm = new GrupModel();
        Crud crud = new Crud(this, gm);
        crud.deleteAll(GrupModel.class);
        crud.deleteAll(ContactModel.class);
        crud.deleteAll(ChatModel.class);
        crud.deleteAll(MemberModel.class);
        crud.deleteAll(MemberLocationModel.class);
        session.deleteSession();
        startActivity(new Intent(ImageProfile.this, MainActivity.class));
        finish();
    }
    private Boolean do_uploads() throws Exception{
        String returns = adapterModel.upload_image(imagePath);
        return returns.equalsIgnoreCase("Image Berhasil Diubah");
    }
    private void do_upload() throws Exception {
        String returns = adapterModel.upload_image(imagePath);
        if (returns.equalsIgnoreCase("Image Berhasil Diubah")){
            do_signout();
        }
        Toast.makeText(this, returns, Toast.LENGTH_SHORT).show();
    }
//    private void progress_upload() throws Exception {
////        showProgress();
//        File file = new File(imagePath);
//        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
//        MultipartBody.Part fileKey = MultipartBody.Part.createFormData("key", session.getToken());
//
//        Call call = adapterModel.uploadImages(filePart, fileKey);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                progressDialog.setProgress(100);
//                hideProgress();
////                hideProgress();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
//
//            }
//        });
//    }
    private void fetch_module(){
        MobileAds.initialize(this, getString(R.string.admobid));
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(R.string.admobunit);

        dialog = new ProgressDialog(this);
        adapterModel = new AdapterModel(this);
        session = new Session(this, new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
    }
    private void showDialog(){
        dialog.setMessage("Uploading");
        dialog.setCancelable(true);
        dialog.show();
    }
    private Boolean statusDialog(){
        return dialog.isShowing();
    }
    private void hideDialog(){
        dialog.dismiss();
    }

    @Override
    public void onTaskStarted() {
        showDialog();
    }

    @Override
    public void onTaskFinished() {
        hideDialog();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    class Uploader extends AsyncTask<String, Void, Boolean>{
        private Context context;
        private TaskListener taskListener;
        public Uploader(Context context, TaskListener taskListener){
            this.context = context;
            this.taskListener = taskListener;
        }
        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            taskListener.onTaskStarted();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            taskListener.onTaskFinished();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                return do_uploads();
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
            return false;
        }
    }
}
